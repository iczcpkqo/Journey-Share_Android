package com.journey.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.journey.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.journey.map.network.FirebaseOperation;
import com.journey.map.network.NetworkUtils;
import com.journey.model.Peer;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.directions.v5.models.RouteLeg;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.ui.v5.listeners.SpeechAnnouncementListener;
import com.mapbox.services.android.navigation.ui.v5.voice.SpeechAnnouncement;
import com.mapbox.services.android.navigation.v5.milestone.Milestone;
import com.mapbox.services.android.navigation.v5.milestone.MilestoneEventListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NavigationViewActivity extends AppCompatActivity implements OnNavigationReadyCallback, NavigationListener {
    private NavigationView navigationView;
    private DirectionsRoute selectedRoute;
    private LatLng sourceLocation;
    private Peer currentPeer;
    private List<Peer> peers;
    private boolean isNetwork = true;
    //comment
    private  static final String BE_LONG_TO = "BelongTo";
    private  static final String ARRIVAL = "arrival";
    private  static final String ARRIVAL_DATE = "arrivalDate";
    private  static final String COMPANION = "companion";
    private  static final String DISTANCE = "distance";

    private  static final String DATE = "date";
    private  static final String DEPARTURE = "departure";
    private static final String COST = "Cost";
    boolean isSingle;
    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {

            if(message.what == FirebaseOperation.FILE_NOT_FOUND_RECORD)
            {

                isNetwork = false;
            }

            toast((String) message.obj);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Intent inte = new Intent();
            inte.putExtra("network",isNetwork);
            inte.putExtra("IS_SINGLE",isSingle);
            NavigationViewActivity.this.setResult(RESULT_OK, inte);
            finish();
            return false;
        }
    });
    private void toast(String text)
    {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_navigation_view);

        selectedRoute = (DirectionsRoute) getIntent().getExtras().get("EXTRA_SELECTED_ROUTE");
        //sourceLocation = (LatLng) getIntent().getExtras().get("EXTRA_SOURCE_LOCATION");
        currentPeer =  (Peer) getIntent().getExtras().get("CURRENT_PEER");

        peers = (List<Peer>) FirebaseOperation.encodeNetworkData( (String)getIntent().getExtras().get("CURRENT_LIST_PEER"));
        isSingle = (boolean) getIntent().getExtras().get("IS_SINGLE");
        navigationView = findViewById(R.id.nav_view);
        navigationView.onCreate(savedInstanceState);
        navigationView.initialize(this);
        //startNavigation();
    }

    public String getAllName(List<Peer> peers)
    {
        String name = "";
        Iterator<Peer> iter = peers.iterator();
        while (iter.hasNext()) {
            Peer peer = iter.next();
            if(!peer.getEmail().equals(currentPeer.getEmail()))
            {
                name += peer.getEmail()+getString(R.string.way_point_arrived);
            }
        }
        return name.substring(0,name.length()-1);
    }

    @Override
    public void onNavigationReady(boolean isRunning) {
        Log.i("NavigationViewActivity", "navigation is ready!");

        NavigationViewOptions navigationViewOptions = NavigationViewOptions.builder()
                .directionsRoute(selectedRoute)
                .navigationListener(this)
                .shouldSimulateRoute(true)
                .speechAnnouncementListener(new SpeechAnnouncementListener() {
                    @Override
                    public SpeechAnnouncement willVoice(SpeechAnnouncement announcement) {
                        Log.i("NavigationViewActivity", "announcement: " + announcement.announcement());
                        return null;
                    }
                })
                .milestoneEventListener(new MilestoneEventListener() {
                    @Override
                    public void onMilestoneEvent(RouteProgress routeProgress, String instruction, Milestone milestone) {
                        RouteLeg lg = routeProgress.currentLeg();
                        if(instruction.contains(currentPeer.getEmail()))
                        {
                            if(instruction.contains("will") || instruction.contains("Will") )
                            {
                                return;
                            }
                            if(instruction.contains(getString(R.string.arrived)) || instruction.contains(getString(R.string.left)) || instruction.contains(getString(R.string.right)))
                            {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            Message message = new Message();
                                            if(!isSingle)
                                            {
                                                Thread.sleep(5000);
                                                String allName =  getAllName(peers);
                                                FirebaseOperation operation = new FirebaseOperation(getString(R.string.firebase_record),currentPeer.getUuid(),mHandler);
                                                Map<String,Object> data = new HashMap<String,Object>();
                                                data.put(BE_LONG_TO,currentPeer.getEmail());
                                                data.put(ARRIVAL,currentPeer.getDestination());
                                                data.put(ARRIVAL_DATE,new Timestamp(new Date()));
                                                data.put(DEPARTURE, currentPeer.getStartAddress());
                                                data.put(COMPANION, allName);
                                                data.put(DISTANCE,routeProgress.distanceTraveled());
                                                double cost = ((routeProgress.distanceTraveled()/1000)*1.14)+3.8;
                                                String costString = String.format("%.2f",cost)+"€";
                                                data.put(COST,costString);
                                                data.put(DATE, new Timestamp(new Date(currentPeer.getStartTime())));
                                                operation.saveDocData(data);
                                                if(!NetworkUtils.isNetworkConnected(getApplicationContext()))
                                                {
                                                    Message ms = new Message();
                                                    ms.what = FirebaseOperation.FILE_NOT_FOUND_RECORD;
                                                    mHandler.sendMessage(ms);
                                                }

                                                return;
                                            }
                                            message.obj = "You have arrived at a leader position!";
                                            Thread.sleep(10000);
                                            message.what = FirebaseOperation.FILE_EXISTS_RECORD;

                                            mHandler.sendMessage(message);

                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();

                            }
                        }
                    }
                })
                .build();
        navigationView.startNavigation(navigationViewOptions);

    }

    @Override
    public void onStart() {
        super.onStart();
        navigationView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        navigationView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        // If the navigation view didn't need to do anything, call super
        if (!navigationView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        navigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navigationView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        navigationView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        navigationView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigationView.onDestroy();
    }

    @Override
    public void onCancelNavigation() {
        int i = 0;
        i++;
    }

    @Override
    public void onNavigationFinished() {

    }

    @Override
    public void onNavigationRunning() {
        int i = 0;
        i++;
    }
}
