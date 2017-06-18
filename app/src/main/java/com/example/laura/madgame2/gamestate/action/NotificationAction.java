package com.example.laura.madgame2.gamestate.action;

public class NotificationAction implements Action {
    public final Type type;
    public final String title;
    public final String msg;

    public NotificationAction(Type type, String title, String msg) {
        this.type = type;
        this.title = title;
        this.msg = msg;
    }

    public enum Type {
        TEXTFIELD,
        TOAST,
        ALERT
    }
}
