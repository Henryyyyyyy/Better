package me.henry.versatile

import android.databinding.DataBindingUtil.setContentView
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.alibaba.android.arouter.launcher.ARouter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed({
ARouter.getInstance().build("/test/WanAndroidActivity").navigation()
        },3000)
    }
}
