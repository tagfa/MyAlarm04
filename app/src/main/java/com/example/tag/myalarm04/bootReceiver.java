package com.example.tag.myalarm04;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by tag on 2017/06/17.
 */

public class BootReceiver extends BroadcastReceiver {
    DBAdapter dbAdapter;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"bootComplete",Toast.LENGTH_LONG).show();

        Intent intent1 = new Intent(context,AlarmStopActivity.class);
        Calendar calendar = Calendar.getInstance();
        dbAdapter.open();
/*
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
                        c.getInt(c.getColumnIndex(DBAdapter.MINUTE)),
                        c.getString(c.getColumnIndex(DBAdapter.SOUNDFILEPATH))
                );
                calendar.set(Calendar.YEAR, alarmData.getYear());
                calendar.set(Calendar.MONTH, alarmData.getMonth());
                calendar.set(Calendar.DAY_OF_MONTH, alarmData.getDay());
                calendar.set(Calendar.HOUR_OF_DAY, alarmData.getHour());
                calendar.set(Calendar.MINUTE, alarmData.getMinute());

                int insertAlarmID = alarmData.getAlarmID();
                //複数アラームを設定できるようにするため、インテント区別する
                intent1.setType(String.valueOf(insertAlarmID));

                String soundFilePath = alarmData.getSoundFilePath();

                //アラーム音のファイルパスを引き渡す
                intent1.putExtra("soundFilePath", soundFilePath);

            } while (c.moveToNext());
        }
*/
    }

}
