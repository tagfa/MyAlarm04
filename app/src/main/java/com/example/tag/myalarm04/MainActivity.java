package com.example.tag.myalarm04;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AlarmListAdapter alarmListAdapter;
    static DBAdapter dbAdapter;

    private List<AlarmData>alarmDatas;
    private ListView alarmListView;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this,"onCreate",Toast.LENGTH_SHORT).show();
        dbAdapter = new DBAdapter(this);

        alarmDatas = new ArrayList<>();

        alarmListAdapter = new AlarmListAdapter(this, alarmDatas);

        alarmListView = (ListView) findViewById(R.id.alarmList);

        loadAlarm();

        Button newAlarmButton = (Button)findViewById(R.id.newAlarmButton);
        newAlarmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,NewAlarmActivity.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        Toast.makeText(this,"onResume",Toast.LENGTH_SHORT).show();
        dbAdapter = new DBAdapter(this);

        alarmDatas = new ArrayList<>();

        alarmListAdapter = new AlarmListAdapter(this, alarmDatas);

        alarmListView = (ListView) findViewById(R.id.alarmList);

        loadAlarm();

        super.onResume();
    }

    protected void loadAlarm(){
        // Read
        alarmDatas.clear();
        dbAdapter.open();
        Cursor c = dbAdapter.getAllAlarm();

        if(c.moveToFirst()){
            do {
                AlarmData alarmData = new AlarmData(
                        c.getInt(c.getColumnIndex(DBAdapter.ALARMID)),
                        c.getString(c.getColumnIndex(DBAdapter.ALARMNAME)),
                        c.getInt(c.getColumnIndex(DBAdapter.YEAR)),
                        c.getInt(c.getColumnIndex(DBAdapter.MONTH)),
                        c.getInt(c.getColumnIndex(DBAdapter.DAY)),
                        c.getInt(c.getColumnIndex(DBAdapter.HOUR)),
                        c.getInt(c.getColumnIndex(DBAdapter.MINUTE))
                );

                alarmDatas.add(alarmData);
            } while(c.moveToNext());
        }

        c.close();
        dbAdapter.close();
        alarmListView.setAdapter(alarmListAdapter);
        alarmListAdapter.notifyDataSetChanged();

    }


}
