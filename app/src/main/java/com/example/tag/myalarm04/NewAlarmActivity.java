package com.example.tag.myalarm04;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
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

    static DBAdapter dbAdapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);

        Intent intent1 = new Intent(this,AlarmStopActivity.class);
        final PendingIntent pendingIntent1 = PendingIntent.getActivity(this,0,intent1,0);

        final AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        fragmentManager = getFragmentManager();

        textView = (TextView)findViewById(R.id.textView);

        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        textView.setText(hour+":"+minute);

        Button button1 = (Button)findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerFragment = new TimePickerDialog();
                timePickerFragment.show(fragmentManager,"test");
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

                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),pendingIntent1);

                EditText inputAlarmName = (EditText)findViewById(R.id.inputAlarmName);
                String alarmName = inputAlarmName.getText().toString();

                dbAdapter = new DBAdapter(NewAlarmActivity.this);
                dbAdapter.open();
                dbAdapter.saveAlarm(alarmName,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE));
                dbAdapter.close();

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

}



