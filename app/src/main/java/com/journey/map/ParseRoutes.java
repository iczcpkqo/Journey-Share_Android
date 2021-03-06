package com.journey.map;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.journey.map.network.FirebaseOperation;
import com.journey.model.Peer;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParseRoutes {

    Point origin;
    Point destination;
    private String token;
    DirectionsRoute currentRoute;
    ArrayList<Point> waypoints;
    Context context;
    NavigationMapRoute navigationMapRoute;
    private MapboxMap mapboxMap;
    private MapView mapview;
    boolean navigationFlg;
    Activity mapActivity;
    Handler mainHandler;
    Peer currentPeer;
    List<Peer> currentPeers;
    String wayPointsName;
    public ParseRoutes(List<Peer> peers,
                       Handler handler,
                       String accessToken,
                       Context currentContext,
                       Activity activity,
                       NavigationMapRoute mapRoute,
                       MapboxMap map,
                       MapView mv,
                       boolean isNavigation,
                       Point or,
                       Point de,
                       ArrayList<Point> UserWaypoints,
                       String mWayPointsName
    )
    {
        currentPeers = peers;
        mainHandler = handler;
        navigationFlg =  isNavigation;
        mapActivity = activity;
        mapboxMap = map;
        mapview = mv;
        token = accessToken;
        origin = or;
        destination = de;
        context = currentContext;
        waypoints = UserWaypoints;
        navigationMapRoute = mapRoute;
        wayPointsName = mWayPointsName;

        getMultipleWaypointRoute();
    }

    public ParseRoutes(Peer  peer,
                       Handler handler,
                       String accessToken,
                       Context currentContext,
                       Activity activity,
                       NavigationMapRoute mapRoute,
                       MapboxMap map,
                       MapView mv,
                       boolean isNavigation,
                       Point or,
                       Point de)
    {
        currentPeer = peer;
        mainHandler = handler;
        navigationFlg =  isNavigation;
        mapActivity = activity;
        token = accessToken;
        mapboxMap = map;
        mapview = mv;
        origin = or;
        destination = de;
        context = currentContext;
        navigationMapRoute = mapRoute;
        getSingleRoute();
    }

    public  DirectionsRoute getRoute() {
        return currentRoute;
    }

     private void getSingleRoute()
    {


        NavigationRoute.builder(context)
                .accessToken(token)
                .origin(origin)
                .destination(destination)
                .profile(DirectionsCriteria.PROFILE_WALKING)
                .addWaypointNames("Start;"+currentPeer.getEmail())
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        assert response.body() != null;
                        if(response.body().routes().size() == 0)
                        {
                            Log.e("NavigationRoute","No route found");
                            assert response.body().routes().size() == 0;
                            return;
                        }
                        currentRoute = response.body().routes().get(0);
                        Message message = new Message();
                        message.what = FirebaseOperation.GET_SINGLE_ROUTE;
                        message.obj = currentRoute;
                        mainHandler.sendMessage(message);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                    }
                });
    }
    private void  getMultipleWaypointRoute()
    {
        NavigationRoute.Builder builder = NavigationRoute.builder(context);
        builder.accessToken(token)
            .origin(origin)
            .destination(destination)
            .profile(DirectionsCriteria.PROFILE_DRIVING);
        if(waypoints.size() != 0 )
        {
            for (Point waypoint : waypoints) {
                builder.addWaypoint(waypoint);
            }


        }
        builder.addWaypointNames(wayPointsName);


        builder.build().getRoute(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {

                if(response.body().routes().size() == 0)
                {

                    Log.e("NavigationRoute","No route found");
                    assert response.body().routes().size() == 0;
                    return;
                }
                currentRoute = response.body().routes().get(0);
                Message message = new Message();
                if(navigationFlg)
                {
                    message.what = FirebaseOperation.GET_MULTIPLE_ROUTE;
                }
                else
                {
                    message.what = FirebaseOperation.SAVE_ROUTE;
                }
                message.obj = currentRoute;
                mainHandler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });

    }
}
