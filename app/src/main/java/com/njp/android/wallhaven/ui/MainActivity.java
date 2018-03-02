package com.njp.android.wallhaven.ui;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.presenter.BasePresenter;
import com.njp.android.wallhaven.utils.EventBusUtil;
import com.njp.android.wallhaven.utils.SPUtil;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends BaseActivity {

    private BottomBar mBottomBar;

    private HomeFragment mHomeFragment;
    private SearchFragment mSearchFragment;
    private StarFragment mStarFragment;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView(savedInstanceState);

        initEvent();

    }

    private void initEvent() {

        mBottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(int tabId) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (tabId) {
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
                transaction.commitAllowingStateLoss();
            }
        });
    }

    private void initView(Bundle savedInstanceState) {
        FragmentManager manager = getSupportFragmentManager();
        mBottomBar = findViewById(R.id.bottom_bar);
        if (savedInstanceState != null) {
            mHomeFragment = (HomeFragment) manager.findFragmentByTag("home");
            mSearchFragment = (SearchFragment) manager.findFragmentByTag("search");
            mStarFragment = (StarFragment) manager.findFragmentByTag("star");
        } else {
            mHomeFragment = new HomeFragment();
            mSearchFragment = new SearchFragment();
            mStarFragment = new StarFragment();
            manager.beginTransaction()
                    .add(R.id.content_layout, mHomeFragment, "home")
                    .add(R.id.content_layout, mSearchFragment, "search")
                    .add(R.id.content_layout, mStarFragment, "star")
                    .commit();
        }

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
            case SKIN_PURPLE:
                changeSkin("purple");
                break;
            case SKIN_GRAY:
                changeSkin("gray");
                break;
            case SKIN_BROWN:
                changeSkin("brown");
                break;
            case SKIN_YELLOW:
                changeSkin("yellow");
                break;
            case SKIN_CYAN:
                changeSkin("cyan");
                break;
        }
    }


    private void changeSkin(String color) {
        switch (color) {
            case "blue":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_blue));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_blue));
                }
                break;
            case "red":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_red));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_red));
                }
                break;
            case "green":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_green));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_green));
                }
                break;
            case "orange":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_orange));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_orange));
                }
                break;
            case "purple":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_purple));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_purple));
                }
                break;
            case "gray":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_gray));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_gray));
                }
                break;
            case "brown":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_brown));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_brown));
                }
                break;
            case "yellow":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_yellow));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_yellow));
                }
                break;
            case "cyan":
                mBottomBar.setActiveTabColor(getResources().getColor(R.color.holo_cyan));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setStatusBarColor(getResources().getColor(R.color.holo_cyan));
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DetailActivity.REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String query = data.getStringExtra("query");
                    mBottomBar.selectTabAtPosition(1);
                    mSearchFragment.search(query);
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
