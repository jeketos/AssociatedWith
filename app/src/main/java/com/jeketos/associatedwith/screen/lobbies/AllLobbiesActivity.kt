package com.jeketos.associatedwith.screen.lobbies

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.jeketos.associatedwith.R
import com.jeketos.associatedwith.screen.lobbies.privatelobbies.PrivateLobbiesFragment
import com.jeketos.associatedwith.screen.lobbies.publiclobbies.PublicLobbiesFragment
import com.jeketos.associatedwith.support.InjectorActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.screen_all_lobbies.*

class AllLobbiesActivity: InjectorActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.screen_all_lobbies)
        viewPager.adapter = LobbiesAdapter(this, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }

    class LobbiesAdapter(private val context: Context, fragmentManager: FragmentManager): FragmentPagerAdapter(fragmentManager){

        override fun getItem(position: Int): Fragment {
            return when(position){
                0 -> PublicLobbiesFragment.newInstance()
                else -> PrivateLobbiesFragment.newInstance()
            }
        }

        override fun getCount(): Int = 2

        override fun getPageTitle(position: Int): CharSequence =
                when(position){
                    0 -> context.getString(R.string.public_tab)
                    1 -> context.getString(R.string.private_tab)
                    else -> ""
        }

    }

}