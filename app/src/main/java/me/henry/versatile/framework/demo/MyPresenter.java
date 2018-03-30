package me.henry.versatile.framework.demo;

import android.view.View;

public class MyPresenter extends TestContract.Present {

public  String haha="omg";



    public MyPresenter(TestContract.View view) {
        super(view);
    }

    @Override
    public void showTost(View v) {
        view.onRefundSuccess();
    }


    @Override
    public void depositRefund(String id) {

    }


}