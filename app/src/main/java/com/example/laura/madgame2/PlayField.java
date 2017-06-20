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
import com.example.laura.madgame2.gamestate.action.EndGameAction;
import com.example.laura.madgame2.gamestate.action.HighlightAction;
import com.example.laura.madgame2.gamestate.action.KickFigureAction;
import com.example.laura.madgame2.gamestate.action.MoveFigureAction;
import com.example.laura.madgame2.gamestate.action.NotificationAction;
import com.example.laura.madgame2.gamestate.action.WinningAction;
import com.example.laura.madgame2.highscore.ScoreEdit;
import com.example.laura.madgame2.multiplayer.Client;
import com.example.laura.madgame2.multiplayer.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayField extends AppCompatActivity {

    private static final int NUMBER_IDENTIFIER = 1;
    private static final int NUM_FIELDS = 40;
    private Controller controller;
    private List<View> fieldViews;
    private List<List<View>> finishFieldViews;
    private List<List<View>> figureViews;
    private List<List<ViewGroup.LayoutParams>> outFields;
    private TextView outPutText;
    private Queue<Action> actionQueue;
    private boolean asyncTaskRunning;
    private CountDownTimer highlightCDTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // activity init
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_field);

        actionQueue = new LinkedBlockingQueue<>();
        asyncTaskRunning = false;

        outPutText = (TextView) findViewById(R.id.PlayerTurn);
        outPutText.setText("Spieler 1 starte Spiel!");

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

            String[] playerNames = MultiplayerLobbyActivity.getPlayerNames();

            p1Name.setText(playerNames[0]);
            p2Name.setText(playerNames[1]);
            p3Name.setText(playerNames[2]);
            p4Name.setText(playerNames[3]);
        }

        // if not in multiplayer mode, init players
        if (!controller.isMP()) {
            controller.setPlayers(players, 0, 0);
        } else {
            controller.startGame(players);
        }

        if (!controller.init())
            Log.d("Debug", "Failed to initialize state controller");

        controller.setPlayField(this);
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
            Toast.makeText(getApplication(), "Spieler " + controller.getPlayerBefore().getPlayerNr() + " hat geschummelt und muss nächste Runde aussetzen!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplication(), "Spieler " + controller.getPlayerBefore().getPlayerNr() + " hat nicht geschummelt! Du musst nächste Runde aussetzen!",
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

        // check if the selected view represents a figure and find out which
        Pattern p = Pattern.compile("iv_player(\\d)_figure(\\d)");
        Matcher m = p.matcher(idOut);

        if (m.matches())
            handleUpdates(controller.chooseFigure(Integer.valueOf(m.group(1)), Integer.valueOf(m.group(2))));
    }

    /**
     * Returns the associated UI element (view) for the given id.
     * Note that this way of retrieving View items is slow compared to resource identifiers.
     *
     * @param id the resource identifier, as declared in the activities xml layout
     * @return the View object
     */
    private View getViewById(String id) {
        return findViewById(getResources().getIdentifier(id, "id", getPackageName()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NUMBER_IDENTIFIER && resultCode == RESULT_OK) {
            handleUpdates(controller.diceRollResult(data.getIntExtra("result", -1), data.getBooleanExtra("hasCheated", false)));
            if (data.getBooleanExtra("hasCheated", false)) {
                RollDiceActivity.setCheat(true);
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
     * Sets the message to call {@link #handleUpdates(List)} on dismiss to establish a queue of actions.
     *
     * @param title  the title of the message
     * @param msg    the message
     * @param button the text on the alert message button
     */
    public void alert(String title, String msg, String button) {
        final PlayField ref = this;

        // method chaining vom feinsten
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(button, null)
                .setCancelable(true)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        // make sure queued tasks are executed after this one finishes
                        synchronized (ref) {
                            asyncTaskRunning = false;
                        }
                        handleUpdates(null);
                    }
                }).create().show();
    }

    /**
     * Helper function to display status message.
     *
     * @param text the status message
     */
    public void putStatusText(String text) {
        outPutText.setText(text);
    }

    /**
     * Handles execution of batches of actions, as returned by Controller's event handlers.
     *
     * @see #handleUpdate(Action)
     */
    public synchronized void handleUpdates(List<Action> actions) {
        // handle new actions
        if (actions != null)
            for (Action action : actions)
                handleUpdate(action);

        // handle queued actions
        Action action;
        while (!asyncTaskRunning && (action = actionQueue.poll()) != null)
            handleUpdate(action);
    }

    /**
     * Handles execution of actions. Synchronous actions are executed immediately
     * while asynchronous are dispatched if there are no other active tasks, else queued for later execution.
     *
     * @see #handleUpdates(List)
     */
    private void handleUpdate(Action action) {
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

        } else if (action instanceof NotificationAction) {
            NotificationAction a = (NotificationAction) action;
            switch (a.type) {
                case TEXTFIELD:
                    putStatusText(a.msg);
                    break;
                case TOAST:
                    toast(a.msg);
                    break;
                case ALERT:
                    if (asyncTaskRunning) {
                        actionQueue.add(action);
                    } else {
                        asyncTaskRunning = true;
                        alert(a.title, a.msg, "Ok");
                    }
                    break;
                default:
                    break;
            }

        } else if (action instanceof WinningAction) {
            WinningAction a = (WinningAction) action;
            if (asyncTaskRunning) {
                actionQueue.add(action);
            } else {
                actionQueue.add(new EndGameAction());
                asyncTaskRunning = true;
                alert("Spiel beendet!", "Spieler " + a.name + " hat gewonnen!", "Ok");
            }

        } else if (action instanceof EndGameAction) {
            finish();
        }
    }

    public void updateField(final List<Action> result) {
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                handleUpdates(result);
            }
        });
    }

}
