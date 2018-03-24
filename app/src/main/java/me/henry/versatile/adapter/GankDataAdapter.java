package me.henry.versatile.adapter;

import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import me.henry.versatile.R;

import me.henry.versatile.fragment.gank.GankType;
import me.henry.versatile.model.gank.GankData;
import me.henry.versatile.utils.CanuteLog;
import me.henry.versatile.view.scaleimageview.RatioImageView;

/**
 * Created by henry on 2017/11/28.
 */

public class GankDataAdapter extends BaseQuickAdapter<GankData,BaseViewHolder>{
    public int dataType;
    public GankDataAdapter(int layoutResId, @Nullable List<GankData> data) {
        super(layoutResId, data);
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    @Override
    protected void convert(BaseViewHolder helper, GankData item) {
        if (dataType==GankType.MeiZhi){
            CanuteLog.d("looo","load meizi="+item.getUrl());
            Glide.with(mContext)
                    .load(item.getUrl())
                    .into((RatioImageView) helper.getView(R.id.ivMeiZhi));
        }else {
            helper.setText(R.id.tv_title,item.getDesc());
            helper.setText(R.id.tv_author,item.getWho());
        }


    }
}
