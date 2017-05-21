package com.example.laura.madgame2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laura.madgame2.multiplayer.AsyncClientTask;
import com.example.laura.madgame2.multiplayer.Client;
import com.example.laura.madgame2.multiplayer.Server;
import com.example.laura.madgame2.utils.ActivityUtils;
import com.example.laura.madgame2.utils.NetworkUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiplayerActivity extends AppCompatActivity {

    private static final String TAG ="MultiplayerActivity";
    private Intent intent;
    private Client client;
    private Logger logger = Logger.getLogger("global");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
        ActivityUtils.setCurrentActivity(this);
    }

    public void hostGame(View view) {
        Server server = Server.getInstance();
        intent = new Intent(this, MultiplayerLobbyActivity.class);
        startActivity(intent);
    }

    public void joinGame(View view) {
        EditText inputIp = (EditText) findViewById(R.id.inputIp);
        String ipText = inputIp.getText().toString();
        if (NetworkUtils.checkIp(ipText)) {
            String[] ipPort = ipText.split(":");
            new AsyncClientTask().execute(ipPort[0], ipPort[1]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                intent = new Intent(this, MainActivity.class);
                logger.log(Level.WARNING, "Interrupted at MultiplayerActivity sleep!" ,e);
                startActivity(intent);
                Thread.currentThread().interrupt();
            }
            client = client.getInstance();
            client.start();
            if (client.isConnected()) {
                intent = new Intent(this, MultiplayerLobbyActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Can't connect", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Invalid. Use IP:Port", Toast.LENGTH_SHORT).show();
        }
    }

}
