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

        alarmListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("削除");
                builder.setMessage("削除しますか？");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // IDを取得する
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
