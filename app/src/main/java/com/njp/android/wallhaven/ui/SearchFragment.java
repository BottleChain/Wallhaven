package com.njp.android.wallhaven.ui;

import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.njp.android.wallhaven.R;
import com.njp.android.wallhaven.adapter.ImagesAdapter;
import com.njp.android.wallhaven.bean.ImageInfo;
import com.njp.android.wallhaven.presenter.SearchPresenter;
import com.njp.android.wallhaven.utils.EventBusUtil;
import com.njp.android.wallhaven.utils.SPUtil;
import com.njp.android.wallhaven.utils.ToastUtil;
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
 * 搜索页面
 */

public class SearchFragment extends BaseFragment<SearchPresenter> implements com.njp.android.wallhaven.view.SearchView {

    private String sorting = "random";

    private ImageView mIvSort;
    private AppBarLayout mTopBar;
    private SearchView mSearchView;
    private TextView mTvTitle;
    private FloatingActionButton mFAB;
    private SmartRefreshLayout mRefreshLayout;
    private RecyclerView mRecyclerView;
    private List<ImageInfo> mList;
    private ImagesAdapter mAdapter;


    @Override
    public SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mTopBar = view.findViewById(R.id.top_bar);
        mSearchView = view.findViewById(R.id.search_view);
        mTvTitle = view.findViewById(R.id.tv_title);
        mFAB = view.findViewById(R.id.fab);
        mRefreshLayout = view.findViewById(R.id.refresh_layout);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mIvSort = view.findViewById(R.id.iv_sort);

        mList = new ArrayList<>();
        mAdapter = new ImagesAdapter(getContext(), mList);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(mAdapter);

        changeSkin(SPUtil.getString("skin", "blue"));

        return view;
    }

    @Override
    public void initEvent() {

        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTvTitle.setVisibility(View.INVISIBLE);
            }
        });

        mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                mTvTitle.setVisibility(View.VISIBLE);
                return false;
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
                    ToastUtil.show("输入不能为空");
                    return false;
                }
                getPresenter().searchImage(query, sorting);
                mRefreshLayout.autoRefresh();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getPresenter().refresh(sorting);
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getPresenter().loadMore(sorting);
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

        mIvSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopMenu();
            }
        });

    }

    private void showPopMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), mIvSort);
        popupMenu.getMenuInflater().inflate(R.menu.menu_sorting, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.sorting_random:
                        sorting = "random";
                        break;
                    case R.id.sorting_date_added:
                        sorting = "date_added";
                        break;
                    case R.id.sorting_toplist:
                        sorting = "toplist";
                        break;
                    case R.id.sorting_favorites:
                        sorting = "favorites";
                        break;
                }
                return true;
            }
        });
        popupMenu.show();
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

    private void changeSkin(String skin) {
        switch (skin) {
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

    @Override
    public void onSearchSuccess(List<ImageInfo> images) {
        mList.clear();
        mList.addAll(images);
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh(true);
    }

    @Override
    public void onSearchError() {
        Snackbar.make(mFAB, "获取图片失败", Snackbar.LENGTH_SHORT).show();
        mRefreshLayout.finishRefresh(false);
    }

    @Override
    public void onSearchNone() {
        Snackbar.make(mFAB, "什么都没找到", Snackbar.LENGTH_SHORT).show();
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
        Snackbar.make(mFAB, "没有更多了", Snackbar.LENGTH_SHORT).show();
        mRefreshLayout.finishLoadmore(false);
    }
}
