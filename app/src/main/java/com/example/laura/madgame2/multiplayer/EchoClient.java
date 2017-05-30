package com.example.laura.madgame2.multiplayer;

import android.util.Log;

import com.example.laura.madgame2.multiplayer.update.Update;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Philipp on 28.04.17.
 */

public class EchoClient extends Thread {

    private final String TAG = "EchoClient";

    private String playername;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private Logger logger = Logger.getLogger("global");
    private boolean gameStarted = false;
    private ObjectOutputStream objectOut;
    private ObjectInputStream objectIn;
    private Update update;
    private boolean killThread = false;

    public EchoClient(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            objectOut = new ObjectOutputStream(socket.getOutputStream());
            objectIn = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException at EchoClient instantiation!", e);
        }
    }

    @Override
    public void run() {
        ServerNotificator serverNotificator = new ServerNotificator();
        while (!gameStarted && !killThread) {
            try {
                playername = in.readUTF();
                if (playername.equals("acceptStart")) {
                    gameStarted = true;
                    break;
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "IOException at EchoClient Thread run!", e);
            }
        }

        while (gameStarted && !killThread) {
            try {
                update = (Update) objectIn.readObject();
                Log.d(TAG, update.toString());
                serverNotificator.notifyServer();
            } catch (IOException e) {
                logger.log(Level.WARNING, "IOException occurred at Client Thread run!", e);
            } catch (ClassNotFoundException e) {
                logger.log(Level.WARNING, "ClassNotFoundException occurred at Client Thread run!", e);
            }
        }

    }

    /**
     * sendet einen String an den dazugehörigen Client
     *
     * @param msg die Nachricht als String
     */
    public void sendString(String msg) {
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException at EchoClient sendString!", e);
        }
    }

    public String getPlayername() {
        return playername;
    }

    public void setPlayername(String playername) {
        this.playername = playername;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getIn() {
        return in;
    }

    public void setIn(DataInputStream in) {
        this.in = in;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public void setOut(DataOutputStream out) {
        this.out = out;
    }


    /**
     * Schaltet den EchoClient ab und wirft ihm damit aus der Lobby
     */
    public void shutdown() {
        try {
            killThread = true;
            in.close();
            out.close();
            objectIn.close();
            objectOut.close();
            socket.close();
        } catch (IOException e) {
            logger.log(Level.INFO, "Killed Thread while running");
        }
    }

    /**
     * Sendet ein Update an den Client
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

    /**
     * Startet das Spiel
     */
    public void startGame() {
        sendString("start");
    }

    public class ServerNotificator extends Observable {


        public ServerNotificator() {
            this.addObserver(Server.getInstance());
        }

        /**
         * Wird ein Update erhalten, wird der Server davon in Kenntnis gesetzt und erhält dieses.
         */
        public void notifyServer() {
            setChanged();
            notifyObservers(update);
        }
    }

}
