package com.example.tag.myalarm04;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

/**
 * アラーム起動時のクラス
 * Created by tag on 2017/05/27.
 */

public class AlarmStopActivity extends AppCompatActivity {

    private MediaPlayer mp;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_alarm);

        Intent intent = getIntent();

        //インテントからアラーム音のファイルパスを取得する
        String soundFilePath = intent.getStringExtra("soundFilePath");

        mp = new MediaPlayer();

        if(soundFilePath.equals("")==false) {

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
            mp.setLooping(true);
            mp.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp.isPlaying()==true){
            mp.stop();
        }
        mp.release();
    }
}
