package com.sehun.android_pet_community.Manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.sehun.android_pet_community.Adapter.ManagerAdapter
import com.sehun.android_pet_community.R
import kotlinx.android.synthetic.main.activity_client.*
import android.view.View
import android.widget.RelativeLayout
import com.google.android.material.bottomnavigation.BottomNavigationView


class Manager : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)

        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.navigation_one
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId) {
            R.id.navigation_one -> {
                var managerFragment1 : ManagerFragment1? = ManagerFragment1()
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, managerFragment1!!).commit()
                return true
            }
            R.id.navigation_two -> {
                var managerFragment2 : ManagerFragment2? = ManagerFragment2()
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, managerFragment2!!).commit()
                return true
            }
            R.id.navigation_three -> {
                var managerFragment3 : ManagerFragment3? = ManagerFragment3()
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, managerFragment3!!).commit()
                return true
            }
            R.id.navigation_four -> {
                var managerFragment4 : ManagerFragment4? = ManagerFragment4()
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, managerFragment4!!).commit()
                return true
            }
        }
        return false
    }


}