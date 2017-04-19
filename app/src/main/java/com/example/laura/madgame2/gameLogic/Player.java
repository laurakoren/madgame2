package com.example.laura.madgame2.gameLogic;

/**
 * Created by Alex on 18.04.2017.
 */


public class Player {

    int playernr;       //Spielernummern 1-4

    public Player(int playernr){
        this.playernr=playernr;

        if(playernr==1){
            Figure fig11 = new Figure();
            Figure fig12 = new Figure();
            Figure fig13 = new Figure();
            Figure fig14 = new Figure();

        }else if(playernr==2){
            Figure fig21 = new Figure();
            Figure fig22 = new Figure();
            Figure fig23 = new Figure();
            Figure fig24 = new Figure();

        }else if(playernr==3){
            Figure fig31 = new Figure();
            Figure fig32 = new Figure();
            Figure fig33 = new Figure();
            Figure fig34 = new Figure();

        }else if(playernr==2){
            Figure fig41 = new Figure();
            Figure fig42 = new Figure();
            Figure fig43 = new Figure();
            Figure fig44 = new Figure();
        }

    }


    public int getPlayernr() {
        return playernr;
    }

    public void setPlayernr(int playernr) {
        this.playernr = playernr;
    }



}

