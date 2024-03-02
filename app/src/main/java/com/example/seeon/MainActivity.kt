package com.example.seeon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.seeon.adapter.PagerAdapter
import com.example.seeon.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAuth: FirebaseAuth
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

    override fun onStart() {
        super.onStart()
        //initFunc()
    }

    private fun initFunc() {
        if(mAuth.currentUser==null){
            replaceActivity(LoginActivity())
            this.finish()
        }
    }

    override fun onResume() {
        super.onResume()

    }
    private fun getTintIconColor(active: Boolean):Int{
        val colorId = if (active)
            ContextCompat.getColor(this,R.color.purple)
        else
            ContextCompat.getColor(this,R.color.white)
        return colorId
    }
    private fun initial() {
        mAuth= FirebaseAuth.getInstance()
        binding.tabLayout.setTabRippleColorResource(R.color.purple)
        binding.viewPager.adapter=PagerAdapter(this)
        TabLayoutMediator(binding.tabLayout,binding.viewPager){
            tab, pos->
            tab.setIcon(tabIconsId[pos])

        }.attach()
        //POPBACKSTACK
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