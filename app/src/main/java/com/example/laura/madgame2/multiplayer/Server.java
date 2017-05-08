package com.example.laura.madgame2.multiplayer;


import android.util.Log;

import com.example.laura.madgame2.MultiplayerLobbyActivity;
import com.example.laura.madgame2.utils.ActivityUtils;
import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Philipp on 03.04.17.
 */

public class Server extends Thread {

    private String playerName = "";
    private static Server instance;
    private final String TAG = "Server";
    private static ServerSocket serverSocket;
    private int port;
    private String ip;
    private List<EchoClient> clients;
    private int maxPlayer = 3;
    private int joinedPlayers = 0;
    private static boolean serverRunning = false;
    private static Logger logger = Logger.getLogger("global");


    private Server() {
        try {
            serverSocket = new ServerSocket(0);
            this.port = serverSocket.getLocalPort();
            this.ip = getLocalIpAddress();
            Log.d(TAG, "Server start");
            clients = new ArrayList<>();

            start();
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException at Server instantiation!" ,e);
        }

    }

    //Gibt den momentanen Server zurück oder wenn dieser nicht besteht wird ein neuer angelegt.
    public static synchronized Server getInstance() {
        if (Server.instance == null) {
            Server.instance = new Server();
        }
        return Server.instance;
    }

    @Override
    public void run() {
        setServerRunning(true);
        while (joinedPlayers < maxPlayer && !serverSocket.isClosed()) {
            try {
                Socket clientSocket = null;
                clientSocket = serverSocket.accept();

                if (clientSocket != null) {
                    EchoClient eClient = new EchoClient(clientSocket);
                    eClient.start();
                    Thread.sleep(2000);
                    clients.add(eClient);
                    MultiplayerLobbyActivity multiLobby = (MultiplayerLobbyActivity) ActivityUtils.getCurrentActivity();
                    while (eClient.getPlayername() == "") {
                        Thread.sleep(100);
                    }

                    multiLobby.updateNames(joinedPlayers + 1, eClient.getPlayername());
                    for (EchoClient c : clients) {
                        c.sendString(getPlayerName());
                        for (int i = 0; i < clients.size(); i++) {
                            c.sendString(clients.get(i).getPlayername());
                        }
                    }

                    joinedPlayers++;
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Exception at Server Thread run!" ,e);
            }

        }
        while (!serverSocket.isClosed()) {
            //Do something with the clients
        }
    }

    public static void shutdown() {
        try {
            instance = null;
            setServerRunning(false);
            serverSocket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException at Client instantiation!" ,e);
        }

    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getJoinedPlayers() {
        return joinedPlayers;
    }

    public void setJoinedPlayers(int joinedPlayers) {
        this.joinedPlayers = joinedPlayers;
    }

    public List getClients() {
        return clients;
    }


    public String getLocalIpAddress() {
        Enumeration<NetworkInterface> nis = null;
        try {
            nis = NetworkInterface.getNetworkInterfaces();

            NetworkInterface ni;
            while (nis.hasMoreElements()) {
                ni = nis.nextElement();
                if (!ni.isLoopback() && ni.isUp()) {
                    for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                        //filter for ipv4/ipv6
                        if (ia.getAddress().getAddress().length == 4) {
                            //4 for ipv4, 16 for ipv6
                            return ia.getAddress().getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            logger.log(Level.WARNING, "SocketException at Server getLocapIp!" ,e);
        }
        return null;
    }

    public EchoClient getClient(int number) {
        if (number < 0 || number > 3) {
            return null;
        }
        return clients.get(number);
    }

    public static boolean isServerRunning() {
        return serverRunning;
    }

    public static void setServerRunning(boolean serverRunning) {
        Server.serverRunning = serverRunning;
    }

    public String getPlayerName() {
        if (playerName == "") {
            playerName = "Player" + new Random().nextInt(100);
            return playerName;
        }
        return playerName;
    }


    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void sendStrings(String msg) {
        for (EchoClient c : clients) {
            c.sendString(msg);
        }
    }
}