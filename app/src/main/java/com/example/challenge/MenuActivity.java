package com.example.challenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MenuActivity extends AppCompatActivity {

    private Button button_play;
    private Button button_quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        button_play = findViewById(R.id.button_play);
        button_quit = findViewById(R.id.button_quit);

        button_play.setBackgroundResource(R.drawable.play);
        button_quit.setBackgroundResource(R.drawable.quit);
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
        System.exit(0);
    }

    public void quitGame(View view)
    {
        System.exit(0);
    }

}
