package com.example.calencon.presentation.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.calencon.R
import com.example.calencon.presentation.contacts.ContactsFragment
import com.example.calencon.presentation.group.GroupsFragment
import com.example.calencon.presentation.near_events.NearEventsFragment

private val TAB_TITLES = arrayOf(/*R.string.tab_text_1,*/ R.string.tab_text_2, R.string.tab_text_3)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return when (position) {
            0 -> { GroupsFragment.newInstance() }
            1 -> { ContactsFragment.newInstance() }
//            2 -> { NearEventsFragment.newInstance() }
            else -> { GroupsFragment.newInstance() }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }
}