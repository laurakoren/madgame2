package com.example.laura.madgame2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.laura.madgame2.multiplayer.Client;
import com.example.laura.madgame2.multiplayer.Server;
import com.example.laura.madgame2.multiplayer.update.UpdateRolledDice;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void sendUpdateHOST(View view){
        Server.getInstance().sendBroadcastUpdate(new UpdateRolledDice(5,5));
    }

    public void sendUpdateCLIENT(View view){
        Client.getInstance().sendUpdate(new UpdateRolledDice(6,6));
    }
}
