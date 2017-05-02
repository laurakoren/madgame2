package com.example.laura.madgame2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.laura.madgame2.multiplayer.Role;
import com.example.laura.madgame2.multiplayer.Server;
import com.example.laura.madgame2.utils.ActivityUtils;

public class MultiplayerLobbyActivity extends AppCompatActivity {

    private final String TAG = "MulitplayerLobby";
    private Intent intent;
    private Server server;
    private TextView hostIp;
    private Role role;
    public TextView[] playerNames;
    Activity m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActivityUtils.setCurrentActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby);
        m = this;
        playerNames = new TextView[4];
        playerNames[0] = (TextView) findViewById(R.id.txtPlayerOne);
        playerNames[1] = (TextView) findViewById(R.id.txtPlayerTwo);
        playerNames[2] = (TextView) findViewById(R.id.txtPlayerThree);
        playerNames[3] = (TextView) findViewById(R.id.txtPlayerFour);
        if (Server.isServerRunning()) {
            server = Server.getInstance();
            role = Role.Host;
            hostIp = (TextView) findViewById(R.id.txtIp);
            hostIp.setText(server.getIp() + ":" + server.getPort());
            playerNames[0].setText(server.getPlayerName());
        } else {
            role = Role.Client;
        }

    }

    public void doCancel(View view) {
        intent = new Intent(this, MultiplayerActivity.class);
        if (role == Role.Host) {
            server.shutdown();
        }
        startActivity(intent);
    }

    public void updateNames(final String[] names) {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                for(int i =0; i<names.length; i++) {
                    playerNames[i].setText(names[i]);
                }
            }
        });
    }

    public void updateNames(final int pos, final String name) {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                playerNames[pos].setText(name);
            }
        });


    }


}
