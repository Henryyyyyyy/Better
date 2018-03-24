package me.henry.versatile.model.note;

import com.example.lib_db.annotation.Column;
import com.example.lib_db.annotation.Table;

import java.io.Serializable;



/**
 * Created by henry on 2018/1/5.
 */
@Table(name = "punch")
public class Punch implements Serializable{
    @Column(id = true,auto_increment = true)
    public long id;
    @Column
    public String punchTime;
    @Column
    public int year;
    @Column
    public int month;
    @Column
    public int day;
    @Column
    public int hour;
    @Column
    public int minutes;
    @Column(type = Column.ColumnType.TONE)
    public Note note;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(String punchTime) {
        this.punchTime = punchTime;
    }

    public Note getNote() {
        return note;
    }

    public void setNote(Note note) {
        this.note = note;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }


}
