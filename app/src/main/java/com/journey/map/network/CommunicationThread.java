package com.journey.map.network;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationThread implements Runnable{
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private Handler handler;
    public CommunicationThread(Socket socket , Handler mHandler)
    {
        this.clientSocket = socket;
        this.handler = mHandler;
        try{
            this.input = new BufferedReader(new InputStreamReader(
                    this.clientSocket.getInputStream()));
            this.output = new PrintWriter(new OutputStreamWriter(
                    this.clientSocket.getOutputStream()));
            CheckLive checkLive = new CheckLive(socket,handler);
            new Thread(checkLive).start();
        }
        catch (IOException e){

        }
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted())
        {

            try {

                String read = input.readLine();
                PeerNetworkData data = PeerSever.encodeNetworkData(read);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}