package com.example.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MenuActivity extends AppCompatActivity {

    private Button button_play;
    private TextView tv_gameTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        button_play = findViewById(R.id.button_play);
        tv_gameTitle = findViewById(R.id.tv_gameTitle);
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void quitGame(View view)
    {
        Intent intent = new Intent(MenuActivity.this, MenuActivity.class);
        System.exit(0);
    }

}
