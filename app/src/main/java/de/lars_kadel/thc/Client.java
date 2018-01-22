package de.lars_kadel.thc;


import android.os.AsyncTask;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class Client extends AsyncTask<String, Player, GameInfo>{

    private CheckBox readyBox;
    private boolean isReady;
    private Socket socket;
    private ArrayList<Player> playerList;
    private GameInfo gameInfo;
    private boolean done;
    private Lobby lobby;
    private String name;


    public Client(CheckBox checkBox, Lobby lobby){
        this.lobby = lobby;
        readyBox = checkBox;
        socket = new Socket();
        playerList = new ArrayList<>();
        lobby.updateListView(playerList);
        name = lobby.getName();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        gameInfo = null;
        playerList.clear();
        done = false;
        try{
            socket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        if(socket.isClosed()){
            socket = new Socket();
        }
    }

    @Override
    protected GameInfo doInBackground(String... strings) {
        try {
            socket.setSoTimeout(100);
            socket.connect(new InetSocketAddress(strings[0], 2307));
            socket.getOutputStream().write(strings[1].getBytes());
        } catch (IOException e) {
            cancel(false);
            e.printStackTrace();
        }
        while(!done){
            updateReady();
            checkStart();
            listenToServer();
            if(isCancelled()){
                return null;
            }
        }
        return gameInfo;
    }

    private void updateReady(){
        if(isReady != readyBox.isChecked()){
            try {
                socket.getOutputStream().write(((readyBox.isChecked()) ? "ready": "notready").getBytes());
                isReady = readyBox.isChecked();
            } catch (IOException e) {
                e.printStackTrace();
                cancel(false);
            }
        }
    }

    private void checkStart(){
        if(lobby.start){
            try{
                socket.getOutputStream().write("start;".getBytes());
            }catch(IOException e){
                e.printStackTrace();
                cancel(false);
            }
        }
    }


    private void listenToServer(){
        try{
            if(socket.getInputStream().available()>0){
                byte[] tmp = new byte[socket.getInputStream().available()];
                socket.getInputStream().read(tmp);
                String msg = new String(tmp);
                handleMessage(msg);
            }
        }catch(IOException e){
            e.printStackTrace();
            cancel(false);
        }
    }

    private void handleMessage(String msg){
        String[] messages = msg.split(";");
        for(String s : messages){
            if(s.startsWith("players")){
                extractPlayers(s);
            }else if(s.startsWith("roundstart")){
                extractGameInfo(s);
            }else if(s.startsWith("leader")){
              //  lobby.setLeader(true);
            }else if(s.startsWith("noleader")){
              //  lobby.setLeader(false);
            }
        }
    }

    private void extractPlayers(String s) {
        s = s.substring(s.indexOf(':')+1);
        playerList.clear();
        for(String playerString : s.split(Pattern.quote("|"))){
            String[] v = playerString.split(",");
            playerList.add(
                    new Player(
                        Integer.parseInt(v[0]),
                        v[1],
                        (v[2].equals("1")),
                        (v[3].equals("1"))));
        }
        Player[] p = new Player[playerList.size()];
        p = playerList.toArray(p);
        publishProgress(p);
    }

    private void extractGameInfo(String s) {
        s = s.substring(s.indexOf(':')+1);
        if(s.endsWith("||")){
            s = s.substring(0,s.length()-2)+"| | ";
        }else if(s.endsWith("|")){
            s += " ";
        }
        Log.w("client", "msg: "+s+"!");
        String[] substrings = s.split(Pattern.quote("|"));

        String[] settings = substrings[0].split(",");
        String role = substrings[1];
        String[] detectives = substrings[2].split(",");
        String[] traitors = substrings[3].split(",");

        gameInfo = new GameInfo(
                Integer.parseInt(settings[0]),  //Preptime
                Integer.parseInt(settings[1]),  //Gametime
                detectives,                     //detectivelist
                traitors,                       //traitorlist
                name,                           //name
                Player.Role.valueOf(role));     //Role
        done = true;
    }


    @Override
    protected void onPostExecute(GameInfo gameInfo) {
        super.onPostExecute(gameInfo);
        try {
            socket.close();
            lobby.startGameActivity(gameInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        Toast.makeText(lobby,"Connection Error", Toast.LENGTH_SHORT).show();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        lobby.finish();
    }

    @Override
    protected void onProgressUpdate(Player... values) {
        super.onProgressUpdate(values);
        ArrayList<Player> pl = new ArrayList<>(Arrays.asList(values));
        lobby.updateListView(pl);
    }


}
