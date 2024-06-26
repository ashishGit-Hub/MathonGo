package com.ashish.mathongo.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ashish.mathongo.fragments.AllRecipesFragment
import com.ashish.mathongo.fragments.FavouriteRecipesFragment

class HomeViewPagerAdapter(fm: FragmentManager,lifecycle : Lifecycle) : FragmentStateAdapter(fm,lifecycle) {

//    override fun getItem(position: Int): Fragment {
//        return when (position) {
//            0 -> AllRecipesFragment()
//            else -> FavouriteRecipesFragment()
//        }
//    }
//
//    override fun getCount(): Int {
//        return 2
//    }

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllRecipesFragment()
            else -> FavouriteRecipesFragment()
        }
    }
}