package me.henry.versatile.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.henry.versatile.R;
import me.henry.versatile.model.note.Punch;

/**
 * Created by henry on 2018/1/13.
 */

public class PunchAdapter extends BaseQuickAdapter<Punch,BaseViewHolder>{


    public PunchAdapter(int layoutResId, @Nullable List<Punch> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Punch item) {
        String hint=item.getHour()<12?" AM":" PM";
        helper.setText(R.id.tv_date,item.getMonth()+"-"+item.getDay());
        helper.setText(R.id.tv_time,item.getHour()+":"+item.getMinutes()+hint);

    }
}
