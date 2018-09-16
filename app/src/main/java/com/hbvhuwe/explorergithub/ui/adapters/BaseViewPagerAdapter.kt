package com.hbvhuwe.explorergithub.ui.adapters

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

abstract class BaseViewPagerAdapter(
        fm: FragmentManager?,
        private val context: Context,
        private val tabTitles: Array<Int>
): FragmentPagerAdapter(fm) {
    final override fun getCount() = tabTitles.size

    final override fun getPageTitle(position: Int): String =
            context.getString(tabTitles[position])

    final override fun getItem(position: Int) = getFragment(position)

    protected abstract fun getFragment(position: Int): Fragment
}