package de.lars_kadel.thc;

import java.util.ArrayList;

/**
 * Created by lars on 20.01.18.
 */

class Player {

    public boolean isReady() {
        return ready;
    }

    public boolean isLeader() {
        return isLeader;
    }

    public String getName() {
        return name;
    }

    public enum Role{
        traitor,
        innocent,
        detective,
    }

    private int id;
    private String name;
    private Role role;
    private boolean isLeader;
    private boolean ready;
    private ArrayList<String> detectives;
    private ArrayList<String> traitors;


    public Player(int id, String name, boolean isLeader, boolean ready){
        this.id = id;
        this.name = name;
        this.isLeader = isLeader;
        this.ready = ready;
    }

    public void setReady(boolean r){
        ready = r;
    }
}
