package com.example.laura.madgame2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laura.madgame2.diceroll.RollDiceActivity;
import com.example.laura.madgame2.gamelogic.Player;
import com.example.laura.madgame2.gamestate.Controller;
import com.example.laura.madgame2.gamestate.action.Action;
import com.example.laura.madgame2.gamestate.action.HighlightAction;
import com.example.laura.madgame2.gamestate.action.KickFigureAction;
import com.example.laura.madgame2.gamestate.action.MoveFigureAction;
import com.example.laura.madgame2.gamestate.action.WinningAction;
import com.example.laura.madgame2.highscore.ScoreEdit;
import com.example.laura.madgame2.multiplayer.Client;
import com.example.laura.madgame2.multiplayer.Server;
import com.example.laura.madgame2.multiplayer.update.UpdateDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayField extends AppCompatActivity {

    private static final int NUMBER_IDENTIFIER = 1;

    private Controller controller;

    private List<View> fieldViews;
    private List<List<View>> finishFieldViews;
    private List<List<View>> figureViews;
    private List<List<ViewGroup.LayoutParams>> outFields;
    private TextView outPutText;

    private CountDownTimer highlightCDTimer;

    private static final int NUM_FIELDS = 40;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // activity inti
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_field);

        outPutText = (TextView) findViewById(R.id.PlayerTurn);
        outPutText.setText("Spieler 0 starte Spiel!");

        // create players
        // TODO create players in multiplayer lobby and pass them to playfield
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++)
            players.add(new Player(i));

        // save fieldViews into list
        fieldViews = new ArrayList<>();
        for (int i = 0; i < NUM_FIELDS; i++)
            fieldViews.add(getViewById("iv_field" + i));

        // save finishFieldViews, figureViews and outField coords into list
        finishFieldViews = new ArrayList<>();
        List<View> tmpFinViews;
        figureViews = new ArrayList<>();
        List<View> tmpFigViews;
        outFields = new ArrayList<>();
        List<ViewGroup.LayoutParams> tmpLayoutParams;

        View tmpView;
        for (int player = 0; player < 4; player++) {
            tmpFinViews = new ArrayList<>();
            tmpFigViews = new ArrayList<>();
            tmpLayoutParams = new ArrayList<>();

            for (int count = 0; count < 4; count++) {
                // add finishFieldViews to array
                tmpFinViews.add(getViewById("iv_player" + player + "_finish" + count));

                // add player views and out field coords to arrays
                tmpView = getViewById("iv_player" + player + "_figure" + count);
                tmpFigViews.add(tmpView);
                tmpLayoutParams.add(tmpView.getLayoutParams());
            }

            finishFieldViews.add(tmpFinViews);
            figureViews.add(tmpFigViews);
            outFields.add(tmpLayoutParams);
        }

        // get controller
        controller = Controller.getInstance();

        if (Server.isServerRunning() || Client.getInstance() != null) {
            //Display player names on playfield
            TextView p1Name = (TextView) findViewById(R.id.playerOneName);
            TextView p2Name = (TextView) findViewById(R.id.playerTwoName);
            TextView p3Name = (TextView) findViewById(R.id.playerThreeName);
            TextView p4Name = (TextView) findViewById(R.id.playerFourName);
            p1Name.setText(MultiplayerLobbyActivity.playerNames[0].getText().toString());
            p2Name.setText(MultiplayerLobbyActivity.playerNames[1].getText().toString());
            p3Name.setText(MultiplayerLobbyActivity.playerNames[2].getText().toString());
            p4Name.setText(MultiplayerLobbyActivity.playerNames[3].getText().toString());
        }

        // if not in multiplayer mode, init players
        if (!controller.isMP()) {
            controller.setPlayers(players, 0, 0);
        } else{
            controller.startGame(players);
        }

        if (!controller.init())
            Log.d("Debug", "Failed to initialize state controller");

        controller.setOutputtext(outPutText);
    }

    public void diceRoll(View view) {
        if (controller.rollDice()) {
            onPause();
            Intent intent = new Intent(this, RollDiceActivity.class);
            startActivityForResult(intent, NUMBER_IDENTIFIER);
        }
    }

    public void hasCheated(View view) {
        /*
        // TODO in state pattern auslagern

        //hat der vorherige Spieler gecheatet?
        boolean help = controller.getPlayerBefore().getCheater();

        if (help) {
            ScoreEdit.updateScore("cheaterCaught");
            Toast.makeText(getApplication(), "Spieler " + controller.getPlayerBefore().getPlayerNr() + " hat geschummelt und wird bestraft!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication(), "Spieler " + controller.getPlayerBefore().getPlayerNr() + " hat nicht geschummelt! Du wirst bestraft!",
                    Toast.LENGTH_SHORT).show();
        }
        */
        controller.catchCheater();
    }

    /**
     * Zeigt das Feld an, zu dem man mit dem Würfelergebnis fahren könnte
     */
    public void click(View viewIn) {

        String idOut = viewIn.getResources().getResourceEntryName(viewIn.getId());

        Pattern p = Pattern.compile("iv_player(\\d)_figure(\\d)");
        Matcher m = p.matcher(idOut);

        if (m.matches()) {
            List<Action> actions = controller.chooseFigure(Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2)));

            handleUpdates(actions);


            //TODO remove if not needed
            /*
            for (Action a : actions) {
                if (a instanceof UpdatePlayerFigure) {
                    int figureNr = ((UpdatePlayerFigure) a).getFigureNr();
                } else if (a instanceof UpdateDiceRoll) {
                    int diceResult = ((UpdateDiceRoll) a).getDiceRollResult();
                } else if (a instanceof HighlightAction) {
                    int field = ((HighlightAction) a).fieldNr;
                    int playerNr = Integer.valueOf(m.group(1));

                    if (field < 0) {
                        field = field + 100;

                        final View img = getViewById("iv_player" + playerNr + "_finish" + (field));       //"@+id/iv_player2_finish2"
                        img.setBackgroundResource(R.drawable.fig_empty);

                        new CountDownTimer(2000, 1000) {
                            public void onFinish() {
                                img.setBackgroundResource(R.drawable.clear_circle);
                            }

                            public void onTick(long millisUntilFinished) {
                                // unused
                            }
                        }.start();
                    } else if (field == 110) {
                    } else {
                        final View img = getViewById("iv_field" + field);
                        img.setBackgroundResource(R.drawable.fig_empty);

                        new CountDownTimer(2000, 1000) {
                            public void onFinish() {
                                img.setBackgroundResource(R.drawable.clear_circle);
                            }

                            public void onTick(long millisUntilFinished) {
                                // unused
                            }
                        }.start();
                    }
                }
            }*/
        }
    }

    /**
     * Returns the associated UI element (view) for the given id.
     *
     * @param id the id name declared in the activities xml layout
     * @return the View object
     */
    private View getViewById(String id) {
        return findViewById(getResources().getIdentifier(id, "id", getPackageName()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NUMBER_IDENTIFIER && resultCode == RESULT_OK) {
            controller.diceRollResult(data.getIntExtra("result", -1), data.getBooleanExtra("hasCheated", false));
            if (data.getBooleanExtra("hasCheated", false)) {
                ScoreEdit.updateScore("gamesCheated");
            }
            ScoreEdit.updateScore("amountDiceRolls");
        }
    }

    public void highlightField(int playerNr, int fieldNr, boolean isFinishField) {
        resetHighlightedField();

        final View field;
        if (isFinishField)
            field = finishFieldViews.get(playerNr).get(fieldNr);
        else
            field = fieldViews.get(fieldNr);

        field.setBackgroundResource(R.drawable.fig_empty);

        highlightCDTimer = new CountDownTimer(2000, 1000) {
            public void onFinish() {
                field.setBackgroundResource(R.drawable.clear_circle);
            }

            public void onTick(long millisUntilFinished) {
                // unused
            }
        }.start();
    }

    private void resetHighlightedField() {
        if (highlightCDTimer != null) {
            highlightCDTimer.cancel();
            highlightCDTimer.onFinish();
        }
    }

    /**
     * Moves a figure to a given field by moving its ImageView to the field's ImageView.
     *
     * @param playerNr the player the figure belongs to
     * @param figureNr the number of the figure
     * @param fieldNr  the number of the field to move to
     */
    private void moveFigure(int playerNr, int figureNr, int fieldNr) {
        moveTo(figureViews.get(playerNr).get(figureNr), fieldViews.get(fieldNr));
    }

    private void moveFigureToOutField(int playerNr, int figureNr) {
        figureViews.get(playerNr).get(figureNr).setLayoutParams(outFields.get(playerNr).get(figureNr));
    }

    private void moveFigureToFinishField(int playerNr, int figureNr, int finishFieldNr) {
        moveTo(figureViews.get(playerNr).get(figureNr), finishFieldViews.get(playerNr).get(finishFieldNr));
    }

    /**
     * Helper function to move a view to the location of another view
     *
     * @param target      to view to move
     * @param destination the view to move it to
     */
    private void moveTo(View target, View destination) {
        if (target != null && destination != null)
            target.setLayoutParams(destination.getLayoutParams());
    }

    /**
     * Helper function to display a toast message.
     *
     * @param msg the message to display
     */
    public void toast(String msg) {
        Toast.makeText(this.getApplication(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Helper function to display an alert message with an Ok button.
     *
     * @param title the title of the message
     * @param msg   the message
     */
    public void alert(String title, String msg) {
        // method chaining vom feinsten
        new AlertDialog.Builder(this).setTitle(title).setMessage(msg).setPositiveButton("Ok", null).create().show();
    }

    /**
     * Helper function to display status message.
     *
     * @param text the status message
     */
    public void postStatus(String text) {
        outPutText.setText(text);
    }


    public void handleUpdates(List<Action> actions) {
        if (actions != null) {
            for (Action action : actions) {
                if (action instanceof MoveFigureAction) {
                    resetHighlightedField();
                    MoveFigureAction a = (MoveFigureAction) action;
                    if (a.moveToFinishField)
                        moveFigureToFinishField(a.playerNr, a.figureNr, a.fieldNr);
                    else
                        moveFigure(a.playerNr, a.figureNr, a.fieldNr);

                } else if (action instanceof KickFigureAction) {
                    KickFigureAction a = (KickFigureAction) action;
                    moveFigureToOutField(a.playerNr, a.figureNr);

                } else if (action instanceof HighlightAction) {
                    HighlightAction a = (HighlightAction) action;
                    highlightField(a.playerNr, a.fieldNr, a.isFinishField);

                } else if (action instanceof WinningAction) {
                    WinningAction a = (WinningAction) action;

                    new AlertDialog.Builder(this)
                            .setTitle("Spiel beendet!")
                            .setMessage("Spieler " + a.name + " hat gewonnen!")
                            .setPositiveButton("Ok", null)
                            .setCancelable(true)
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            finish();
                        }
                    }).create().show();
                }
            }
        }
    }
}
