package me.henry.versatile.framework.demo;


import me.henry.versatile.framework.base.BasePresent;
import me.henry.versatile.framework.base.BaseView;

public class TestContract {


    public static abstract class Present extends BasePresent<View> {

        public Present(TestContract.View view) {
            super(view);
        }

        public abstract void showTost(android.view.View view);

        public abstract void depositRefund(String id);
    }

    public interface View extends BaseView {

        void onRefundSuccess();
    }

}