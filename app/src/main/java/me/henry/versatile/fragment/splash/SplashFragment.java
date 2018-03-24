package me.henry.versatile.fragment.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;


import com.example.lib_db.DBManager;

import java.util.ArrayList;

import me.henry.versatile.R;
import me.henry.versatile.base.VFragment;

import me.henry.versatile.base.DatabaseHelper;
import me.henry.versatile.fragment.Mainfragment;
import me.henry.versatile.model.note.Note;
import me.henry.versatile.model.note.Punch;

/**
 * Created by henry on 2017/11/22.
 */

public class SplashFragment extends VFragment {


    @Override
    public Object setLayout() {
        return R.layout.fragment_splash;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportDelegate().startWithPop(new Mainfragment());

            }
        }, 1500);

        DBManager.init(getMainActivity(), new DatabaseHelper(getMainActivity()));
//        initFakeData1();
//        initFakeData2();
//        initFakeData3();
    }

    private void initFakeData3() {
        Note note = new Note();
        note.setName("早起");
        note.setDescription("早起的鸟儿有虫吃，嘿嘿嘿！");
        ArrayList<Punch> punches = new ArrayList<>();
        int day = 1;
        int rHour;
        int rMin;
        for (int i = 0; i < 5; i++) {
            Punch p1 = new Punch();
            p1.setPunchTime(String.valueOf(System.currentTimeMillis()));
            p1.setYear(2018);
            p1.setMonth(1);
            p1.setDay(i + day);
            rHour = (int) (Math.random() * 24);
            rMin = (int) (Math.random() * 60);
            Log.e("miao", "hour=" + rHour + "--------rmin=" + rMin);
            p1.setHour(rHour);
            p1.setMinutes(rMin);
            punches.add(p1);
        }
        note.setPunches(punches);
        DBManager.getInstance().getDao(Note.class).newOrUpdate(note);
    }

    private void initFakeData2() {
        Note note = new Note();
        note.setName("练琴^_^");
        note.setDescription("恕我直言，贝多芬，李斯特什么的都是渣渣！");
        ArrayList<Punch> punches = new ArrayList<>();
        int day = 1;
        int rHour;
        int rMin;
        for (int i = 0; i < 20; i++) {
            Punch p1 = new Punch();
            p1.setPunchTime(String.valueOf(System.currentTimeMillis()));
            p1.setYear(2017);
            p1.setMonth(12);
            p1.setDay(i + day);
            rHour = (int) (Math.random() * 24);
            rMin = (int) (Math.random() * 60);
            Log.e("miao", "hour=" + rHour + "--------rmin=" + rMin);
            p1.setHour(rHour);
            p1.setMinutes(rMin);
            punches.add(p1);
        }
        note.setPunches(punches);
        DBManager.getInstance().getDao(Note.class).newOrUpdate(note);
    }

    private void initFakeData1() {
        Note note = new Note();
        note.setName("健身打卡");
        note.setDescription("每天一小时，以后变成肌肉佬！哈哈哈哈");
        ArrayList<Punch> punches = new ArrayList<>();
        int day = 1;
        int rHour;
        int rMin;
        for (int i = 0; i < 10; i++) {
            Punch p1 = new Punch();
            p1.setPunchTime(String.valueOf(System.currentTimeMillis()));
            p1.setYear(2018);
            p1.setMonth(1);
            p1.setDay(i + day);
            rHour = (int) (Math.random() * 24);
            rMin = (int) (Math.random() * 60);
            Log.e("miao", "hour=" + rHour + "--------rmin=" + rMin);
            p1.setHour(rHour);
            p1.setMinutes(rMin);
            punches.add(p1);
        }
        note.setPunches(punches);
        DBManager.getInstance().getDao(Note.class).newOrUpdate(note);
    }


    public void testcases() {
//                        ArrayList<Note> note=DBManager.getInstance().getDao(Note.class).queryAll();
//                Log.e("hahaah","note size="+note.size());
//                Punch punch=new Punch();
//                punch.setId(21);
//                DBManager.getInstance().getDao(Punch.class).delete(punch);

//                Punch p1 = new Punch();
//                p1.setPunchTime(String.valueOf(System.currentTimeMillis()));
//                p1.setYear(2018);
//                p1.setMonth(1);
//                p1.setDay(23);
//                int Hour = (int) (Math.random() * 24);
//                int Min = (int) (Math.random() * 60);
//                p1.setHour(Hour);
//                p1.setMinutes(Min);
//                DBManager.getInstance().getDao(Punch.class).newOrUpdate(p1);

//                Note note=new Note();
//                note.setId(1);
//                DBManager.getInstance().getDao(Note.class).delete(note);
    }

}
