

package me.henry.versatile.framework.base;


import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.CallSuper;

import me.henry.versatile.BR;


public abstract class AbstractDataBindingActivity<T extends BasePresent, B extends ViewDataBinding> extends AbstractMvpActivity<T> implements BaseView {
    protected B binding;
    boolean isFirstEntry = true;
    public AbstractDataBindingActivity() {
    }
/**
 * 是否获得焦点，一定要在这里再初始化，不然有时候用到自定义view就获取不到宽高
 */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && isFirstEntry) {
            if(presenter != null) {
                this.binding.setVariable(BR.presenter,presenter);
            }
            initData();
            isFirstEntry = false;
        }
    }
    protected abstract void initData();

    @CallSuper
    public void dealOnWithDataBinding(Bundle savedInstanceState) {
        this.binding = DataBindingUtil.setContentView(this, this.setContentView());
    }

    protected void onDestroy() {
        super.onDestroy();
        this.binding.unbind();
    }

}
