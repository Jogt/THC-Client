package de.lars_kadel.de.thc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Lobby extends AppCompatActivity {

    private CheckBox readyBox;
    private ListView listView;
    private Client c;
    private LobbyAdapter lobbyAdapter;
    private Button startButton;
    private String name;
    public boolean start = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);
        startButton = findViewById(R.id.startButton);
        readyBox = findViewById(R.id.readyBox);
        listView = findViewById(R.id.lobbyListView);
        lobbyAdapter = new LobbyAdapter();
        c = new Client(readyBox, this);
        String ip = getIntent().getStringExtra("ip");
        name = getIntent().getStringExtra("name");
        String[] args = {ip, name};
        c.execute(args);
    }

    public void updateListView(ArrayList<Player> players){
        lobbyAdapter.setPlayers(players);
        listView.setAdapter(lobbyAdapter);
    }

    public void setLeader(boolean b){
        if(b){
            startButton.setVisibility(View.VISIBLE);
        }else{
            startButton.setVisibility(View.GONE);
        }
    }

    public void onClick_start(View v){
        start = true;
    }

    public void startGameActivity(GameInfo gi){
        Intent i = new Intent(this, Game.class);
        i.putExtra("gameinfo",gi);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        c.cancel(false);
    }

    public String getName(){
        return name;
    }

    private class LobbyAdapter extends BaseAdapter {

        ArrayList<Player> players;

        public void setPlayers(ArrayList<Player> players){
            this.players = players;
        }

        @Override
        public int getCount() {
            return players.size();
        }

        @Override
        public Object getItem(int i) {
            return players.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.listitem_lobby, viewGroup, false);
            }

            ((TextView) view.findViewById(R.id.nameField)).setText(players.get(i).getName());
            // ((TextView) view.findViewById(R.id.nameField)).setText("PENIS");
            if(players.get(i).isLeader()) {
                ((ImageView) view.findViewById(R.id.leaderImage)).setVisibility(View.VISIBLE);
            }else{
                ((ImageView) view.findViewById(R.id.leaderImage)).setVisibility(View.GONE);
            }if(players.get(i).isReady()){
                ((ImageView) view.findViewById(R.id.readyImage)).setImageResource(android.R.drawable.presence_online);
            }else{
                ((ImageView) view.findViewById(R.id.readyImage)).setImageResource(android.R.drawable.presence_busy);
            }
            return view;
        }
    }

}
