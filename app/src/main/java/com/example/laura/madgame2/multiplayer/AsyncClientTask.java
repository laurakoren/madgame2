package com.example.laura.madgame2.multiplayer;

import android.os.AsyncTask;

/**
 * Created by Philipp on 19.04.17.
 */

public class AsyncClientTask extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        Client client = new Client(params[0], Integer.parseInt(params[1]));
        client.setInstance(client);
        return null;
    }
}
