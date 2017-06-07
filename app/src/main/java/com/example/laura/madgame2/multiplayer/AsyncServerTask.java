package com.example.laura.madgame2.multiplayer;

import android.os.AsyncTask;

import com.example.laura.madgame2.multiplayer.update.Update;
import com.example.laura.madgame2.multiplayer.update.UpdateMyNumber;
import com.example.laura.madgame2.multiplayer.update.UpdatePlayersTurn;

/**
 * Created by Philipp on 16.05.17.
 */

public class AsyncServerTask extends AsyncTask<Update, Void, Void> {

    @Override
    protected Void doInBackground(Update... params) {
        if (params[0] instanceof UpdatePlayersTurn) {
            Server.getInstance().getClient(((UpdatePlayersTurn)params[0]) .getPlayerNr()-1).sendUpdate(params[0]);
            return null;
                    }

        if (params[0] instanceof UpdateMyNumber) {
            Server.getInstance().getClient(((UpdateMyNumber)params[0]) .getPlayerNr()-1).sendUpdate(params[0]);
            return null;
        }

        for (EchoClient ec : Server.getInstance().getClients()) {
            ec.sendUpdate(params[0]);

        }
        return null;
    }
}
