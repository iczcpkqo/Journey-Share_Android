package com.journey.map.network;

import android.os.Handler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;

public class CommunicationThread implements Runnable{
    private Socket clientSocket;
    private BufferedReader input;
    private PrintWriter output;
    private Handler handler;
    private boolean isServer;

    public static PeerNetworkData encodeNetworkData(String baseContent)
    {
        byte[] decodedBytes = Base64.getDecoder().decode(baseContent);
        ByteArrayInputStream bis = new ByteArrayInputStream(decodedBytes);
        ObjectInput in = null;
        PeerNetworkData networkData = null;
        try {
            in = new ObjectInputStream(bis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            networkData = (PeerNetworkData) in.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return networkData;
    }
    public CommunicationThread(Socket socket , Handler mHandler,boolean misServer)
    {
        this.clientSocket = socket;
        this.handler = mHandler;
        this.isServer = misServer;
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
    public void sendData(PeerNetworkData peerNetworkData)
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(peerNetworkData);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] dataBytes = bos.toByteArray();
        String data = Base64.getEncoder().encodeToString(dataBytes);

        PrintWriter writer = new PrintWriter(output, true);
        writer.println(data);
    }
    @Override
    public void run() {

        while(!Thread.currentThread().isInterrupted())
        {

            try {

                String read = input.readLine();
                PeerNetworkData data = CommunicationThread.encodeNetworkData(read);


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