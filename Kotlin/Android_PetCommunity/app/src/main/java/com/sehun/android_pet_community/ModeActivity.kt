package com.sehun.android_pet_community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.common.api.Api
import com.sehun.android_pet_community.Client.Client
import com.sehun.android_pet_community.Manager.Manager
import kotlinx.android.synthetic.main.activity_mode.*

class ModeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mode)

        Managerbtn.setOnClickListener {
            val intent = Intent(this, Manager::class.java)
            startActivity(intent)
        }


        Clientbtn.setOnClickListener {
            val intent = Intent(this, Client::class.java)
            startActivity(intent)
        }

    }



}