package me.henry.versatile.fragment.live;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.lib_db.DBManager;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import me.henry.versatile.R;
import me.henry.versatile.adapter.NoteAdapter;
import me.henry.versatile.base.VFragment;

import me.henry.versatile.model.note.Note;
import me.henry.versatile.model.note.Punch;

/**
 * Created by henry on 2017/11/23.
 */

public class NoteFragment extends VFragment {

    @BindView(R.id.rv_notes)
    RecyclerView rv_notes;

    NoteAdapter mAdapter;
    ArrayList<Note> mDatas = new ArrayList<>();
    public MaterialDialog dialog;

    @Override
    public Object setLayout() {
        return R.layout.fragment_note;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {


    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new NoteAdapter(R.layout.item_note, new ArrayList<Note>());

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Note item = mAdapter.getData().get(position);
                switch (view.getId()) {
                    case R.id.ll_total_time:
                        startFragmentFromMain(NoteDetailFragment.newInstance(item.getPunches(), item));
                        break;
                    case R.id.iv_punch:
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1;
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        int hours = calendar.get(Calendar.HOUR_OF_DAY);
                        int mins = calendar.get(Calendar.MINUTE);
                        long currentmill = System.currentTimeMillis();
                        Punch punch = new Punch();
                        punch.setPunchTime(String.valueOf(currentmill));
                        punch.setYear(year);
                        punch.setMonth(month);
                        punch.setDay(day);
                        punch.setHour(hours);
                        punch.setMinutes(mins);
                        punch.setNote(item);
                        ArrayList<Punch> punches = item.getPunches();
                        if (punches == null) {
                            punches = new ArrayList<>();
                        }
                        punches.add(punch);
                        item.setPunches(punches);
                        DBManager.getInstance().getDao(Note.class).newOrUpdate(item);
                        mAdapter.notifyItemChanged(position);
                        break;
                }
            }
        });
        mAdapter.openLoadAnimation();
        rv_notes.setLayoutManager(linearLayoutManager);
        rv_notes.setAdapter(mAdapter);
    }


    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                mDatas = DBManager.getInstance().getDao(Note.class).queryAll();
                _mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mDatas != null) {
                                    if (mDatas.size() > 0) {
                                        mAdapter.setNewData(mDatas);
                                    }
                                }
                            }
                        }, 400);

                    }
                });
            }
        }).start();

    }

    public void newTask() {
        dialog = new MaterialDialog.Builder(_mActivity)
                .title("新建任务")
                .customView(R.layout.dialog_new_note, true)
                .positiveText("NEW")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        View view = dialog.getCustomView();
                        EditText edt_title = view.findViewById(R.id.edt_title);
                        EditText edt_intro = view.findViewById(R.id.edt_title);
                        String title = edt_title.getText().toString();
                        String intro = edt_intro.getText().toString();
                        if (!title.equals("") && !intro.equals("")) {
                            Note note = new Note();
                            note.setName(title);
                            note.setDescription(intro);
                            long id = DBManager.getInstance().getDao(Note.class).newOrUpdate(note);
                            note.setId(id);
                            mAdapter.setData(0, note);
                        } else {
                            Toast.makeText(_mActivity, "内容不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .negativeText("CANCEL")
                .show();
    }

}
