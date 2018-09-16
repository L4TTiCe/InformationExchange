package io.github.l4ttice.informationexchange

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class PostDetailsFragmentPagerAdapter(fm: FragmentManager, val PID: String) : FragmentPagerAdapter(fm) {

    private var TabTitles = arrayOf("Content", "Details")

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return attachPIDPostContentFragment(PID)
            else -> return attachPIDPostDetailsFragment(PID)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return TabTitles[position]
    }

    override fun getCount(): Int {
        return 2
    }

    fun attachPIDPostContentFragment(PID: String): Fragment {
        var bundle: Bundle = Bundle()
        bundle.putString("PID", PID)

        var frag = PostContentFragment()
        frag.arguments = bundle

        return frag
    }

    fun attachPIDPostDetailsFragment(PID: String): Fragment {
        var bundle: Bundle = Bundle()
        bundle.putString("PID", PID)

        var frag = PostDetailsFragment()
        frag.arguments = bundle

        return frag
    }
}