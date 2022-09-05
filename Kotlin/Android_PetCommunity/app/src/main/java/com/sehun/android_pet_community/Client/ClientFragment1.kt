package com.sehun.android_pet_community.Client

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sehun.android_pet_community.R
import kotlinx.android.synthetic.main.fragment_client1.view.*


class ClientFragment1 : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_client1, container, false)

        //activity: fragment아래에 깔려있는 activity를 호출함.
        view.ClientFragment1_userInfoBtn.setOnClickListener {
            val intent = Intent(activity, ClientInput1Activity::class.java)
            startActivity(intent)
        }

        view.ClientFragment1_userInfoChangeBtn.setOnClickListener {
            val intent = Intent(activity, ClientInput2Activity::class.java)
            startActivity(intent)
        }

        return view
    }


}