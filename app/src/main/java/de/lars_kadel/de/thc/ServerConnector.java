package de.lars_kadel.de.thc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class ServerConnector extends AppCompatActivity {

    EditText nameInput;
    EditText ipInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_connector);
        nameInput = (EditText) findViewById(R.id.nameInput);
        ipInput = (EditText) findViewById(R.id.ipInput);
    }

    public void onClick_connect(View v){
        String ip = validateIp(ipInput.getText().toString());
        String name = validateName(nameInput.getText().toString());
        if(ip != null && name != null) {
            Intent i = new Intent(this, Lobby.class);
            i.putExtra("ip", ip);
            i.putExtra("name", name);
            startActivity(i);
        }
    }

    private String validateName(String s) {
        if(!s.equals("")){
            return s;
        }
        return null;
    }

    private String validateIp(String s) {
        if(!s.equals("")) {
            return s;
        }
        return null;
    }

}
