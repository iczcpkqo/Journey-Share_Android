package com.journey.map.network;

import android.os.Handler;
import android.os.Message;

import com.journey.model.Peer;

import java.io.IOException;
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
    List<Peer> peersList;
    public PeerClient(int serverPort, String serverIp, Handler mainTheradHandler, List<Peer> peers) {
        this.serverPort = serverPort;
        this.serverIp = serverIp;
        this.mainTheradHandler = mainTheradHandler;
        peersList = peers;
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
                            CommunicationThread comm = new CommunicationThread(socket,mainTheradHandler);
                            //start communication with the client
                            new Thread(comm).start();
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
    }

}
