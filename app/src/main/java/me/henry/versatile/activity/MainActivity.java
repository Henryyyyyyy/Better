package me.henry.versatile.activity;


import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.henry.versatile.R;
import me.henry.versatile.adapter.SideAdapter;
import me.henry.versatile.app.Canute;
import me.henry.versatile.fragment.splash.SplashFragment;
import me.henry.versatile.model.others.SideData;
import me.henry.versatile.service.MusicPlayer;
import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends SupportActivity {

    @BindView(R.id.cf_fragment_container)
    ContentFrameLayout cf_fragment_container;
    @BindView(R.id.dl_main_drawer)
    DrawerLayout dl_main_drawer;
    @BindView(R.id.rv_side)
    RecyclerView rv_side;
    private SideAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

            }
        }).check();

        Canute.getConfigurator().withActivity(this);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            loadRootFragment(R.id.cf_fragment_container, new SplashFragment());
        }
        MusicPlayer.bindToService(this);

        initView();
    }

    private void initView() {
        ArrayList<SideData> datas=new ArrayList<>();
        SideData d1=new SideData(R.mipmap.icon_dever,"开发者");
        SideData d2=new SideData(R.mipmap.icon_setting,"设置");
        datas.add(d1);
        datas.add(d2);
        mAdapter=new SideAdapter(R.layout.item_side,datas);
        rv_side.setLayoutManager(new LinearLayoutManager(this));
        rv_side.setAdapter(mAdapter);


    }

    public DrawerLayout getDrawerLayout() {
        return dl_main_drawer;
    }


    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }
    private long exitTime = 0;
    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                 ActivityCompat.finishAfterTransition(this);
            }



        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        System.runFinalization();
    }
}
