package com.journey.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.journey.R;
import com.journey.map.network.NetworkUtils;
import com.journey.service.database.ChatingService;
import com.journey.map.ParseRoutes;
import com.journey.map.network.FirebaseOperation;
import com.journey.model.Peer;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import org.apache.commons.lang3.SerializationUtils;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigationActivity extends AppCompatActivity implements
        OnMapReadyCallback, PermissionsListener {

    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;
    private static final String TAG = "Main";
    private NavigationMapRoute navigationMapRoute;
    private List<Peer> peersList;
    private String currentUserID;
    private Peer currentPeer;
    private List<Peer> otherPeers = new ArrayList<Peer>();
    private String serverIP = "";
    private int serverPort ;
    FirebaseOperation currentFirebase ;
    private String mapDatabase = "map";


    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    Location currentLocation;
    boolean routeTag = false;
    DirectionsRoute currentRoute_1;
    DirectionsRoute currentRoute_2;
    Button navigationButton;
    NetworkUtils network = new NetworkUtils();



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityReenter(resultCode, data);
        if (resultCode == RESULT_OK) {
            boolean network = data.getExtras().getBoolean("network");
            boolean isSingle = data.getExtras().getBoolean("IS_SINGLE");

            if( network == true && isSingle == true && peersList.size() != 1)
            {


                toast("Waiting for the leader to start the journey.");
                FirebaseOperation fir = new FirebaseOperation("map",currentPeer.getUuid(),mHandler);
                fir.startListnere("map",currentPeer.getUuid(),"START");
            }
            else if(network == false && isSingle == false)
            {
                toast("Journey record is saved in the cache !");
            }
            ChatingService.addWithMe(getUserNames(peersList));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            toast("You have arrived at your destination.");

            finish();
        }
    }





    Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == FirebaseOperation.FILE_EXISTS)
            {
                toast("Loading routes.");
                setToNavigationRoute(currentRoute_2,false);
            }
            else if(msg.what == FirebaseOperation.START_NAVIGATION)
            {
                setToNavigationRoute(currentRoute_2,false);
            }
            else if(msg.what == FirebaseOperation.FILE_NOT_FOUND)
            {
                //currentFirebase.startListnere("MAP","UID","ROUTE");
                toast((String) msg.obj);
            }
            else if(msg.what == FirebaseOperation.GET_SINGLE_ROUTE)
            {
                currentRoute_1 = (DirectionsRoute) msg.obj;
                setToNavigationRoute(msg.obj,true);
            }
            else if(msg.what == FirebaseOperation.GET_MULTIPLE_ROUTE)
            {
                toast("Multiple anchor point routes have been got.");
                currentRoute_2 =(DirectionsRoute) msg.obj;
                setToNavigationRoute(currentRoute_2,false);
            }
            else if(msg.what == FirebaseOperation.ARRIVED_LEADER)
            {

            }
            else if(msg.what == FirebaseOperation.FILE_NOT_FOUND_RECORD)
            {
                toast((String) msg.obj);
            }
            else if(msg.what == FirebaseOperation.FILE_EXISTS_RECORD)
            {
                toast((String) msg.obj);
            }
            else if(msg.what == FirebaseOperation.SAVE_ROUTE)
            {
                currentRoute_2 = (DirectionsRoute) msg.obj;
            }
            else if(msg.what == FirebaseOperation.FIELD_NOT_FOUND)
            {
                if(!NetworkUtils.isNetworkConnected(getApplicationContext()))
                {
                    toast("Automatic navigation due to network loss !");
                    setToNavigationRoute(currentRoute_2,false);
                }
                FirebaseOperation fir = new FirebaseOperation("map",currentPeer.getUuid(),mHandler);
                fir.startListnere("map",currentPeer.getUuid(),"START");
            }
        }
    };

    private List<String> getUserNames(List<Peer> peersList)
    {
        List<String> names = new ArrayList<String>();
        Iterator<Peer> iter = peersList.iterator();
        while (iter.hasNext()) {
            Peer peer = iter.next();
            names.add(peer.getEmail());
        }
        return names;
    }
    private void setToNavigationRoute(Object data,boolean isSingle)
    {

        DirectionsRoute currentRoute = (DirectionsRoute) data;

        if(navigationMapRoute != null)
        {
            navigationMapRoute.removeRoute();
        }else
        {
            navigationMapRoute = new NavigationMapRoute(null,mapView,mapboxMap);
        }
        navigationMapRoute.addRoute(currentRoute);

        if (currentRoute != null) {
            Intent intent = new Intent(NavigationActivity.this, NavigationViewActivity.class);
            intent.putExtra("EXTRA_SELECTED_ROUTE", currentRoute);
            intent.putExtra("CURRENT_PEER",currentPeer);
            intent.putExtra("IS_SINGLE",isSingle);
            intent.putExtra("CURRENT_LIST_PEER",FirebaseOperation.getObjectString(peersList) );

            //startActivity(intent);
            startActivityForResult(intent,1);

        }

    }


    private void toast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    private List<Peer> testPeerList()
    {
        //53.3498, -6.2603
        //origin:53.3496, -6.2600
        //waypoint :53.3480, -6.2593
        //waypoint :53.3457, -6.2573
        //waypoint :53.339132, -6.272588
        //destination: 53.3446, -6.2595
        List<Peer> peers = new ArrayList<Peer>();
        //10.0.2.16
        String serverIP = getLocalIpAddress();

        Peer user1 = new Peer("liu@tcd.com",
                "Female",
                12,
                4.5,
                53.3481,
                -6.2594,
                53.3481,
                -6.2483,
                0L,
                0L,
                1,
                "12",
                12,
                true,
                true,
                "123456789",
                true,
                null,
                serverIP,
                "3344",null,null,null,null,null);

        Peer user2 = new Peer("user_2@user_2.com",
                "Female",
                12,
                4.5,
                53.3479,
                -6.2576,
                53.3483,
                -6.2551,
                0L,
                0L,
                1,
                "12",
                12,
                false,
                false,
                "123456789",
                true,
                null,
                "127.0.0.1",
                "3030",null,null, null, null, null);
        Peer user3 = new Peer("liu@tcd.com",
                "Female",
                12,
                4.5,
                53.3490,
                -6.2588,
                53.3483,
                -6.2515,
                0L,
                0L,
                1,
                "12",
                12,
                false,
                false,
                "123456789",
                true,
                null,
                "127.0.0.1",
                "3030",null,null,null,null,null);
        peers.add(user1);
        //peers.add(user2);
        peers.add(user3);
        return peers;
    }
    private boolean isLeader(List<Peer>peers,Peer peer)
    {
        boolean isLeader = false;

        Iterator<Peer> iter = peers.iterator();
        while (iter.hasNext())
        {
            Peer iterPeer = iter.next();
            if(iterPeer.getLeader() && peer.getEmail().equals(iterPeer.getEmail()))
            {
                isLeader = true;
                break;
            }
            else
            {
                isLeader = false;
                break;
            }

        }

        return isLeader;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.

        Mapbox.getInstance(this, getString(R.string.access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_map_navigation);

        mapView = findViewById(R.id.navigationView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        peersList = (List<Peer>) FirebaseOperation.encodeNetworkData((String) getIntent().getExtras().get(getString(R.string.PEER_LIST)));
        currentUserID = (String) getIntent().getExtras().get(getString(R.string.CURRENT_PEER_EMAIL));

        //peersList = testPeerList();
        //currentUserID = "liu@tcd.com";
        //FirebaseOperation.fuzzyQueries("users","email",currentUserID,mHandler);
        currentPeer = getCurrentPeer(currentUserID,peersList);
        currentFirebase = new FirebaseOperation("map",currentPeer.getUuid(),mHandler);
        navigationButton = findViewById(R.id.select_navigation_button);
        if(!isLeader(peersList,currentPeer))
        {
            navigationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setToNavigationRoute(currentRoute_2,false);
                }
            });
        }
        else
        {
            navigationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toast("Navigate to Destination !");
                    Map<String, Object> data = new HashMap<String,Object>();
                    data.put("START",new String("1"));
                    FirebaseOperation db = new FirebaseOperation("map",currentPeer.getUuid(),mHandler);
                    db.saveDocData("map",currentPeer.getUuid(),data,FirebaseOperation.START_NAVIGATION);
                }
            });
        }


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setLocationUpdata(10000,5000);
        setUpdateCallback();

    }

    private void getSingleRoute(Peer peer,Peer LeaderPeer,boolean isLeader)
    {
        Point destinationPoint;
        if(!isLeader)
        {
            destinationPoint = Point.fromLngLat(LeaderPeer.getLongitude(),LeaderPeer.getLatitude());
        }
        else
        {
            destinationPoint = Point.fromLngLat(LeaderPeer.getdLongtitude(),LeaderPeer.getdLatitude());

        }
        Point originPoint = Point.fromLngLat(peer.getLongitude(),peer.getLatitude());
        ParseRoutes route = new ParseRoutes(peer,
                mHandler,
                getString(R.string.access_token),
                getApplicationContext(),
                NavigationActivity.this,
                navigationMapRoute,
                mapboxMap,
                mapView,
                true,
                originPoint,
                destinationPoint);
    }

    private void getMultipleRoute(List<Peer> peers,Boolean isNavigation)
    {
        ArrayList<Point> UserWaypoints = new ArrayList<>();
        Point destinationPoint = null;
        Point originPoint = null;
        Iterator<Peer> iter = peers.iterator();
        String destinationName = "";
        String wayPointsName = "Start;";
        while (iter.hasNext()) {
            Peer peer = iter.next();
            //change furthest
            if(peer.getLeader())
            {
                originPoint = Point.fromLngLat(peer.getLongitude(), peer.getLatitude());
                destinationPoint = Point.fromLngLat(peer.getdLongtitude(),peer.getdLatitude());
                destinationName = peer.getEmail();
                continue;
            }
            UserWaypoints.add(Point.fromLngLat(peer.getdLongtitude(),peer.getdLatitude()));
            wayPointsName += peer.getEmail()+getString(R.string.way_point_arrived);
        }
        wayPointsName+= destinationName;
        ParseRoutes route = new ParseRoutes(peers,
                mHandler,
                getString(R.string.access_token),
                getApplicationContext(),
                NavigationActivity.this,
                navigationMapRoute,
                mapboxMap,
                mapView,
                isNavigation,
                originPoint,
                destinationPoint,
                UserWaypoints,wayPointsName);


    }

    
    private void checkRoutes(List<Peer> listPeer)
    {
        if(currentPeer.getLeader())
        {
            if(listPeer.size() == 1)
            {
                navigationButton.setVisibility(View.INVISIBLE);
                getSingleRoute(currentPeer, currentPeer,true);
            }
            else
                getMultipleRoute(listPeer,false);
        }
        else
        {
            Iterator<Peer> iter = listPeer.iterator();
            Peer LeaderPeer = null;
            while (iter.hasNext()) {
                Peer peer = iter.next();
                if(peer.getLeader())
                {
                    //to leader
                    LeaderPeer = peer;
                    getSingleRoute(currentPeer,LeaderPeer,false);
                    continue;
                }
                if(peer.getEmail().equals(currentPeer.getEmail()))
                {
                    getMultipleRoute(listPeer,false);
                }
            }

        }



    }
    
    private void createCommunication(Peer currentPeer)
    {
        checkRoutes(peersList);
    }

    private String getServerIP()
    {
        return serverIP;
    }
    private int getServerPort()
    {
        return serverPort;
    }

    private Peer getCurrentPeer(String mCurretnID,List<Peer> peersList)
    {
        Peer currentPeer = null;
        Iterator<Peer> iter = peersList.iterator();
        while (iter.hasNext())
        {
            Peer temporaryPeer = iter.next();
            if(temporaryPeer.getEmail().equals(mCurretnID))
            {
                currentPeer = temporaryPeer;
            }
            else
            {
                otherPeers.add(temporaryPeer);
            }
        }
        return currentPeer;
    }

    private  byte[] testSerializable(DirectionsRoute object) {
        byte[] data = SerializationUtils.serialize(object);
        return data;
    }

    private  DirectionsRoute unSerializable(byte[] data) {
        DirectionsRoute d2 = SerializationUtils.deserialize(data);
        return d2;
    }

    private void getRoute(Point origin,Point destination)
    {

        NavigationRoute.builder(getApplicationContext())
                .accessToken( getString(R.string.access_token))
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                        if(response.body() == null)
                        {
                            Log.e("NavigationRoute","No routes found, check right user and access token");
                            return;
                        }else if(response.body().routes().size() == 0)
                        {
                            Log.e("NavigationRoute","No route found");
                            return;
                        }


                        DirectionsRoute currentRoute = response.body().routes().get(0);
                        if(navigationMapRoute != null)
                        {
                            navigationMapRoute.removeRoute();
                        }else
                        {
                            navigationMapRoute = new NavigationMapRoute(null,mapView,mapboxMap);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {

                    }
                });



    }

    private void setUpdateCallback()
    {
        //instantiating the LocationCallBack
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    if (locationResult == null) {
                        return;
                    }
                    //Showing the latitude, longitude and accuracy on the home screen.
                    for (Location location : locationResult.getLocations()) {
                        currentLocation = location;
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }
    private void setLocationUpdata(int updateMillisecond,int fastUpdateMillisecond)
    {

        //Instantiating the Location request and setting the priority and the interval I need to update the location.
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(updateMillisecond);
        locationRequest.setFastestInterval(fastUpdateMillisecond);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        NavigationActivity.this.mapboxMap = mapboxMap;




        createCommunication(currentPeer);
        mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
            }
        });
    }

    @SuppressWarnings( {"MissingPermission"})
    private void enableLocationComponent(@NonNull Style loadedMapStyle) {
        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(this)) {

            // Enable the most basic pulsing styling by ONLY using
            // the `.pulseEnabled()` method
            LocationComponentOptions customLocationComponentOptions = LocationComponentOptions.builder(this)
                    .pulseEnabled(true)
                    .build();

            // Get an instance of the component
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            // Activate with options
            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(this, loadedMapStyle)
                            .locationComponentOptions(customLocationComponentOptions)
                            .build());

            // Enable to make component visible
            locationComponent.setLocationComponentEnabled(true);

            // Set the component's camera mode
            locationComponent.setCameraMode(CameraMode.TRACKING);

            if (locationComponent.getLastKnownLocation()!= null) {
                Location lastKnownLocation = mapboxMap.getLocationComponent().getLastKnownLocation();
                double lat = lastKnownLocation.getLatitude();
                double longitude = lastKnownLocation.getLongitude();
            }
            // Set the component's render mode
            locationComponent.setRenderMode(RenderMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        } else {
            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent inte = new Intent();
        NavigationActivity.this.setResult(RESULT_OK, inte);
        finish();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}