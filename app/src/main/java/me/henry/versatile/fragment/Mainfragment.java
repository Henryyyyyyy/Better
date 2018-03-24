package me.henry.versatile.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.gyf.barlibrary.ImmersionBar;


import butterknife.BindView;
import butterknife.ButterKnife;
import me.henry.versatile.R;
import me.henry.versatile.activity.MainActivity;
import me.henry.versatile.base.VFragment;
import me.henry.versatile.fragment.gank.GankFragment;
import me.henry.versatile.fragment.live.NoteFragment;
import me.henry.versatile.fragment.music.MusicFragment;
import me.henry.versatile.fragment.video.VideoFragment;

import me.henry.versatilev2.floattab.FloatTab;
import me.henry.versatilev2.vtitlebar.TitleBar;
import me.yokeyword.fragmentation.ISupportFragment;


/**
 * Created by henry on 2017/11/22.
 */

public class Mainfragment extends VFragment {
    @BindView(R.id.mTitleBar)
    TitleBar mTitleBar;
    @BindView(R.id.cf_3block_container)
    ContentFrameLayout cf_3block_container;

    MainActivity mMainActivity;
    int mCurrentFragmentIndex = 0;
    ISupportFragment[] mFragments;
    @BindView(R.id.ft_menu)
    FloatTab ft_menu;

    @Override
    public Object setLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void onBindView(@Nullable Bundle savedInstanceState, View rootView) {
        mMainActivity = getMainActivity();
        add3MainFragment();
        initTitleBar();

        ft_menu.setOnFoldingItemClickListener(new FloatTab.OnFoldingItemSelectedListener() {
            @Override
            public boolean onFoldingItemSelected( MenuItem item) {
                int goIndex = 0;
                switch (item.getItemId()) {
                    case R.id.ftb_menu_nearby:
                        goIndex = 0;
                        mTitleBar.getCenterTxt().setText("干货");
                        break;
                    case R.id.ftb_menu_new_chat:
                        goIndex = 1;
                        mTitleBar.getCenterTxt().setText("打卡");
                        break;
                    case R.id.ftb_menu_profile:
                        goIndex = 2;
                        mTitleBar.getCenterTxt().setText("音乐");
                        break;
                    case R.id.ftb_menu_settings:
                        goIndex = 3;
                        mTitleBar.getCenterTxt().setText("视频");
                        break;
                }
                if (goIndex != mCurrentFragmentIndex) {
                    getSupportDelegate().showHideFragment(mFragments[goIndex], mFragments[mCurrentFragmentIndex]);
                    mCurrentFragmentIndex = goIndex;
                }
                if (goIndex == 1) {
                    mTitleBar.getRightImg().setVisibility(View.VISIBLE);
                } else {
                    mTitleBar.getRightImg().setVisibility(View.GONE);
                }
                return false;
            }
        });

    }


    private void add3MainFragment() {
        mFragments = new ISupportFragment[4];
        mFragments[0] = new GankFragment();
        mFragments[1] = new NoteFragment();
        mFragments[2] = new MusicFragment();
        mFragments[3] = new VideoFragment();
        getSupportDelegate().loadMultipleRootFragment(R.id.cf_3block_container, mCurrentFragmentIndex, mFragments);

    }


    private void initTitleBar() {
        mTitleBar.getCenterTxt().setText("干货");
        final DrawerLayout drawer = mMainActivity.getDrawerLayout();
        ImageView ivLeft = mTitleBar.getLeftImg();
        mTitleBar.getRightImg().setImageResource(R.mipmap.icon_new_task);
        mTitleBar.getRightImg().setVisibility(View.GONE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawer.isDrawerOpen(Gravity.LEFT)) {
                    drawer.closeDrawer(Gravity.LEFT);
                } else {
                    drawer.openDrawer(Gravity.LEFT);
                }
            }
        });
        mTitleBar.getRightImg().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragments[1] != null) {
                    NoteFragment fragment = ((NoteFragment) mFragments[1]);
                        fragment.newTask();
                }
            }
        });
    }


}
