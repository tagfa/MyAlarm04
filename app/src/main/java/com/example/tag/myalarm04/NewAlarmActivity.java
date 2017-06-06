package com.example.tag.myalarm04;

import android.app.*;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * 新規アラーム作成用のクラス
 * Created by tag on 2017/05/27.
 */

public class NewAlarmActivity extends AppCompatActivity implements android.app.TimePickerDialog.OnTimeSetListener,android.app.DatePickerDialog.OnDateSetListener{

    private FragmentManager fragmentManager;
    TextView inputTimeTextView;
    TextView inputDateTextView;
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

        calendar = Calendar.getInstance();

        //現在時刻を初期値として設定
        inputTimeTextView = (TextView)findViewById(R.id.inputTime);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        inputTimeTextView.setText(hour+":"+String.format("%02d",minute));

        //当日の日付を初期値として設定
        inputDateTextView = (TextView)findViewById(R.id.inputDate);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        inputDateTextView.setText(month+1+"/"+dayOfMonth);

        //アラームファイル名とファイルパスを設定
        soundFileName = (TextView) findViewById(R.id.soundFileName);
        soundFileName.setText("");
        soundFilePath="";

        //アラーム選択画面の選択肢はアラームのみ
        final RingtoneManager manager = new RingtoneManager(getApplicationContext());
        manager.setType(RingtoneManager.TYPE_ALARM);

        //時刻設定ボタンを押したときの処理
        Button setTimeButton = (Button)findViewById(R.id.setTimeButton);
        setTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //timePickerFragmentを表示する
                TimePickerDialog timePickerFragment = new TimePickerDialog();
                timePickerFragment.show(fragmentManager,"timePicker");
            }
        });

        //日付設定ボタンを押したときの処理
        Button setDateButton = (Button)findViewById(R.id.setDateButton);
        setDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DatePickerDialogを表示する
                DatePickerDialog datePickerFragment = new DatePickerDialog();
                datePickerFragment.show(fragmentManager,"datePicker");
            }
        });

        //アラーム音選択ボタンを押したときの処理
        Button soundSelectButton = (Button)findViewById(R.id.soundSelectButton);
        soundSelectButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //RINGTONE_PICKERを起動
                Cursor cursor = manager.getCursor();
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "アラーム選択");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM); // アラーム音
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);// デフォルトは表示しない
                startActivityForResult(intent, RINGTONE_PICKER);
            }
        });

        //アラーム設定ボタンを押したときの処理
        Button setAlarmButton = (Button)findViewById(R.id.setAlarmButton);
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(NewAlarmActivity.this,
                        calendar.get(Calendar.HOUR_OF_DAY)+"時"+
                                calendar.get(Calendar.MINUTE)+"分にアラームをセットします。",
                        Toast.LENGTH_LONG).show();

                //画面に入力されたアラーム名を取得する
                EditText inputAlarmName = (EditText)findViewById(R.id.inputAlarmName);
                String alarmName = inputAlarmName.getText().toString();

                //アラームデータをTBLに追加する
                dbAdapter = new DBAdapter(NewAlarmActivity.this);
                dbAdapter.open();
                int insertAlarmID = dbAdapter.saveAlarm(
                        alarmName,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        soundFilePath);
                dbAdapter.close();

                //複数アラームを設定できるようにするため、インテント区別する
                intent1.setType(String.valueOf(insertAlarmID));

                //アラーム音のファイルパスを引き渡す
                intent1.putExtra("soundFilePath",soundFilePath);

                PendingIntent pendingIntent1 = PendingIntent.getActivity(NewAlarmActivity.this,0,intent1,0);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),pendingIntent1);

                //一覧画面へ遷移する
                Intent intent2=new Intent(NewAlarmActivity.this,MainActivity.class);
                startActivity(intent2);

            }
        });

    }

    //選択時刻をTextViewとカレンダーに設定
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        inputTimeTextView.setText(hourOfDay+":"+String.format("%02d",minute));
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
    }

    //選択日付をTextViewとカレンダーに設定
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        inputDateTextView.setText(month+1+"/"+dayOfMonth);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RINGTONE_PICKER) {
                // RINGTONE_PICKERから選択されたデータを取得する
                mUri = (Uri) data.getExtras().get(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), mUri);


                if(mUri!=null) {
                    //アラームが選択された場合
                    soundFileName.setText(ringtone.getTitle(getApplicationContext()));

                    //Uriのgetpath()だとRFC 2396に準拠したpathを返却する
                    //このpathだとsetDataSourceの引数にできないので、実際のファイルパスに変換する
                    soundFilePath = getPath(getApplicationContext());
                }else {
                    //Noneの場合
                    soundFileName.setText("");
                    soundFilePath = "";
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public String getPath(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        String[] columns = { MediaStore.Audio.Media.DATA };
        Cursor cursor = contentResolver.query(mUri, columns, null, null, null);
        cursor.moveToFirst();
        String path = cursor.getString(0);
        cursor.close();
        return path;
    }



}



