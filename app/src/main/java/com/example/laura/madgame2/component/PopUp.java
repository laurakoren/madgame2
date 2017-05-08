package com.example.laura.madgame2.component;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.widget.EditText;

import com.example.laura.madgame2.gameLogic.Player;
import com.example.laura.madgame2.multiplayer.Client;
import com.example.laura.madgame2.utils.ActivityUtils;

/**
 * Created by Philipp on 05.05.17.
 */

public class PopUp {

    private String title;
    private String submitButtonText = "OK";
    private String cancelButtonText = "Cancel";
    private String input;
    private Intent intent = null;

    private   AlertDialog.Builder builder;

    public PopUp(String title, String submitButtonText, String cancelButtonText) {
        this.title = title;
        this.submitButtonText = submitButtonText;
        this.cancelButtonText = cancelButtonText;
    }

    public PopUp(String title, String submitButtonText, String cancelButtonText, Intent intent) {
        this.title = title;
        this.submitButtonText = submitButtonText;
        this.cancelButtonText = cancelButtonText;
        this.intent = intent;
    }

    //Bevor popUp() aufgerufen werden kann, muss ActivityUtils.setCurretnActivity() gsetzt werden!
    public void popUpNameInput(){
     builder = new AlertDialog.Builder(ActivityUtils.getCurrentActivity());
        builder.setTitle(title);

        final EditText txtInput = new EditText(ActivityUtils.getCurrentActivity());
        txtInput.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(txtInput);

        builder.setPositiveButton(submitButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                input = txtInput.getText().toString();
                Client.setPlayerName(input);
                if(intent != null){
                    ActivityUtils.getCurrentActivity().startActivity(intent);
                }
            }
        });
        builder.setNegativeButton(cancelButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


}
