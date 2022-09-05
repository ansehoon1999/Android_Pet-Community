package com.sehun.android_pet_community.Client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sehun.android_pet_community.Adapter.ClientRecyclerviewAdapter
import com.sehun.android_pet_community.Model.ClientDTO
import com.sehun.android_pet_community.R
import kotlinx.android.synthetic.main.fragment_client2.view.*


class ClientFragment2 : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_client2, container, false)

        // viewholder : 각 항목의 뷰를 재활용하기 위해 보관하는 클라스
        // LayoutManager : 목록을 어떻게 배치할지 결정
        // 뷰를 뷰의 데이터에 바인딩


        //view.recyclerView.adapter = ClientRecyclerviewAdapter()
        //view.recyclerView.layoutManager = LinearLayoutManager(activity)

        return view
    }
}