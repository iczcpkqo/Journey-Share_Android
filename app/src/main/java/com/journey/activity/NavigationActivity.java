package com.journey.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.journey.MainActivity;
import com.journey.R;
import com.journey.map.OrderUser;
import com.journey.map.ParseRoutes;
import com.journey.map.ParseUserGroups;
import com.journey.map.network.FirebaseNetworkData;
import com.journey.map.network.FirebaseOperation;
import com.journey.map.network.PeerClient;
import com.journey.map.network.PeerSever;
import com.journey.model.Peer;
import com.mapbox.android.core.location.LocationEngine;
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
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
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

    Handler mHandler = new Handler(Looper.myLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg.what == FirebaseOperation.FILE_EXISTS)
            {
                toast("Loading routes.");
            }
            else if(msg.what == FirebaseOperation.FILE_NOT_FOUND)
            {
                //currentFirebase.startListnere("MAP","UID","ROUTE");
                toast("Waiting for the leader to assign the navigation routes.");
            }
            else if(msg.what == FirebaseOperation.GET_SINGLE_ROUTE)
            {
                currentRoute_1 = (DirectionsRoute) msg.obj;
                setToNavigationRoute(msg.obj);
            }
            else if(msg.what == FirebaseOperation.GET_MULTIPLE_ROUTE)
            {
                toast("Multiple anchor point routes have been got.");
                currentRoute_2 =(DirectionsRoute) msg.obj;
                setToNavigationRoute(currentRoute_2);
            }
        }
    };

    private void setToNavigationRoute(Object data)
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
        NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                .directionsRoute(currentRoute)
                .shouldSimulateRoute(true)
                .build();
        NavigationLauncher.startNavigation(NavigationActivity.this,options);
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

        Peer user1 = new Peer("user_1@user_1.com",
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
                "3344",null,null);

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
                "3030",null,null);
        Peer user3 = new Peer("user_3@user_3.com",
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
                "3030",null,null);
        peers.add(user1);
        peers.add(user2);
        peers.add(user3);
        return peers;
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


        peersList = testPeerList();
        currentUserID = "user_1@user_1.com";
        currentPeer = getCurrentPeer(currentUserID,peersList);
        currentFirebase = new FirebaseOperation("map",currentPeer.getUuid(),mHandler);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setLocationUpdata(10000,5000);
        setUpdateCallback();

    }

    private void getSingleRoute(Peer peer,Peer LeaderPeer)
    {
        Point destinationPoint = Point.fromLngLat(LeaderPeer.getLatitude(),LeaderPeer.getLongitude());
        Point originPoint = Point.fromLngLat(peer.getLatitude(),peer.getLongitude());
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

    private void getMultipleRoute(List<Peer> peers)
    {
        ArrayList<Point> UserWaypoints = new ArrayList<>();
        Point destinationPoint = null;
        Point originPoint = null;
        Iterator<Peer> iter = peers.iterator();
        while (iter.hasNext()) {
            Peer peer = iter.next();
            //change furthest
            if(peer.getLeader())
            {
                originPoint = Point.fromLngLat( peer.getLatitude(),peer.getLongitude());
                destinationPoint = Point.fromLngLat(peer.getdLatitude(),peer.getdLongtitude());
                continue;
            }
            UserWaypoints.add(Point.fromLngLat(peer.getdLatitude(), peer.getdLongtitude()));
        }

        ParseRoutes route = new ParseRoutes(peers,
                mHandler,
                getString(R.string.access_token),
                getApplicationContext(),
                NavigationActivity.this,
                navigationMapRoute,
                mapboxMap,
                mapView,
                true,
                originPoint,
                destinationPoint,
                UserWaypoints);
    }

    
    private void checkRoutes(List<Peer> listPeer)
    {
        if(currentPeer.getLeader())
        {
            getMultipleRoute(listPeer);
        }
        else
        {
            Iterator<Peer> iter = listPeer.iterator();
            Peer LeaderPeer = null;
            while (iter.hasNext()) {
                Peer peer = iter.next();
                if(peer.getLeader())
                {
                    LeaderPeer = peer;
                    getSingleRoute(currentPeer,LeaderPeer);
                    break;
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
            if(temporaryPeer.getLeader())
            {
                serverIP = temporaryPeer.getIp();
                serverPort = Integer.parseInt(temporaryPeer.getPort());
            }
            if(temporaryPeer.getEmail() == mCurretnID)
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
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}