package com.njp.android.wallhaven.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import java.util.List;

/**
 * Fragment+ViewPager的适配器
 */

public class FragmentsAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private List<Fragment> mFragmentList;
    private List<String> mStringList;

    public FragmentsAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> stringList) {
        super(fm);
        mFragmentList = fragmentList;
        mStringList = stringList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mStringList.get(position);
    }
}
