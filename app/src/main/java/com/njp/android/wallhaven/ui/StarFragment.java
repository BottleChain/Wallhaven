package com.njp.android.wallhaven.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.adapter.ImagesAdapter;
import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.presenter.BasePresenter;
import com.njp.android.wallhaven.utils.EventBusUtil;
import com.njp.android.wallhaven.utils.ImageDao;
import com.njp.android.wallhaven.utils.SPUtil;
import com.njp.android.wallhaven.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏界面
 */

public class StarFragment extends BaseFragment {

    private AppBarLayout mTopBar;
    private FloatingActionButton mFAB;
    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<ImageInfo> mList;
    private ImagesAdapter mAdapter;

    @Override
    public BasePresenter createPresenter() {
        return null;
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_star, container, false);
        mTopBar = view.findViewById(R.id.top_bar);
        mFAB = view.findViewById(R.id.fab);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mList = new ArrayList<>();
        mAdapter = new ImagesAdapter(getContext(), mList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(mAdapter);

        changeSkin(SPUtil.getString("skin", "blue"));
        return view;
    }

    @Override
    public void initEvent() {

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mFAB.hide();
                } else {
                    mFAB.show();
                }
            }
        });

        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.scrollToPosition(0);
            }
        });

        mAdapter.setClickListener(new ImagesAdapter.OnImageItemClickListener() {
            @Override
            public void onClick(ImageInfo imageInfo) {
                DetailActivity.actionStart(getActivity(), imageInfo);
            }
        });

        mAdapter.setLongClickListener(new ImagesAdapter.OnImageItemLongClickListener() {
            @Override
            public void onLongClick(final ImageInfo imageInfo, final boolean exists) {
                new AlertDialog.Builder(getContext())
                        .setTitle(imageInfo.getId())
                        .setMessage(exists ? "取消收藏咩？" : "收藏咩？")
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (exists) {
                                    ImageDao.delete(imageInfo);
                                    ToastUtil.show("已取消收藏");
                                } else {
                                    ImageDao.insert(imageInfo);
                                    ToastUtil.show("已收藏");
                                }
                                EventBus.getDefault().post(EventBusUtil.ChangeStarEvent.CHANGE_STAR_EVENT);
                            }
                        }).show();
            }
        });
    }


    private void refresh() {
        mList.clear();
        mList.addAll(ImageDao.queryAll());
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        mRefreshLayout.setRefreshing(true);
        refresh();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onChangeStar(EventBusUtil.ChangeStarEvent event) {
        refresh();
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
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_blue));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_blue));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_blue));
                break;
            case "red":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_red));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_red));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_red));
                break;
            case "green":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_green));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_green));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_green));
                break;
            case "orange":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_orange));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_orange));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_orange));
                break;
            case "purple":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_purple));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_purple));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_purple));
                break;
            case "gray":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_gray));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_gray));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_gray));
                break;
            case "brown":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_brown));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_brown));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_brown));
                break;
            case "yellow":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_yellow));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_yellow));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_yellow));
                break;
            case "cyan":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_cyan));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_cyan));
                mRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.holo_cyan));
                break;
        }
    }

}
