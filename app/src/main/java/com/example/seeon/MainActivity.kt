package com.example.seeon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.seeon.adapter.PagerAdapter
import com.example.seeon.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val tabIconsId: Array<Int> = arrayOf(
        R.drawable.home_icon,
        R.drawable.ticket_icon,
        R.drawable.account_icon)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initial()
    }
    private fun getTintIconColor(active: Boolean):Int{
        var colorId=0
        if (active)
            colorId= ContextCompat.getColor(this,R.color.purple)
        else
            colorId= ContextCompat.getColor(this,R.color.white)
        return colorId

    //popBackStack
    }
    private fun initial() {
        binding.viewPager.adapter=PagerAdapter(this)
        TabLayoutMediator(binding.tabLayout,binding.viewPager){
            tab, pos->
            tab.setIcon(tabIconsId[pos])

        }.attach()
        /*binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon?.setTint(getTintIconColor(true))

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.setTint(getTintIconColor(false))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }

        }
        )*/
    }
}