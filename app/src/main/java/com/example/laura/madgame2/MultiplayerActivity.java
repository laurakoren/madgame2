package com.example.laura.madgame2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.laura.madgame2.multiplayer.AsyncClientTask;
import com.example.laura.madgame2.multiplayer.Client;
import com.example.laura.madgame2.multiplayer.Server;
import com.example.laura.madgame2.utils.NetworkUtils;

public class MultiplayerActivity extends AppCompatActivity {

    private Server server;
    private Intent intent;
    private Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer);
    }

    public void hostGame(View view) {
        server = Server.getInstance();
        intent = new Intent(this, MultiplayerLobbyActivity.class);
        startActivity(intent);
    }

    public void joinGame(View view) {
        EditText inputIp = (EditText) findViewById(R.id.inputIp);
        String ipText = inputIp.getText().toString();
        if (NetworkUtils.checkIp(ipText)) {
            String ipPort[] = ipText.split(":");
            new AsyncClientTask().execute(ipPort[0], ipPort[1]);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            client = client.getInstance();
            client.start();
            if (client.isConnected()) {
                client.sendString(client.getPlayerName());
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
