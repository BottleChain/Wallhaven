package com.njp.android.wallhaven.ui;


import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.SimpleAdapter;

import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.adapter.FragmentsAdapter;
import com.njp.android.wallhaven.presenter.BasePresenter;
import com.njp.android.wallhaven.utils.EventBusUtil;
import com.njp.android.wallhaven.utils.SPUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        changeSkin(SPUtil.getString("skin", "blue"));

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
                showDialog();
            }
        });

    }

    private void showDialog() {

        String[] names = {
                "红色主题", "橙色主题", "黄色主题",
                "绿色主题", "青色主题", "蓝色主题",
                "紫色主题", "棕色主题", "灰色主题"
        };

        int[] colors = {
                R.drawable.skin_red, R.drawable.skin_orange, R.drawable.skin_yellow,
                R.drawable.skin_green, R.drawable.skin_cyan, R.drawable.skin_blue,
                R.drawable.skin_purple, R.drawable.skin_brown, R.drawable.skin_gray
        };

        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            Map<String, Object> item = new HashMap();
            item.put("name", names[i]);
            item.put("color", colors[i]);
            items.add(item);
        }

        new AlertDialog.Builder(getContext())
                .setTitle("选择主题")
                .setAdapter(new SimpleAdapter(
                                getContext(), items, R.layout.item_skin,
                                new String[]{"name", "color"},
                                new int[]{R.id.tv_name, R.id.iv_color}
                        ),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        SPUtil.putString("skin", "red");
                                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_RED);
                                        break;
                                    case 1:
                                        SPUtil.putString("skin", "orange");
                                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_ORANGE);
                                        break;
                                    case 2:
                                        SPUtil.putString("skin", "yellow");
                                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_YELLOW);
                                        break;
                                    case 3:
                                        SPUtil.putString("skin", "green");
                                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_GREEN);
                                        break;
                                    case 4:
                                        SPUtil.putString("skin", "cyan");
                                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_CYAN);
                                        break;
                                    case 5:
                                        SPUtil.putString("skin", "blue");
                                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_BLUE);
                                        break;
                                    case 6:
                                        SPUtil.putString("skin", "purple");
                                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_PURPLE);
                                        break;
                                    case 7:
                                        SPUtil.putString("skin", "brown");
                                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_BROWN);
                                        break;
                                    case 8:
                                        SPUtil.putString("skin", "gray");
                                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_GRAY);
                                        break;
                                }
                            }
                        })
                .show();

    }

    private void showPopMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), mIvSkin);
        popupMenu.getMenuInflater().inflate(R.menu.menu_skin, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.skin_blue:
                        SPUtil.putString("skin", "blue");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_BLUE);
                        break;
                    case R.id.skin_red:
                        SPUtil.putString("skin", "red");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_RED);
                        break;
                    case R.id.skin_green:
                        SPUtil.putString("skin", "green");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_GREEN);
                        break;
                    case R.id.skin_orange:
                        SPUtil.putString("skin", "orange");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_ORANGE);
                        break;
                    case R.id.skin_purple:
                        SPUtil.putString("skin", "purple");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_PURPLE);
                        break;
                    case R.id.skin_gray:
                        SPUtil.putString("skin", "gray");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_GRAY);
                        break;
                    case R.id.skin_brown:
                        SPUtil.putString("skin", "brown");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_BROWN);
                        break;
                    case R.id.skin_yellow:
                        SPUtil.putString("skin", "yellow");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_YELLOW);
                        break;
                    case R.id.skin_cyan:
                        SPUtil.putString("skin", "cyan");
                        EventBus.getDefault().post(EventBusUtil.ChangeSkinEvent.SKIN_CYAN);
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

    private void changeSkin(String skin) {
        switch (skin) {
            case "blue":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_blue));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_blue));
                break;
            case "red":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_red));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_red));
                break;
            case "green":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_green));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_green));
                break;
            case "orange":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_orange));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_orange));
                break;
            case "purple":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_purple));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_purple));
                break;
            case "gray":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_gray));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_gray));
                break;
            case "brown":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_brown));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_brown));
                break;
            case "yellow":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_yellow));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_yellow));
                break;
            case "cyan":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_cyan));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_cyan));
                break;
        }
    }

}
