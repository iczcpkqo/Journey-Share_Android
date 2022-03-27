package com.journey.map.network;

import com.journey.model.Peer;
import com.mapbox.geojson.Point;

import java.io.Serializable;

public class PeerNetworkData implements Serializable {
    private Peer peer;
    private Point  currentPoint;
    private boolean isArrived;

    public  PeerNetworkData(Peer p,Point point,boolean arrived)
    {
        peer = p;
        currentPoint = point;
        isArrived = arrived;
    }


    public Peer getPeer() {
        return peer;
    }

    public void setPeer(Peer peer) {
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


}
