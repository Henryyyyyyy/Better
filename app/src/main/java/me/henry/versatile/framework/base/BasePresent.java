//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package me.henry.versatile.framework.base;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public abstract class BasePresent<V extends BaseView> {
    private V targetView;
    protected V view;

    public BasePresent(V view) {
        this.targetView = view;
        this.view = (V) Proxy.newProxyInstance(view.getClass().getClassLoader(), view.getClass().getInterfaces(), new NotNullHandler());
    }



    public void onDestroy() {
        this.targetView = null;
    }

    public class NotNullHandler implements InvocationHandler {
        public NotNullHandler() {
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if(BasePresent.this.targetView == null) {

                return null;
            } else {
                return method.invoke(BasePresent.this.targetView, args);
            }
        }
    }
}
