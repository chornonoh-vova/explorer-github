package com.hbvhuwe.explorergithub.ui.adapters

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

abstract class BaseViewPagerAdapter(
        fm: androidx.fragment.app.FragmentManager?,
        private val context: Context,
        private val tabTitles: Array<Int>
): FragmentPagerAdapter(fm) {
    final override fun getCount() = tabTitles.size

    final override fun getPageTitle(position: Int): String =
            context.getString(tabTitles[position])

    final override fun getItem(position: Int) = getFragment(position)

    protected abstract fun getFragment(position: Int): Fragment
}