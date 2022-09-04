package com.sehun.android_pet_community.Client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.sehun.android_pet_community.Adapter.ClientAdapter
import com.sehun.android_pet_community.R
import kotlinx.android.synthetic.main.activity_client.*
import android.view.View
import android.widget.RelativeLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

class Client : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client)

        navigationView.setOnNavigationItemSelectedListener(this)
        navigationView.selectedItemId = R.id.navigation_one

    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when(p0.itemId) {
            R.id.navigation_one -> {
                var clientFragment1 : ClientFragment1? = ClientFragment1()
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, clientFragment1!!).commit()
                return true
            }
            R.id.navigation_two -> {
                var clientFragment2 : ClientFragment2? = ClientFragment2()
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, clientFragment2!!).commit()
                return true
            }
            R.id.navigation_three -> {
                var clientFragment3 : ClientFragment3? = ClientFragment3()
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, clientFragment3!!).commit()
                return true
            }

            R.id.navigation_four -> {
                var clientFragment4 : ClientFragment4? = ClientFragment4()
                supportFragmentManager.beginTransaction().replace(R.id.frameLayout, clientFragment4!!).commit()
                return true
            }

        }
        return false
    }


}