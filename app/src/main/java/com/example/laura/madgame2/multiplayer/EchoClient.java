package com.example.laura.madgame2.multiplayer;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
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

    public EchoClient(Socket socket) {
        this.socket = socket;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException at EchoClient instantiation!" ,e);
        }
    }

    @Override
    public void run() {
        Log.d(TAG, "Started");
        try {
            playername = in.readUTF();
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException at EchoClient Thread run!" ,e);
        }

        Log.d(TAG, "GOT: " + playername);
        while(true){

        }

    }

    public void sendString(String msg) {
        try {
            out.writeUTF(msg);
            out.flush();
        } catch (IOException e) {

            logger.log(Level.WARNING, "IOException at EchoClient sendString!" ,e);
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

    public void sendStringUpdate(UpdateTyp typ, String msg){
        try {
            out.writeUTF(typ.toString()+";"+msg);
            out.flush();
        } catch (IOException e) {
            logger.log(Level.WARNING, "IOException at EchoClient sendStringUpdate !" ,e);
        }
    }
}
