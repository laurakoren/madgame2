package com.example.laura.madgame2.multiplayer;

import android.os.AsyncTask;

import com.example.laura.madgame2.multiplayer.update.Update;

/**
 * Created by Philipp on 16.05.17.
 */

public class AsyncServerTask extends AsyncTask<Update, Void, Void> {

    @Override
    protected Void doInBackground(Update... params) {
        Server.getInstance().sendBroadcastUpdate(params[0]);
        return null;
    }
}
