package com.example.laura.madgame2.multiplayer;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.laura.madgame2.MultiplayerLobbyActivity;
import com.example.laura.madgame2.utils.ActivityUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
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

    private final String TAG = "Client";

    private Socket clientSocket;
    private String playerName = "";
    private boolean connected;
    private DataInputStream in;
    private DataOutputStream out;
    private int port;
    private String ip;
    private static Client instance;
    private boolean gameStarted = false;

    public Client(String ip, int port) {
        try {
            this.ip = ip;
            this.port = port;
            clientSocket = new Socket(ip, port);
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
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
        MultiplayerLobbyActivity multiLobby = (MultiplayerLobbyActivity) ActivityUtils.getCurrentActivity();
        String[] playerNames = new String[4];
        String response = null;

        try {
            out.writeUTF(getPlayerName());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "playername send");

        int count = 0;
        while (!gameStarted) {
            try {
                Thread.sleep(500);
                playerNames[count] = (in.readUTF());
                count++;
                ((MultiplayerLobbyActivity) ActivityUtils.getCurrentActivity()).updateNames(playerNames);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
            playerName = "Player" + (int) (Math.random() * 100);
            return playerName;
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
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void waitForResponse(){
        try {
            Log.d(TAG, "waiting for response");
           String temp=  in.readUTF();
            String[] response = temp.split(";");
            if(response.equals(UpdateTyp.TOAST.toString())){
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}