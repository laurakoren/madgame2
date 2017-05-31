package com.example.laura.madgame2.multiplayer;

import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.laura.madgame2.MultiplayerActivity;
import com.example.laura.madgame2.MultiplayerLobbyActivity;
import com.example.laura.madgame2.PlayField;
//import com.example.laura.madgame2.TestActivity;
import com.example.laura.madgame2.gamestate.Controller;
import com.example.laura.madgame2.multiplayer.update.Update;
import com.example.laura.madgame2.utils.ActivityUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Philipp on 05.04.17.
 */

public class Client extends Thread {

    private static final String TAG = "Client";

    private static String playerName = "";
    private Socket clientSocket;
    private DataInputStream in;
    private DataOutputStream out;
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;
    private int port;
    private String ip;
    private static Client instance;
    private boolean gameStarted = false;
    private Update update;
    private Logger logger = Logger.getLogger("global");
    private boolean killThread = false;

    public Client(String ip, int port) {
        try {
            this.ip = ip;
            this.port = port;
            clientSocket = new Socket(ip, port);
            in = new DataInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());
            objectOut = new ObjectOutputStream(clientSocket.getOutputStream());
            objectIn = new ObjectInputStream(clientSocket.getInputStream());
            instance = this;
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException at Client instantiation!", e);
        }
    }

    public Client(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    public static synchronized void setInstance(Client client) {
        instance = client;
    }

    /**
     * Gibt die laufende Instance des Clients zurück oder erstellt eine neue, falls es keine Instance gibt.
     * DER CLIENT IST AUSSCHLIESSLICH ÜBER DIESE METHODE AUFZURUFEN!
     * Siehe Singleton.
     */
    public static synchronized Client getInstance() {
        return instance;
    }


    @Override
    public void run() {
        String[] playerNames = new String[4];

        try {
            out.writeUTF(getPlayerName());
            out.flush();
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException at Client Thread run!", e);
        }


        int count = 0;
        String input;
        while (!gameStarted && !killThread) {
            try {
                Thread.sleep(500);
                input = in.readUTF();
                if (input.equals("start")) {
                    gameStarted = true;
                    ActivityUtils.getCurrentActivity().startActivity(new Intent(ActivityUtils.getCurrentActivity(), PlayField.class));
                    sendString("acceptStart");
                    break;
                }
                playerNames[count] = input;
                count++;
                ((MultiplayerLobbyActivity) ActivityUtils.getCurrentActivity()).updateNames(playerNames);
            } catch (IOException e) {
                logger.log(Level.WARNING, "IOException at Client Thread run!", e);
                killMe();
            } catch (InterruptedException e) {
                logger.log(Level.WARNING, "Interrupted at Client Thread run!", e);
                Thread.currentThread().interrupt();
            }

        }

        while (gameStarted && !killThread) {
            try {
                update = (Update) objectIn.readObject();

                Controller.getInstance().receiveUpdate(update);  //
                update=null;

            } catch (IOException e) {
                logger.log(Level.WARNING, "IOException occurred at Client Thread run!", e);
                killMe();
            } catch (ClassNotFoundException e) {
                logger.log(Level.WARNING, "ClassNotFoundException occurred at Client Thread run!", e);
            }
        }


    }

    /**
     * Schaltet den Client ab und löscht die momentane Instance.
     */
    private void killMe() {
        killThread = true;
        setInstance(null);
        ActivityUtils.getCurrentActivity().startActivity(new Intent(ActivityUtils.getCurrentActivity(), MultiplayerActivity.class));
        Looper.prepare();
        Toast.makeText(ActivityUtils.getCurrentActivity(), "You have been kicked", Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }


    public boolean isConnected() {
        return clientSocket.isConnected();
    }

    /**
     * Sendet einen String an den EchoClient
     *
     * @param msg Die Nachricht als String
     */
    public void sendString(String msg) {
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException at Client sendingString!", e);
        }
    }

    public static String getPlayerName() {
        if (playerName == "") {
            playerName = "Player" + new Random().nextInt(100);
            return playerName;
        }
        return playerName;
    }

    public static void setPlayerName(String playerName) {
        Client.playerName = playerName;
    }

    /**
     * Sendet ein Update an den EchoClient.
     *
     * @param update Das Update, doh.
     */
    public void sendUpdate(Update update) {
        try {
            objectOut.writeObject(update);
            objectOut.flush();
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException occurred at Client Thread run!", e);
        }
    }

}