package com.njp.android.wallhaven.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.adapter.ImagesAdapter;
import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.presenter.GalleryPresenter;
import com.njp.android.wallhaven.utils.EventBusUtil;
import com.njp.android.wallhaven.utils.ImageDao;
import com.njp.android.wallhaven.utils.SPUtil;
import com.njp.android.wallhaven.utils.SnakeBarUtil;
import com.njp.android.wallhaven.utils.ToastUtil;
import com.njp.android.wallhaven.view.GalleryView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片展示页面
 */

public class GalleryFragment extends BaseFragment<GalleryPresenter> implements GalleryView {


    public static final int TAG_RANDOM = 0;
    public static final int TAG_LATEST = 1;
    public static final int TAG_TOPLIST = 2;

    private int tag;

    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;

    private List<ImageInfo> mList;
    private ImagesAdapter mAdapter;

    public void setTag(int tag) {
        this.tag = tag;
    }

    public static GalleryFragment getGalleryFragment(int tag) {
        GalleryFragment fragment = new GalleryFragment();
        fragment.setTag(tag);
        return fragment;
    }

    @Override
    public GalleryPresenter createPresenter() {
        return new GalleryPresenter(tag);
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        mList = new ArrayList<>();
        mAdapter = new ImagesAdapter(getContext(), mList);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(mAdapter);

        changeSkin(SPUtil.getString("skin", "blue"));

        return view;
    }

    @Override
    public void initEvent() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getPresenter().refresh();
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getPresenter().loadMore();
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    EventBus.getDefault().post(EventBusUtil.ScrollEvent.SCROLL_UP);
                } else {
                    EventBus.getDefault().post(EventBusUtil.ScrollEvent.SCROLL_DOWN);
                }
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
                                if (exists){
                                    ImageDao.delete(imageInfo);
                                    ToastUtil.show("已取消收藏");
                                }else {
                                    ImageDao.insert(imageInfo);
                                    ToastUtil.show("已收藏");
                                }
                                EventBus.getDefault().post(EventBusUtil.ChangeStarEvent.CHANGE_STAR_EVENT);
                            }
                        }).show();
            }
        });

    }

    @Override
    public void onLazyLoad() {
        mRefreshLayout.autoRefresh();
        getPresenter().refresh();
    }

    @Override
    public void onVisible() {
        super.onVisible();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDisVisible() {
        super.onDisVisible();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onScrollEvent(EventBusUtil.ScrollEvent event) {
        if (event == EventBusUtil.ScrollEvent.SCROLL_TO_TOP) {
            mRecyclerView.scrollToPosition(0);
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

    private void changeSkin(String skin) {
        switch (skin) {
            case "blue":
                mRefreshLayout.setPrimaryColorsId(R.color.holo_blue);
                break;
            case "red":
                mRefreshLayout.setPrimaryColorsId(R.color.holo_red);
                break;
            case "green":
                mRefreshLayout.setPrimaryColorsId(R.color.holo_green);
                break;
            case "orange":
                mRefreshLayout.setPrimaryColorsId(R.color.holo_orange);
                break;
            case "purple":
                mRefreshLayout.setPrimaryColorsId(R.color.holo_purple);
                break;
            case "gray":
                mRefreshLayout.setPrimaryColorsId(R.color.holo_gray);
                break;
            case "brown":
                mRefreshLayout.setPrimaryColorsId(R.color.holo_brown);
                break;
            case "yellow":
                mRefreshLayout.setPrimaryColorsId(R.color.holo_yellow);
                break;
            case "cyan":
                mRefreshLayout.setPrimaryColorsId(R.color.holo_cyan);
                break;
        }
    }

    @Override
    public void onRefreshSuccess(List<ImageInfo> images) {
        mList.clear();
        mList.addAll(images);
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh(true);
    }

    @Override
    public void onRefreshError() {
        mRefreshLayout.finishRefresh(false);
        SnakeBarUtil.getInstance().show("获取图片失败", mRefreshLayout);
    }

    @Override
    public void onLoadMoreSuccess(List<ImageInfo> images) {
        mList.addAll(images);
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.finishLoadmore(true);
    }

    @Override
    public void onLoadMoreError() {
        mRefreshLayout.finishLoadmore(false);
        SnakeBarUtil.getInstance().show("获取图片失败", mRefreshLayout);
    }

    @Override
    public void onNoMore() {
        mRefreshLayout.finishLoadmore(false);
        SnakeBarUtil.getInstance().show("没有更多了", mRefreshLayout);
    }
}
