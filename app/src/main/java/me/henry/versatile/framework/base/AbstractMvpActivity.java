//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package me.henry.versatile.framework.base;


import android.os.Bundle;

public abstract class AbstractMvpActivity<T extends BasePresent> extends AbstractActivity implements BaseView {
    protected T presenter;

    public AbstractMvpActivity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.presenter = this.newPresent();
    }
    protected abstract T newPresent();




    protected void onDestroy() {
        super.onDestroy();
        if(this.presenter != null) {
            this.presenter.onDestroy();
        }

    }
}
