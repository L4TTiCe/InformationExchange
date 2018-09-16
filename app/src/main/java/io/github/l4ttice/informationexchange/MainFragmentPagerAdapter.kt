package io.github.l4ttice.informationexchange

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class MainFragmentPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var TabTitles = arrayOf("Post", "View")

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return PostFragment()
            else -> return ViewFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TabTitles[position]
    }

    override fun getCount(): Int {
        return 2
    }

}