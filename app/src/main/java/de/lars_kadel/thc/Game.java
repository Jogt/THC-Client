package de.lars_kadel.thc;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Game extends AppCompatActivity {

    private GameInfo gameInfo;
    private TextView nameText;
    private Button revealRoleButton;
    private TextView timeStatusText;
    private TextView timeText;
    private TextView detectiveText;
    private TextView traitorText;
    private Button youDiedButton;
    private String tText;
    private boolean dead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameInfo = getIntent().getParcelableExtra("gameinfo");
        dead = false;
        nameText = findViewById(R.id.nameText);
        revealRoleButton = findViewById(R.id.roleButton);
        timeStatusText = findViewById(R.id.timeStatusText);
        timeText = findViewById(R.id.timeText);
        detectiveText = findViewById(R.id.detectiveText);
        traitorText = findViewById(R.id.traitorText);
        youDiedButton = findViewById(R.id.youDiedButton);

        nameText.setText(gameInfo.name);
        String dText = "";
        for(String s : gameInfo.detectives){
            dText += s +"\n";
        }
        detectiveText.setText(dText);

        tText = "";
        for(String s : gameInfo.traitors){
            tText += s + "\n";
        }

        revealRoleButton.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    switch (gameInfo.role) {
                        case traitor:
                            revealRoleButton.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                            traitorText.setText(tText);
                            break;
                        case innocent:
                            revealRoleButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                            break;
                        case detective:
                            revealRoleButton.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
                            break;
                    }
                    revealRoleButton.setText(gameInfo.role.toString());
                }else{
                    revealRoleButton.setText(R.string.revealrole);
                    revealRoleButton.setTextColor(getResources().getColor(android.R.color.white));
                    traitorText.setText("");
                }
                return false;
            }
        });

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(100);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                long time = System.currentTimeMillis()/1000;
                                if(time <= gameInfo.startTime/1000+gameInfo.prepTime){
                                    String min = ""+(gameInfo.startTime/1000+gameInfo.prepTime-time)/60;
                                    String sec = ""+(gameInfo.startTime/1000+gameInfo.prepTime-time)%60;
                                    if(min.length()<2){min = "0"+min;}
                                    if(sec.length()<2){sec = "0"+sec;}
                                    timeStatusText.setText(getText(R.string.preptime));
                                    timeText.setText(min+":"+sec);
                                }else if(time <= gameInfo.startTime/1000+gameInfo.gameTime){
                                    String min = ""+(gameInfo.startTime/1000+gameInfo.gameTime-time)/60;
                                    String sec = ""+(gameInfo.startTime/1000+gameInfo.gameTime-time)%60;
                                    if(min.length()<2){min = "0"+min;}
                                    if(sec.length()<2){sec = "0"+sec;}
                                    timeStatusText.setText(getText(R.string.gametime));
                                    timeText.setText(min+":"+sec);
                                }else{
                                    timeStatusText.setText(getText(R.string.roundover));
                                    timeText.setText("");
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();


    }


    @Override
    public void onBackPressed(){}

    public void onClick_youDied(View v){
        if(!dead) {
            tText = "";
            traitorText.setText(tText);
            dead = true;
            youDiedButton.setTextAppearance(R.style.BigButtonText);
            youDiedButton.setText(R.string.endgame);
        }else{
            Intent i = new Intent(this, ServerConnector.class);
            startActivity(i);
        }
    }


}
