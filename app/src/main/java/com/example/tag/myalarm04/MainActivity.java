package com.example.tag.myalarm04;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 *  一覧画面表示用のクラス
 *  Created by tag on 2017/05/27.
 */

public class MainActivity extends AppCompatActivity {

    private AlarmListAdapter alarmListAdapter;
    static DBAdapter dbAdapter;

    private List<AlarmData>alarmDatas;
    private ListView alarmListView;
    protected AlarmData alarmData;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbAdapter = new DBAdapter(this);

        alarmDatas = new ArrayList<>();
        alarmListAdapter = new AlarmListAdapter(this, alarmDatas);
        alarmListView = (ListView) findViewById(R.id.alarmList);

        //アラームデータをDBから読み込み、表示
        loadAlarm();

        //アラーム新規作成時の処理
        Button newAlarmButton = (Button)findViewById(R.id.newAlarmButton);
        newAlarmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //アラーム作成画面へ遷移
                intent = new Intent(MainActivity.this,NewAlarmActivity.class);
                startActivity(intent);

            }
        });

        //項目長押し時の処理
        alarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("削除");
                builder.setMessage("削除しますか？");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // アラームIDを取得する
                        alarmData = alarmDatas.get(position);
                        int deleteAlarmId = alarmData.getAlarmID();

                        //TBLから削除
                        dbAdapter.open();
                        dbAdapter.deleteAlarm(deleteAlarmId);
                        dbAdapter.close();

                        //pendingintentも削除
                        Intent deleteIntent = new Intent(MainActivity.this,AlarmStopActivity.class);
                        deleteIntent.setType(String.valueOf(deleteAlarmId));
                        PendingIntent pendingIntent1 = PendingIntent.getActivity(MainActivity.this,0,deleteIntent,0);
                        pendingIntent1.cancel();

                        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
                        alarmManager.cancel(pendingIntent1);

                        loadAlarm();
                    }
                });

                builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                // ダイアログの表示
                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });

    }

    @Override
    protected void onResume() {
        //アラーム一覧を取得しなおす
        dbAdapter = new DBAdapter(this);

        alarmDatas = new ArrayList<>();
        alarmListAdapter = new AlarmListAdapter(this, alarmDatas);
        alarmListView = (ListView) findViewById(R.id.alarmList);

        loadAlarm();

        super.onResume();
    }

    //アラームデータをDBから読み込み、表示
    protected void loadAlarm(){
        alarmDatas.clear();
        dbAdapter.open();
        Cursor c = dbAdapter.getAllAlarm();

        if(c.moveToFirst()){

            do {
                //1レコード取得する
                AlarmData alarmData = new AlarmData(
                        c.getInt(c.getColumnIndex(DBAdapter.ALARMID)),
                        c.getString(c.getColumnIndex(DBAdapter.ALARMNAME)),
                        c.getInt(c.getColumnIndex(DBAdapter.YEAR)),
                        c.getInt(c.getColumnIndex(DBAdapter.MONTH)),
                        c.getInt(c.getColumnIndex(DBAdapter.DAY)),
                        c.getInt(c.getColumnIndex(DBAdapter.HOUR)),
                        c.getInt(c.getColumnIndex(DBAdapter.MINUTE)),
                        c.getString(c.getColumnIndex(DBAdapter.SOUNDFILEPATH))
                );

                //リストに追加
                alarmDatas.add(alarmData);
            } while(c.moveToNext());
        }

        c.close();
        dbAdapter.close();
        alarmListView.setAdapter(alarmListAdapter);
        alarmListAdapter.notifyDataSetChanged();

    }


}
