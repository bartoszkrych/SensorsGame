package com.example.sensorsgame;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void playGame(View v){
        Intent intent = new Intent(this,Game.class);
        startActivity(intent);
    }

    public void exitGame(View v){
        finishAndRemoveTask();
    }
}
