package com.journey.map.network;

import android.os.Handler;

import java.io.IOException;
import java.net.Socket;

public class CheckLive implements Runnable{

    private Socket socket;
    Handler mainTheradHandler;

    public CheckLive(Socket socket, Handler mainTheradHandler) {
        this.socket = socket;
        this.mainTheradHandler = mainTheradHandler;
    }

    @Override
    public void run() {
        try {
            socket.sendUrgentData(0xFF);
        } catch (IOException e) {
            //
            e.printStackTrace();
        }
    }
}
