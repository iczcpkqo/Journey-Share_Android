package com.journey.map.network;

import com.journey.model.Peer;
import com.mapbox.geojson.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PeerNetworkData implements Serializable {
    private Peer peer;
    private Point  currentPoint;
    private boolean isArrived;
    private boolean isServer;
    private int COMMAND_ID;

    public List<String> getCommand() {
        return command;
    }

    public void setCommand(List<String> command) {
        this.command = command;
    }

    private List<String> command;

    public  PeerNetworkData(Peer p,Point point,boolean arrived,boolean served,int commandId)
    {
        peer = p;
        currentPoint = point;
        isArrived = arrived;
        isServer = served;
        command = new ArrayList<String>();
        COMMAND_ID = commandId;
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
