package me.henry.versatile.fragment.gank;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.henry.versatile.R;
import me.henry.versatile.base.VFragment;
import me.henry.versatile.fragment.music.MusicFragment;
import me.henry.versatile.view.MyTabs;
import me.yokeyword.fragmentation.ISupportFragment;

/**
 * Created by henry on 2017/11/23.
 */

public class GankFragment extends VFragment {
    @BindView(R.id.tl_tabs)
    TabLayout tl_tabs;
    @BindView(R.id.vp_data_block)
    ViewPager vp_data_block;

    List<MyTabs> mTitles;
    List<ISupportFragment> mFragments = new ArrayList<>();
    PagerAdapter mPagerAdapter;

    @Override
    public Object setLayout() {
        return R.layout.fragment_gank;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        //init tabs
        mTitles = new ArrayList<>();
        mTitles.add(new MyTabs("android", R.mipmap.icon_android_on, R.mipmap.icon_android_off));
        mTitles.add(new MyTabs("ios", R.mipmap.icon_ios_on, R.mipmap.icon_ios_off));
        mTitles.add(new MyTabs("web", R.mipmap.icon_web_on, R.mipmap.icon_web_off));
        mTitles.add(new MyTabs("girl", R.mipmap.icon_girl_on, R.mipmap.icon_girl_off));

        //init pager-----------------
        mFragments = new ArrayList<>();
        mFragments.add(GankDataFragment.newInstance(GankType.Android));
        mFragments.add(GankDataFragment.newInstance(GankType.IOS));
        mFragments.add(GankDataFragment.newInstance(GankType.Web));
        mFragments.add(GankDataFragment.newInstance(GankType.MeiZhi));

        mPagerAdapter = new GankPagerAdapter(getMainActivity().getSupportFragmentManager());
        vp_data_block.setAdapter(mPagerAdapter);

        //关联tabs---------顺序不能乱，先addtab,再setupviewpager,再设置文字settext
        tl_tabs.setupWithViewPager(vp_data_block);
        for (int i = 0; i < mTitles.size(); i++) {
            tl_tabs.getTabAt(i).setCustomView(getTabView(mTitles.get(i)));
        }

        tl_tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            View view;
            ImageView iv;

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                view = tab.getCustomView();
                iv = view.findViewById(R.id.iv_tab);
                iv.setImageResource(mTitles.get(tab.getPosition()).getSelectedRes());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                view = tab.getCustomView();
                iv = view.findViewById(R.id.iv_tab);
                iv.setImageResource(mTitles.get(tab.getPosition()).getUn_selectedRes());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                view = tab.getCustomView();
                iv = view.findViewById(R.id.iv_tab);
                iv.setImageResource(mTitles.get(tab.getPosition()).getSelectedRes());

            }
        });
    }

    public View getTabView(MyTabs tab) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_tab, null);
        ImageView img = view.findViewById(R.id.iv_tab);
        if (tab.getType().equals("android")) {
            img.setImageResource(tab.getSelectedRes());
        } else {

            img.setImageResource(tab.getUn_selectedRes());
        }
        return view;
    }

    class GankPagerAdapter extends FragmentPagerAdapter {
        public GankPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            return (Fragment) mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }
}
