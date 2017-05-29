package com.example.tag.myalarm04;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

/**
 * Created by tag on 2017/05/27.
 */

public class AlarmStopActivity extends AppCompatActivity {

    private MediaPlayer mp;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);

        Intent intent = getIntent();
        String soundFilePath = intent.getStringExtra("soundFilePath");

        mp = new MediaPlayer();
        try {
            mp.setDataSource(soundFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();

    }
}
