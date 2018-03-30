package me.henry.versatile.framework.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;

import me.henry.versatile.R;
import me.henry.versatile.databinding.ActivityVmTestBinding;
import me.henry.versatile.framework.base.AbstractDataBindingActivity;
import me.henry.versatile.framework.base.BasePresent;
import me.henry.versatile.model.gank.GankData;

public class VmTestActivity extends AbstractDataBindingActivity<MyPresenter,ActivityVmTestBinding> implements TestContract.View {



    @Override
    public int setContentView() {
        return R.layout.activity_vm_test;
    }

    @Override
    protected void initData() {
        GankData data=new GankData();
        data.setDesc("hahahahahaha");
        binding.setVariable(BR.gankio,data);
        binding.setVariable(BR.presenter,presenter);
    }


    @Override
    protected MyPresenter newPresent() {
        return new MyPresenter(this);
    }

    @Override
    public void onRefundSuccess() {
        startActivity(new Intent(VmTestActivity.this,VmTestActivity.class));
    }
}
