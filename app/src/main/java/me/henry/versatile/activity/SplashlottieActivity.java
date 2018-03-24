package me.henry.versatile.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

//import com.airbnb.lottie.LottieAnimationView;

import com.example.lib_db.DBManager;

import java.util.ArrayList;

import me.henry.versatile.R;
import me.henry.versatile.model.note.Note;
import me.henry.versatile.model.note.Punch;

//        animation_view = findViewById(R.id.animation_view);
//        animation_view.setAnimation("data.json");
//        animation_view.setImageAssetsFolder("images/");
//        animation_view.playAnimation();
public class SplashlottieActivity extends AppCompatActivity implements View.OnClickListener {
   // LottieAnimationView animation_view;
    TextView tvaddone;
    TextView tvaddtwo;
    TextView tvmodify;
    TextView tvdelete;
    TextView tvquery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashlottie);
        tvaddone = findViewById(R.id.tvaddone);
        tvaddtwo = findViewById(R.id.tvaddtwo);
        tvmodify = findViewById(R.id.tvmodify);
        tvdelete = findViewById(R.id.tvdelete);
        tvquery = findViewById(R.id.tvquery);

        tvaddone.setOnClickListener(this);
        tvaddtwo.setOnClickListener(this);
        tvmodify.setOnClickListener(this);
        tvdelete.setOnClickListener(this);
        tvquery.setOnClickListener(this);
    }


    public void add1() {
        //note
        Note note1 = new Note();
        note1.setDescription("a's des");
        note1.setName("aaa");
        DBManager.getInstance().getDao(Note.class).newOrUpdate(note1);
        Note note2 = new Note();
        note2.setDescription("b's des");
        note2.setName("bbb");
        DBManager.getInstance().getDao(Note.class).newOrUpdate(note2);
        Note note3 = new Note();
        note3.setDescription("c's des");
        note3.setName("ccc");
        DBManager.getInstance().getDao(Note.class).newOrUpdate(note3);
        //punch
        Punch punch1 = new Punch();
        punch1.setPunchTime("1点");
        DBManager.getInstance().getDao(Punch.class).newOrUpdate(punch1);
        Punch punch2 = new Punch();
        punch2.setPunchTime("2点");
        DBManager.getInstance().getDao(Punch.class).newOrUpdate(punch2);
    }

    public void add2() {
        Note note1 = new Note();
        note1.setDescription("else's des");
        note1.setName("else");
        DBManager.getInstance().getDao(Note.class).newOrUpdate(note1);

//        Note note = new Note();
//        note.setId(3);
//        note.setDescription("c's des");
//        note.setName("ccc");
//        Punch punch3 = new Punch();
//        punch3.setNote(note);
//        punch3.setPunchTime("3点");
//        DBManager.getInstance().getDao(Punch.class).newOrUpdate(punch3);
    }

    public void delete() {
        Note note = new Note();
        note.setId(1);
        DBManager.getInstance().getDao(Note.class).delete(note);
    }

    public void modify() {
        Note note = new Note();
        note.setId(2);
        note.setDescription("哈哈哈,改了！");
        DBManager.getInstance().getDao(Note.class).newOrUpdate(note);
    }

    /**
     * 有问题
     */
    public void query() {
        Punch punch1 = new Punch();
        punch1.setId(2);
        punch1.setPunchTime("666点");
        Note note = new Note();
        note.setName("测试666");
        note.setDescription("miao666");
        ArrayList<Punch> list = new ArrayList<>();
        list.add(punch1);
        note.setPunches(list);
        DBManager.getInstance().getDao(Note.class).newOrUpdate(note);



//        int tableDataCount = DBManager.getInstance().getDao(Note.class).getTableDataCount(DBUtil.getTableName(Note.class));
//        Log.e("ooqp", "query=" + tableDataCount);



//        ArrayList<Note> notes = DBManager.getInstance().getDao(Note.class).queryByField("name", "ccc");
//        if (notes.size() > 0) {
//            Log.e("ooqp", "query=" + notes.get(0).toString());
//
//        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tvaddone:
                Log.e("ooqp", "添加1");
                add1();
                break;
            case R.id.tvaddtwo:
                Log.e("ooqp", "添加2");
                add2();
                break;
            case R.id.tvmodify:
                Log.e("ooqp", "修改");
                modify();
                break;
            case R.id.tvdelete:
                Log.e("ooqp", "删除");
                delete();
                break;
            case R.id.tvquery:
                Log.e("ooqp", "查找");
                query();
                break;
        }
    }
}
