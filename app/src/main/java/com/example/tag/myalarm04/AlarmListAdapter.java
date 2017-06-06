package com.example.tag.myalarm04;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by tag on 2017/05/27.
 */

public class AlarmListAdapter extends BaseAdapter {

    private Context context;
    List<AlarmData>alarmDatas;

    private SQLiteDatabase db = null;

    public AlarmListAdapter(Context context, List<AlarmData>alarmDatas){
        this.context=context;
        this.alarmDatas=alarmDatas;
    }



    // Listの要素数を返す
    @Override
    public int getCount() {
        return alarmDatas.size();
    }

    // indexやオブジェクトを返す
    @Override
    public Object getItem(int position) {
        return alarmDatas.get(position);
    }

    // IDを他のindexに返す
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // super.getView() は 呼ばない(カスタムビューにしているため)
        View view = convertView;

        // テンプレート処理。
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.alarm_row, parent, false);
        } else {
            view = convertView;
        }

        // データをgetItemで取る
        AlarmData alarm = (AlarmData)getItem(position);

        if(alarm != null) {

            //データをセット
            TextView name = (TextView) view.findViewById(R.id.alarmName);
            name.setText(alarm.getAlarmName());
            TextView alarmTime =(TextView)view.findViewById(R.id.alarmTime);
            alarmTime.setText(alarm.getMonth()+"月"+ alarm.getDay()+"日 "+ alarm.getHour()+":"+ String.format("%02d",alarm.getMinute()) );

        }
        return view;
    }




}
