package com.njp.android.wallhaven.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.njp.android.wallhaven.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * SnakeBar统一管理工具
 */

public class SnakeBarUtil {

    private int color;

    private Resources mResources;

    private static SnakeBarUtil mUtil;

    public static SnakeBarUtil getInstance() {
        if (mUtil == null) {
            mUtil = new SnakeBarUtil();
        }
        return mUtil;
    }

    private SnakeBarUtil() {
    }


    public void init(Context context, String skin) {
        mResources = context.getResources();
        changeSkin(skin);
        EventBus.getDefault().register(this);
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

    private void changeSkin(String skin) {
        switch (skin) {
            case "blue":
                color = R.color.holo_blue;
                break;
            case "red":
                color = R.color.holo_red;
                break;
            case "green":
                color = R.color.holo_green;
                break;
            case "orange":
                color = R.color.holo_orange;
                break;
            case "purple":
                color = R.color.holo_purple;
                break;
            case "gray":
                color = R.color.holo_gray;
                break;
            case "brown":
                color = R.color.holo_brown;
                break;
            case "yellow":
                color = R.color.holo_yellow;
                break;
            case "cyan":
                color = R.color.holo_cyan;
                break;
        }
    }

    public void show(String content, View view) {
        Snackbar snackbar = Snackbar.make(view, content, Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(mResources.getColor(color));
        snackbar.show();

    }

}
