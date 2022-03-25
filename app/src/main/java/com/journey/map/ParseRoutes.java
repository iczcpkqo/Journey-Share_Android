package com.journey.map;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.ArrayList;

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
    public ParseRoutes(String accessToken,
                       Context currentContext,
                       Activity activity,
                       NavigationMapRoute mapRoute,
                       MapboxMap map,
                       MapView mv,
                       boolean isNavigation,
                       Point or,
                       Point de,
                       ArrayList<Point> UserWaypoints
    )
    {
        navigationFlg =  isNavigation;
        mapActivity = activity;
        mapboxMap = map;
        mapview = mv;
        token = accessToken;
        origin = or;
        destination = de;
        context = currentContext;
        waypoints = UserWaypoints;
        getMultipleWaypointRoute();
        navigationMapRoute = mapRoute;
    }

    public ParseRoutes(String accessToken,
                       Context currentContext,
                       Activity activity,
                       NavigationMapRoute mapRoute,
                       MapboxMap map,
                       MapView mv,
                       boolean isNavigation,
                       Point or,
                       Point de)
    {
        navigationFlg =  isNavigation;
        mapActivity = activity;
        token = accessToken;
        mapboxMap = map;
        mapview = mv;
        origin = or;
        destination = de;
        context = currentContext;
        getSingleRoute();
        navigationMapRoute = mapRoute;
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
                        if(navigationMapRoute != null)
                        {
                            navigationMapRoute.removeRoute();
                        }else
                        {
                            navigationMapRoute = new NavigationMapRoute(null,mapview,mapboxMap);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                        if(navigationFlg)
                        {
                            NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                                    .directionsRoute(currentRoute)
                                    .shouldSimulateRoute(true)
                                    .build();
                            NavigationLauncher.startNavigation(mapActivity,options);
                        }
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

        for (Point waypoint : waypoints) {
            builder.addWaypoint(waypoint);
        }
        builder.build().getRoute(new Callback<DirectionsResponse>() {
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
                if(navigationMapRoute != null)
                {
                    navigationMapRoute.removeRoute();
                }else
                {
                    navigationMapRoute = new NavigationMapRoute(null,mapview,mapboxMap);
                }
                navigationMapRoute.addRoute(currentRoute);
                if(navigationFlg)
                {
                    NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                            .directionsRoute(currentRoute)
                            .shouldSimulateRoute(true)
                            .build();
                    NavigationLauncher.startNavigation(mapActivity,options);
                }
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });

    }
}
