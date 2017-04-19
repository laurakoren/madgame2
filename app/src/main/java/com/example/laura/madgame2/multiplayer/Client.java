package com.example.laura.madgame2.multiplayer;

import android.app.Activity;
import android.util.Log;

import com.example.laura.madgame2.MultiplayerLobbyActivity;
import com.example.laura.madgame2.utils.ActivityUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Philipp on 05.04.17.
 */

public class Client extends Thread {

    private final String TAG ="Client";

    private Socket clientSocket;
    private String playerName = "";
    private boolean connected;

    private BufferedReader bReader;
    private PrintWriter pWriter;
    private int port;
    private String ip;
    private static Client instance;

    public Client(String ip, int port) {
        try {
            this.ip = ip;
            this.port = port;
            clientSocket = new Socket(ip, port);
            pWriter = new PrintWriter(clientSocket.getOutputStream());
            bReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void setInstance(Client client) {
        instance = client;
    }

    public synchronized static Client getInstance() {
        return instance;
    }


    @Override
    public void run() {
       try {
           MultiplayerLobbyActivity multiLobby = (MultiplayerLobbyActivity) ActivityUtils.getCurrentActivity();
           Set<String> playerNames = new HashSet<>();
           while(true){
               String msg = "";
               while(msg == ""){
                   msg = bReader.readLine();
               }
               Log.d(TAG,msg);
            playerNames.add(msg);

               for(int i = 0; i < playerNames.size(); i++){
                   multiLobby.runThread(i, playerNames.toArray()[i].toString());
               }
           }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public String getPlayerName() {
        if (playerName == "") {
            return "Player" + (int) (Math.random() * 100);
        }
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isConnected() {
        return clientSocket.isConnected();
    }

    public void sendString(String msg) {
            pWriter.println(msg);
            pWriter.flush();

    }

    public BufferedReader getBufferedReader() {
        return bReader;
    }

    public PrintWriter getPrintWriter() {
        return pWriter;
    }


}