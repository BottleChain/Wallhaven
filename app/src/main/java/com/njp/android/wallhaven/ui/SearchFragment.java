package com.njp.android.wallhaven.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
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
import com.njp.android.wallhaven.utils.ImageDao;
import com.njp.android.wallhaven.utils.SPUtil;
import com.njp.android.wallhaven.utils.SnakeBarUtil;
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
                search(query);
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
                if (mList.size() > 0) {
                    getPresenter().refresh(sorting);
                } else {
                    mRefreshLayout.finishRefresh();
                }
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

    public void search(String query) {
        mRefreshLayout.autoRefresh();
        mSearchView.onActionViewCollapsed();
        mTvTitle.setText(query);
        mTvTitle.setVisibility(View.VISIBLE);
        mTopBar.setExpanded(true);
        getPresenter().searchImage(query, sorting);
    }

    private void showPopMenu() {
        PopupMenu popupMenu = new PopupMenu(getContext(), mIvSort);
        popupMenu.getMenuInflater().inflate(R.menu.menu_sorting, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                item.setChecked(true);
                switch (item.getItemId()) {
                    case R.id.sorting_random:
                        sorting = "random";
                        break;
                    case R.id.sorting_date_added:
                        sorting = "date_added";
                        break;
                    case R.id.sorting_toplist:
                        sorting = "views";
                        break;
                    case R.id.sorting_favorites:
                        sorting = "favorites";
                        break;
                }
                if (mList.size() > 0) {
                    mRefreshLayout.autoRefresh();
                    getPresenter().refresh(sorting);
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

    private void changeSkin(String skin) {
        switch (skin) {
            case "blue":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_blue));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_blue));
                mRefreshLayout.setPrimaryColorsId(R.color.holo_blue);
                break;
            case "red":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_red));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_red));
                mRefreshLayout.setPrimaryColorsId(R.color.holo_red);
                break;
            case "green":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_green));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_green));
                mRefreshLayout.setPrimaryColorsId(R.color.holo_green);
                break;
            case "orange":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_orange));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_orange));
                mRefreshLayout.setPrimaryColorsId(R.color.holo_orange);
                break;
            case "purple":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_purple));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_purple));
                mRefreshLayout.setPrimaryColorsId(R.color.holo_purple);
                break;
            case "gray":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_gray));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_gray));
                mRefreshLayout.setPrimaryColorsId(R.color.holo_gray);
                break;
            case "brown":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_brown));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_brown));
                mRefreshLayout.setPrimaryColorsId(R.color.holo_brown);
                break;
            case "yellow":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_yellow));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_yellow));
                mRefreshLayout.setPrimaryColorsId(R.color.holo_yellow);
                break;
            case "cyan":
                mTopBar.setBackgroundColor(getResources().getColor(R.color.holo_cyan));
                mFAB.setBackgroundTintList(getResources().getColorStateList(R.color.holo_cyan));
                mRefreshLayout.setPrimaryColorsId(R.color.holo_cyan);
                break;
        }
    }

    @Override
    public void onSearchSuccess(List<ImageInfo> images) {
        mList.clear();
        mList.addAll(images);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
        mRefreshLayout.finishRefresh(true);
    }

    @Override
    public void onSearchError() {
        mRefreshLayout.finishRefresh(false);
        SnakeBarUtil.getInstance().show("获取图片失败", mRefreshLayout);
    }

    @Override
    public void onSearchNone() {
        mList.clear();
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.finishRefresh(false);
        mFAB.hide();
        SnakeBarUtil.getInstance().show("什么都没找到：建议使用英文搜索哦", mRefreshLayout);
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
