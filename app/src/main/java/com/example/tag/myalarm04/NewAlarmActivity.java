package com.example.tag.myalarm04;

import android.app.*;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by tag on 2017/05/27.
 */

public class NewAlarmActivity extends AppCompatActivity implements android.app.TimePickerDialog.OnTimeSetListener{

    private FragmentManager fragmentManager;
    TextView textView;
    Calendar calendar;

    TextView soundFileName;
    String soundFilePath;
    static DBAdapter dbAdapter;

    public static final int RINGTONE_PICKER = 1;
    private Uri mUri;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        final Intent intent1 = new Intent(this,AlarmStopActivity.class);

        final AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        fragmentManager = getFragmentManager();

        textView = (TextView)findViewById(R.id.textView);

        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        final RingtoneManager manager = new RingtoneManager(getApplicationContext());
        manager.setType(RingtoneManager.TYPE_ALARM);


        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerFragment = new TimePickerDialog();
                timePickerFragment.show(fragmentManager,"test");
            }
        });

        Button soundSelectButton = (Button)findViewById(R.id.soundSelectButton);
        soundSelectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Cursor cursor = manager.getCursor();
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "アラーム選択");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM); // アラーム音
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);// デフォルトは表示しない
                startActivityForResult(intent, RINGTONE_PICKER);
            }
        });

        Button button2 = (Button)findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calendar.getTimeInMillis()<System.currentTimeMillis()){
                    calendar.add(Calendar.DAY_OF_YEAR,1);
                }

                Toast.makeText(NewAlarmActivity.this,
                        calendar.get(Calendar.HOUR_OF_DAY)+"時"+
                                calendar.get(Calendar.MINUTE)+"分にアラームをセットします。",
                        Toast.LENGTH_LONG).show();

                EditText inputAlarmName = (EditText)findViewById(R.id.inputAlarmName);
                String alarmName = inputAlarmName.getText().toString();

                dbAdapter = new DBAdapter(NewAlarmActivity.this);
                dbAdapter.open();
                int insertAlarmID = dbAdapter.saveAlarm(
                        alarmName,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        mUri);
                dbAdapter.close();

                intent1.setType(String.valueOf(insertAlarmID));
                PendingIntent pendingIntent1 = PendingIntent.getActivity(NewAlarmActivity.this,0,intent1,0);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),pendingIntent1);

                Intent intent2=new Intent(NewAlarmActivity.this,MainActivity.class);
                startActivity(intent2);

            }
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        textView.setText(hourOfDay+":"+minute);
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RINGTONE_PICKER) {
                // RINGTONE_PICKERからの選択されたデータを取得する
                mUri = (Uri) data.getExtras().get(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), mUri);
                soundFileName = (TextView) findViewById(R.id.soundFileName);
                soundFileName.setText(ringtone.getTitle(getApplicationContext()));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}



