package com.journey.map.network;

import android.os.Handler;
import android.os.Message;

import com.journey.model.Peer;
import com.mapbox.geojson.Point;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

public class PeerClient {
    private Thread clientThread;
    private int serverPort;
    private String serverIp;
    Message message;
    Handler mainTheradHandler;
    Socket socket;
    Peer currentPeer;
    OutputStreamWriter output;
    CommunicationThread comm;
    public PeerClient(int serverPort, String serverIp, Handler mainTheradHandler, Peer peer) {
        if(serverPort == 0)
            return;
        this.serverPort = serverPort;
        this.serverIp = serverIp;
        this.mainTheradHandler = mainTheradHandler;
        currentPeer = peer;
        startClient();
        sendData();
    }
    private void sendData()
    {

    }

    public void startClient()
    {
        this.clientThread = new Thread(){
            @Override
            public void run() {
                int n = 3;
                while (true)
                {
                    try {
                        Thread.sleep(3);
                        InetAddress sever  = InetAddress.getByName(serverIp);
                        socket = new Socket(sever,serverPort);
                        if(socket != null)
                        {
                            comm = new CommunicationThread(socket,mainTheradHandler,false);
                            //start communication with the client
                            new Thread(comm).start();
                            //request routes
                            PeerNetworkData data = new PeerNetworkData(currentPeer, Point.fromLngLat(currentPeer.getLongitude(),currentPeer.getLatitude()),false,false,CommunicationThread.REQUEST_ROUTES);
                            comm.sendData(data);
                            break;
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        n--;
                        if(n == 0)
                        {
                            String str = "Leader disconnect";
                            break;
                        }

                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }

            }
        };
        this.clientThread.start();
    }

}
