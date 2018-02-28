package com.njp.android.wallhaven.ui;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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

    @Override
    public void onRefreshSuccess(List<ImageInfo> images) {
        mList.clear();
        mList.addAll(images);
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh(true);
    }

    @Override
    public void onRefreshError() {
        Snackbar.make(mRefreshLayout, "获取图片失败", Snackbar.LENGTH_SHORT).show();
        mRefreshLayout.finishRefresh(false);
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
    }

    @Override
    public void onNoMore() {
        mRefreshLayout.finishLoadmore(false);
        Snackbar.make(mRefreshLayout, "没有更多了", Snackbar.LENGTH_SHORT).show();
    }
}
