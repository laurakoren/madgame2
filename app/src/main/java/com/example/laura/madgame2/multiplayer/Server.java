package com.example.laura.madgame2.multiplayer;


import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.laura.madgame2.MultiplayerLobbyActivity;
import com.example.laura.madgame2.PlayField;
import com.example.laura.madgame2.TestActivity;
import com.example.laura.madgame2.gamestate.Controller;
import com.example.laura.madgame2.multiplayer.update.Update;
import com.example.laura.madgame2.multiplayer.update.UpdateChooseFigure;
import com.example.laura.madgame2.multiplayer.update.UpdatePlayersTurn;
import com.example.laura.madgame2.multiplayer.update.UpdateRolledDice;
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
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Philipp on 03.04.17.
 */

public class Server extends Thread implements Observer {

    private String playerName = "";
    private static Server instance;
    private final String TAG = "Server";
    private static ServerSocket serverSocket;
    private int port;
    private String ip;
    private List<EchoClient> clients;
    private static boolean serverRunning = false;
    private static Logger logger = Logger.getLogger("global");
    private boolean gameStarted = false;


    private Server() {
        try {
            serverSocket = new ServerSocket(0);
            this.port = serverSocket.getLocalPort();
            this.ip = getLocalIpAddress();
            clients = new ArrayList<>();

            start();
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException at Server instantiation!", e);
        }

    }

    /**
     * Gibt die laufende Instance des Servers zurück oder erstellt eine neue, falls es keine Instance gibt.
     * DER SERVER IST AUSSCHLIESSLICH ÜBER DIESE METHODE AUFZURUFEN!
     * Siehe Singleton.
     */
    public static synchronized Server getInstance() {
        if (Server.instance == null) {
            Server.instance = new Server();
        }
        return Server.instance;
    }

    @Override
    public void run() {
        setServerRunning(true);
        int count;
        while (!serverSocket.isClosed() && !gameStarted && clients.size() <= 3) {
            try {
                Socket clientSocket = null;
                clientSocket = serverSocket.accept();

                if (clientSocket != null) {
                    EchoClient eClient = new EchoClient(clientSocket);
                    eClient.start();
                    Thread.sleep(2000);
                    count = 0;
                    while (clients.size() > 0 && clients == null) {
                        count++;
                    }
                    clients.add(count, eClient);

                    MultiplayerLobbyActivity multiLobby = (MultiplayerLobbyActivity) ActivityUtils.getCurrentActivity();
                    while (eClient.getPlayername() == "") {
                        Thread.sleep(100);
                    }

                    multiLobby.updateNames(count + 1, eClient.getPlayername(), false);
                    for (EchoClient c : clients) {
                        c.sendString(getPlayerName());
                        for (int i = 0; i < clients.size(); i++) {
                            c.sendString(clients.get(i).getPlayername());
                        }
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING, "Exception at Server Thread run!", e);
            }

        }
        while (!serverSocket.isClosed() && gameStarted) {
            //Do something with the clients
        }
    }

    /**
     * Schließt den Server und löscht die Instance.
     */
    public static void shutdown() {
        try {
            instance = null;
            setServerRunning(false);
            serverSocket.close();
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException at Client instantiation!", e);
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

    public List<EchoClient> getClients() {
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
            logger.log(Level.WARNING, "SocketException at Server getLocapIp!", e);
        }
        return null;
    }

    /**
     * Gibt den EchoClient mit der entsprechnenden ID zurück.
     *
     * @param number Der Index des EchoClients
     * @return den EchoClient mit dem index number
     */
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

    /**
     * Sendet einen String an alle Clients.
     *
     * @param msg Die Nachricht als String
     */
    public void sendStrings(String msg) {
        for (EchoClient c : clients) {
            c.sendString(msg);
        }
    }

    /**
     * Sendet ein Update an alle Clients.
     *
     * @param update
     */
    public void sendBroadcastUpdate(Update update) {
        new AsyncServerTask().execute(update);
    }

    /**
     * Startet das Spiel
     */
    public void startGame() {
        new StartGame().execute();
        ActivityUtils.getCurrentActivity().startActivity(new Intent(ActivityUtils.getCurrentActivity(), PlayField.class));
    }

    /**
     * Kickt den Spieler mit dem Index id.
     *
     * @param id der Index des Spielers
     */
    public void kickPlayer(int id) {
        clients.get(id - 1).shutdown();
        clients.remove(id - 1);
        ((MultiplayerLobbyActivity) ActivityUtils.getCurrentActivity()).updateNames(id, "", true);
    }

    /**
     * Sollte der EchoClient ein Update erhalten, landet es hier.
     *
     * @param o
     * @param update Das empfangene Update
     */
    @Override
    public void update(Observable o, Object update) {

        Update up =(Update) update;

        if(((Update) update).getPlayerNr()==0){
            Controller.getInstance().receiveUpdate(up);

        }else{
            sendBroadcastUpdate(up);
        }


    }


    class StartGame extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            gameStarted = true;
            for (EchoClient ec : clients) {
                ec.startGame();
            }
            return null;
        }
    }
}