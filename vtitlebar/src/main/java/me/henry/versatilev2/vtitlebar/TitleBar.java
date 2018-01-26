package me.henry.versatilev2.vtitlebar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by henry on 2018/1/23.
 */

public class TitleBar extends RelativeLayout {
    ImageView iv_left;
    ImageView iv_right;
    TextView tv_center;
    Context mContext;

    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        LayoutParams left_params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        left_params.addRule(CENTER_VERTICAL);
        left_params.leftMargin = dp2px(12);
        iv_left = new ImageView(mContext);
        iv_left.setImageResource(R.drawable.icon_side);
        iv_left.setLayoutParams(left_params);
        addView(iv_left);
        LayoutParams right_params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        right_params.addRule(CENTER_VERTICAL);
        right_params.addRule(ALIGN_PARENT_RIGHT);
        right_params.rightMargin = dp2px(12);
        iv_right = new ImageView(mContext);
        iv_right.setImageResource(R.drawable.icon_more);
        iv_right.setLayoutParams(right_params);
        addView(iv_right);
        LayoutParams center_params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        center_params.addRule(CENTER_IN_PARENT);
        tv_center = new TextView(mContext);
        tv_center.setText("标题");
        tv_center.setSingleLine();
        tv_center.setTextColor(Color.WHITE);
        tv_center.setLayoutParams(center_params);
        addView(tv_center);
    }

    public int dp2px(float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, mContext.getResources()
                .getDisplayMetrics());
    }


    public ImageView getLeftImg() {
        return iv_left;
    }

    public ImageView getRightImg() {
        return iv_right;
    }

    public TextView getCenterTxt() {
        return tv_center;
    }
}
