package com.njp.android.wallhaven.ui;

import android.os.Build;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.utils.EventBusUtil;
import com.njp.android.wallhaven.utils.SPUtil;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private BottomBar mBottomBar;

    private HomeFragment mHomeFragment;
    private SearchFragment mSearchFragment;
    private StarFragment mStarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initEvent();


    }

    private void initEvent() {

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (tabId){
                    case R.id.tab_home:
                        transaction.show(mHomeFragment).hide(mSearchFragment).hide(mStarFragment);
                        break;
                    case R.id.tab_search:
                        transaction.show(mSearchFragment).hide(mHomeFragment).hide(mStarFragment);
                        break;
                    case R.id.tab_star:
                        transaction.show(mStarFragment).hide(mSearchFragment).hide(mHomeFragment);
                        break;
                }
                transaction.commit();
            }
        });
    }

    private void initView() {
       mBottomBar = findViewById(R.id.bottom_bar);
        mHomeFragment = new HomeFragment();
        mSearchFragment = new SearchFragment();
        mStarFragment = new StarFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_layout, mHomeFragment)
                .add(R.id.content_layout, mSearchFragment)
                .add(R.id.content_layout, mStarFragment)
                .commit();

        changeSkin(SPUtil.getString("skin", "blue"));
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeSkin(EventBusUtil.ChangeSkinEvent event) {
        switch (event) {
            case SKIN_BLUE:
                changeSkin("blue");
                break;
            case SKIN_RED:
                changeSkin("red");
                break;
            case SKIN_GREEN:
                changeSkin("green");
                break;
            case SKIN_ORANGE:
                changeSkin("orange");
                break;
        }
    }

    private void changeSkin(String color) {
        switch (color) {
            case "blue":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_blue_light));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_blue_dark));
                }
                break;
            case "red":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_red_light));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_red_dark));
                }
                break;
            case "green":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_green_light));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_green_dark));
                }
                break;
            case "orange":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_orange_light));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_orange_dark));
                }
                break;
        }
    }


}
