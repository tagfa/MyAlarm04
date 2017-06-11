package com.example.tag.myalarm04;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

/**
 * Created by tag on 2017/06/08.
 */

public class bootReceiver extends BroadcastReceiver {
    DBAdapter dbAdapter;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"bootComplete",Toast.LENGTH_LONG).show();

        dbAdapter.open();

        Cursor cursor = dbAdapter.getAllAlarm();

        if(cursor.moveToFirst()){


        }

    }
}
