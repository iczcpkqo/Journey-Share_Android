package com.journey.map.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.journey.model.Peer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class PeerSever {
    private ServerSocket serverSocket;
    private Thread serverThread;
    private int serverPort;
    Message message;
    Handler mainTheradHandler;
    List<peerNetowrkInformation> clientList;
    List<Peer> peersList;

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

    public PeerSever(int port,Handler handler,List<Peer> peers)
    {
        mainTheradHandler = handler;
        serverPort = port;
        clientList = new ArrayList<peerNetowrkInformation>();
        peersList = peers;
    }
    public void startServer()
    {
        this.serverThread = new Thread(){
            public void run(){
                try {
                    serverSocket = new ServerSocket(serverPort);

                } catch (IOException e) {
                    Log.e("Server:", e.getMessage());
                    e.printStackTrace();
                }
                if(null != serverThread){
                    while(!Thread.currentThread().isInterrupted()){
                        try {
                            Socket socket = serverSocket.accept();

                            CommunicationThread comm = new CommunicationThread(socket,mainTheradHandler);
                            //start communication with the client
                            new Thread(comm).start();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        };
        this.serverThread.start();
    }

    class peerNetowrkInformation {

        private Socket currentSocket;
        private Peer currentPeer;

        public peerNetowrkInformation(Socket socket,Peer peer)
        {
            currentSocket = socket;
            currentPeer = peer;
        }
        public Socket getSocket() {
            return currentSocket;
        }

        public void setSocket(Socket socket) {
            this.currentSocket = socket;
        }

        public Peer getPeer() {
            return currentPeer;
        }


    }




}

