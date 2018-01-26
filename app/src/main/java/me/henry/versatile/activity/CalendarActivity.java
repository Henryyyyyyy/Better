package me.henry.versatile.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.gyf.barlibrary.ImmersionBar;
import com.ldf.calendar.Utils;
import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;

import java.util.ArrayList;
import java.util.HashMap;

import me.henry.versatile.R;
import me.henry.versatile.view.calendar.CustomDayView;

public class CalendarActivity extends AppCompatActivity {
    MonthPager calendarPager;
    CalendarViewAdapter calendarAdapter;
    OnSelectDateListener onSelectDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        calendarPager = findViewById(R.id.calendarPager);
        initCalendarPager();
        ImmersionBar.with(this).init();
       // StatusBarCompat.translucentStatusBar(this, false);



    }

    private void initCalendarPager() {

        //初始化selectlistener
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
                Log.e("aaqq", "date=" + date.toString());
            }

            @Override
            public void onSelectOtherMonth(int offset) {
                Log.e("aaqq", "offset=" + offset);
            }
        };
        //初始化adapter---------------
        CustomDayView customDayView = new CustomDayView(this, R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(this, onSelectDateListener,
                CalendarAttr.CalendarType.MONTH,
                CalendarAttr.WeekArrayType.Sunday,
                customDayView);
        //初始化带标记的日期--------------
        HashMap markData = new HashMap<>();
        //1表示灰点，0表示红点
        markData.put("2018-1-2", "1");
        markData.put("2018-1-3", "0");
        markData.put("2018-1-6", "1");
        markData.put("2017-11-6", "0");
        calendarAdapter.setMarkData(markData);
//添加滑动监听---------------
        calendarPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    Log.e("aaqq","年="+date.getYear()+"     月="+date.getMonth());

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        calendarPager.setAdapter(calendarAdapter);
        calendarPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);
    }
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();

    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private CalendarDate currentDate;
}
