package com.example.laura.madgame2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.laura.madgame2.multiplayer.Role;
import com.example.laura.madgame2.multiplayer.Server;
import com.example.laura.madgame2.utils.ActivityUtils;

public class MultiplayerLobbyActivity extends AppCompatActivity {

    private Intent intent;
    private Server server;
    private TextView hostIp;
    private Role role;
    public TextView[] playerNameTextViews;
    private Button[] kickPlayer;
    private static String[] playerNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_lobby);

        playerNameTextViews = new TextView[4];
        playerNameTextViews[0] = (TextView) findViewById(R.id.txtPlayerOne);
        playerNameTextViews[1] = (TextView) findViewById(R.id.txtPlayerTwo);
        playerNameTextViews[2] = (TextView) findViewById(R.id.txtPlayerThree);
        playerNameTextViews[3] = (TextView) findViewById(R.id.txtPlayerFour);

        setPlayerName(playerNameTextViews);

        kickPlayer = new Button[4];
        kickPlayer[0] = (Button) findViewById(R.id.btnKickPlayerTwo);
        kickPlayer[1] = (Button) findViewById(R.id.btnKickPlayerThree);
        kickPlayer[2] = (Button) findViewById(R.id.btnKickPlayerFour);

        ActivityUtils.setCurrentActivity(this);
        if (Server.isServerRunning()) {
            server = Server.getInstance();
            role = Role.HOST;
            hostIp = (TextView) findViewById(R.id.txtIp);
            hostIp.setText(server.getIp() + ":" + server.getPort());
            playerNameTextViews[0].setText(server.getPlayerName());
        } else {
            role = Role.Client;
            findViewById(R.id.btnStart).setVisibility(View.INVISIBLE);
        }

    }

    public void doCancel(View view) {
        intent = new Intent(this, MultiplayerActivity.class);
        if (role == Role.HOST) {
            Server.shutdown();
        }
        startActivity(intent);
    }

    public void updateNames(final String[] names) {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < names.length; i++) {
                    playerNameTextViews[i].setText(names[i]);
                }
                setPlayerName(playerNameTextViews);
            }
        });
    }

    public void updateNames(final int pos, final String name, final boolean kicked) {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                playerNameTextViews[pos].setText(name);
                if (kicked) {
                    kickPlayer[pos].setVisibility(View.INVISIBLE);
                } else if (pos != 0) {
                    kickPlayer[pos - 1].setVisibility(View.VISIBLE);
                }
                setPlayerName(playerNameTextViews);
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

    public static void setPlayerName(TextView[] names) {
        playerNames = new String[names.length];
        for (int i = 0; i < playerNames.length; i++) {
            playerNames[i] = names[i].getText().toString();
        }
    }

    public static String[] getPlayerNames() {
        return playerNames;
    }

}
