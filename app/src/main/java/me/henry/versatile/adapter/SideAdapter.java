package me.henry.versatile.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.henry.versatile.R;
import me.henry.versatile.model.others.SideData;

/**
 * Created by henry on 2018/1/24.
 */

public class SideAdapter extends BaseQuickAdapter<SideData,BaseViewHolder>{
    public SideAdapter(int layoutResId, @Nullable List<SideData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SideData item) {
        ((ImageView)helper.getView(R.id.iv_icon)).setImageResource(item.res);
        ((TextView)helper.getView(R.id.tv_txt)).setText(item.txt);
    }
}
