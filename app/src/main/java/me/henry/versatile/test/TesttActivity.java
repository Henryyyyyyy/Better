package me.henry.versatile.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.launcher.ARouter;

import me.henry.versatile.R;

public class TesttActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testt);
        ARouter.getInstance().build("/test/wan").navigation();

    }
}
