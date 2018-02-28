package com.njp.android.wallhaven.ui;


import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.adapter.FragmentsAdapter;
import com.njp.android.wallhaven.presenter.BasePresenter;
import com.njp.android.wallhaven.utils.EventBusUtil;
import com.njp.android.wallhaven.utils.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Home页面
 */

public class HomeFragment extends BaseFragment {

    private ViewPager mViewPager;
    private AppBarLayout mTopBar;
    private TabLayout mTabLayout;
    private ImageView mIvSkin;
    private FloatingActionButton mFAB;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mViewPager = view.findViewById(R.id.view_pager);
        mTopBar = view.findViewById(R.id.top_bar);
        mTabLayout = view.findViewById(R.id.tab_layout);
        mFAB = view.findViewById(R.id.fab);
        mIvSkin = view.findViewById(R.id.iv_skin);

        List<Fragment> fragmentList = new ArrayList<>();
        List<String> stringList = new ArrayList<>();
        stringList.add("推荐");
        stringList.add("最新");
        stringList.add("最热");
        for (int i = 0; i < stringList.size(); i++) {
            fragmentList.add(GalleryFragment.getGalleryFragment(i));
        }

        FragmentsAdapter adapter = new FragmentsAdapter(
                getActivity().getSupportFragmentManager(),
                fragmentList, stringList
        );
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);

        changeSkin(SPUtil.getString("skin","blue"));

        return view;
    }

    @Override
    public void initEvent() {

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(EventBusUtil.ScrollEvent.SCROLL_TO_TOP);
            }
        });

        mIvSkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu();
            }
        });

    }

    private void showPopMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(),mIvSkin);
        popupMenu.getMenuInflater().inflate(R.menu.menu_skin,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.skin_blue:
                        SPUtil.putString("skin","blue");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_BLUE);
                        break;
                    case R.id.skin_red:
                        SPUtil.putString("skin","red");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_RED);
                        break;
                    case R.id.skin_green:
                        SPUtil.putString("skin","green");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_GREEN);
                        break;
                    case R.id.skin_orange:
                        SPUtil.putString("skin","orange");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_ORANGE);
                        break;
                }
                return true;
            }
        });
    popupMenu.show();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollEvent(EventBusUtil.ScrollEvent event) {
        if (event == EventBusUtil.ScrollEvent.SCROLL_UP) {
            mFAB.hide();
        } else if (event == EventBusUtil.ScrollEvent.SCROLL_DOWN) {
            mFAB.show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeSkin(EventBusUtil.ChangeSkinEvent event){
        switch (event){
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

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    private void changeSkin(String skin){
        switch (skin){
            case "blue":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_blue_light));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_blue_light));
                break;
            case "red":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_red_light));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_red_light));
                break;
            case "green":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_green_light));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_green_light));
                break;
            case "orange":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_orange_light));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_orange_light));
                break;
        }
    }

}
