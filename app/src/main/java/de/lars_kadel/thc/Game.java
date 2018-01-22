package de.lars_kadel.thc;

import android.annotation.SuppressLint;
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
    private TextView detectiveText;
    private TextView traitorText;
    private Button youDiedButton;
    private String tText;
    private ConstraintLayout rootLayout;
    private boolean dead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameInfo = getIntent().getParcelableExtra("gameinfo");
        dead = false;
        nameText = findViewById(R.id.nameText);
        revealRoleButton = findViewById(R.id.roleButton);
        detectiveText = findViewById(R.id.detectiveText);
        traitorText = findViewById(R.id.traitorText);
        youDiedButton = findViewById(R.id.youDiedButton);
        rootLayout = findViewById(R.id.gameRootLayout);

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
                return checkRoleButton();
            }
        });


    }


    @Override
    public void onBackPressed(){}

    public void onClick_youDied(View v){
        if(!dead) {
            tText = "";
            dead = true;
            revealRoleButton.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Widget_Button);
            youDiedButton.setText(R.string.endgame);
        }
    }

    private boolean checkRoleButton(){
        if(revealRoleButton.isPressed()){
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
        return true;

    }

}
