package com.example.tag.myalarm04;

/**
 * Created by tag on 2017/05/27.
 */

public class AlarmData {
    private int alarmID;
    private String alarmName;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    public AlarmData(int alarmID,String alarmName,int year,int month,int day,int hour,int minute){
        this.alarmID = alarmID;
        this.alarmName = alarmName;
        this.year = year;
        this.month= month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
    }

    public int getYear() {
        return year;
    }

    public int getMonth(){
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getAlarmID(){
        return alarmID;
    }

    public String getAlarmName(){
        return  alarmName;
    }


}
