package com.journey.map.network;

import com.journey.model.Peer;
import com.mapbox.geojson.Point;

import java.io.Serializable;
import java.util.List;

public class FirebaseNetworkData implements Serializable{
    private List<Peer> peer;
    private Point currentPoint;
    private boolean isArrived;
    private int COMMAND_ID;


    public List<Peer> getPeer() {
        return peer;
    }

    public void setPeer(List<Peer> peer) {
        this.peer = peer;
    }

    public Point getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(Point currentPoint) {
        this.currentPoint = currentPoint;
    }

    public boolean isArrived() {
        return isArrived;
    }

    public void setArrived(boolean arrived) {
        isArrived = arrived;
    }

    public boolean isServer() {
        return isServer;
    }

    public void setServer(boolean server) {
        isServer = server;
    }

    private boolean isServer;

    public FirebaseNetworkData(List<Peer> peer, Point currentPoint, boolean isArrived, boolean isServer) {
        this.peer = peer;
        this.currentPoint = currentPoint;
        this.isArrived = isArrived;
        this.isServer = isServer;
    }
}

