package com.example.laura.madgame2;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.laura.madgame2.multiplayer.AsyncServerTask;
import com.example.laura.madgame2.multiplayer.Client;
import com.example.laura.madgame2.multiplayer.Role;
import com.example.laura.madgame2.multiplayer.Server;
import com.example.laura.madgame2.multiplayer.Update;
import com.example.laura.madgame2.utils.ActivityUtils;

public class MultiplayerLobbyActivity extends AppCompatActivity {

    private static final String TAG = "MulitplayerLobby";
    private Intent intent;
    private Server server;
    private TextView hostIp;
    private Role role;
    public TextView[] playerNames;
    private Button[] kickPlayer;
    Activity m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby);
        m = this;
        playerNames = new TextView[4];
        playerNames[0] = (TextView) findViewById(R.id.txtPlayerOne);
        playerNames[1] = (TextView) findViewById(R.id.txtPlayerTwo);
        playerNames[2] = (TextView) findViewById(R.id.txtPlayerThree);
        playerNames[3] = (TextView) findViewById(R.id.txtPlayerFour);

        kickPlayer = new Button[4];
        kickPlayer[0] = (Button)findViewById(R.id.btnKickPlayerTwo);
        kickPlayer[1] = (Button)findViewById(R.id.btnKickPlayerThree);
        kickPlayer[2] = (Button)findViewById(R.id.btnKickPlayerFour);

        ActivityUtils.setCurrentActivity(this);
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
                for (int i = 0; i < names.length; i++) {
                    playerNames[i].setText(names[i]);
                }
            }
        });
    }

    public void updateNames(final int pos, final String name, final boolean kicked) {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                playerNames[pos].setText(name);
                if(kicked) {
                    kickPlayer[pos].setVisibility(View.INVISIBLE);
                } else if(pos != 0){
                    kickPlayer[pos-1].setVisibility(View.VISIBLE);
                }

            }
        });
    }

    public void startGame(View view) {
        Server.getInstance().startGame();
    }

    public void kickPlayer(View view) {
        switch (view.getId()) {
            case R.id.btnKickPlayerTwo:
                Server.getInstance().kickPlayer(1);
                break;
            case R.id.btnKickPlayerThree:
                Server.getInstance().kickPlayer(2);
                break;
            case R.id.btnKickPlayerFour:
                Server.getInstance().kickPlayer(3);
                break;
            default:
                break;
        }
    }


}
