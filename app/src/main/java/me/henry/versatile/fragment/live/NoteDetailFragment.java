package me.henry.versatile.fragment.live;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldf.calendar.component.CalendarAttr;
import com.ldf.calendar.component.CalendarViewAdapter;
import com.ldf.calendar.interf.OnSelectDateListener;
import com.ldf.calendar.model.CalendarDate;
import com.ldf.calendar.view.Calendar;
import com.ldf.calendar.view.MonthPager;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import me.henry.versatile.R;
import me.henry.versatile.adapter.PunchAdapter;
import me.henry.versatile.base.VFragment;
import me.henry.versatile.base.web.WebFragment;
import me.henry.versatile.model.note.Note;
import me.henry.versatile.model.note.Punch;
import me.henry.versatile.view.calendar.CustomDayView;
import me.henry.versatile.view.titlebar.TitleBar;


/**
 * Created by henry on 2018/1/13.
 */

public class NoteDetailFragment extends VFragment implements View.OnClickListener {
    public static final String DATA_PUNCHES = "DATA_PUNCHES";
    public static final String DATA_NOTE = "DATA_NOTE";
    @BindView(R.id.tv_year_month)
    TextView tv_year_month;
    @BindView(R.id.tv_intro)
    TextView tv_intro;
    @BindView(R.id.mCalendarPager)
    MonthPager mCalendarPager;
    @BindView(R.id.rv_punches)
    RecyclerView rv_punches;
    @BindView(R.id.mTitleBar)
    TitleBar mTitleBar;
    @BindView(R.id.iv_previous)
    ImageView iv_previous;
    @BindView(R.id.iv_go)
    ImageView iv_go;
    Note mNote;
    ArrayList<Punch> mPunches = new ArrayList<>();
    PunchAdapter mAdapter;
    //关于datepager的
    CalendarViewAdapter calendarAdapter;
    OnSelectDateListener onSelectDateListener;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    private CalendarDate currentDate;
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Bundle args = getArguments();
        //有可能为空
        mPunches = (ArrayList<Punch>) args.getSerializable(DATA_PUNCHES);
        mNote = (Note) args.getSerializable(DATA_NOTE);


    }

    public static NoteDetailFragment newInstance(ArrayList<Punch> datas, Note note) {
        final Bundle args = new Bundle();
        args.putSerializable(DATA_PUNCHES, datas);
        args.putSerializable(DATA_NOTE, note);

        final NoteDetailFragment fragment = new NoteDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object setLayout() {
        return R.layout.fragment_note_detail;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        //初始化list 和 adapter
        initTitlebar();
        mAdapter = new PunchAdapter(R.layout.item_punches, new ArrayList<Punch>());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter.openLoadAnimation();
        tv_intro.setText(mNote.getDescription());
        rv_punches.setLayoutManager(linearLayoutManager);
        rv_punches.setAdapter(mAdapter);
        initCalendarPager();
        if (mPunches != null) {
            if (mPunches.size() > 0) {
                mAdapter.setNewData(mPunches);
            }
        }
        //初始化现在的日期
        initCurrentDate();
        iv_go.setOnClickListener(this);
        iv_previous.setOnClickListener(this);
    }

    private void initTitlebar() {
        mTitleBar.getCenterTxt().setText("打卡历史");
        ImageView leftImg = mTitleBar.getLeftImg();
        mTitleBar.getRightImg().setVisibility(View.GONE);
        leftImg.setImageResource(R.mipmap.icon_back);
        leftImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop();
            }
        });

    }


    private void initCurrentDate() {
        currentDate = new CalendarDate();
        tv_year_month.setText(currentDate.getYear() + "年 " + currentDate.getMonth() + "月");

    }

    private void initCalendarPager() {

        //初始化selectlistener
        onSelectDateListener = new OnSelectDateListener() {
            @Override
            public void onSelectDate(CalendarDate date) {
            }

            @Override
            public void onSelectOtherMonth(int offset) {
            }
        };
        //初始化adapter---------------
        CustomDayView customDayView = new CustomDayView(getContext(), R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(getContext(), onSelectDateListener,
                CalendarAttr.CalendarType.MONTH,
                CalendarAttr.WeekArrayType.Sunday,
                customDayView);
        //初始化带标记的日期--------------
        if (mPunches != null) {
            if (mPunches.size() > 0) {
                HashMap markData = new HashMap<>();
                for (Punch punch : mPunches) {
                    //1表示灰点，0表示红点
                    markData.put(punch.year + "-" + punch.getMonth() + "-" + punch.getDay(), "0");
                }
                calendarAdapter.setMarkData(markData);
            }
        }


//添加滑动监听---------------
        mCalendarPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
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
                    tv_year_month.setText(date.getYear() + "年 " + date.getMonth() + "月");
                    Log.e("aaqq", "年=" + date.getYear() + "     月=" + date.getMonth());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        mCalendarPager.setAdapter(calendarAdapter);
        mCalendarPager.setCurrentItem(MonthPager.CURRENT_DAY_INDEX);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_go:
                mCalendarPager.setCurrentItem(mCalendarPager.getCurrentPosition() + 1);
                break;
            case R.id.iv_previous:
                mCalendarPager.setCurrentItem(mCalendarPager.getCurrentPosition() - 1);
                break;
        }
    }
}
