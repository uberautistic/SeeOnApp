package com.example.seeon.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.seeon.mainfragments.AccountFragment
import com.example.seeon.mainfragments.HomeFragment
import com.example.seeon.mainfragments.TicketsFragment

class PagerAdapter(fragmentActivity : FragmentActivity):FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->HomeFragment()
            1->TicketsFragment()
            else->AccountFragment()
        }
    }
}