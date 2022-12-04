package com.sehun.android_pet_community.Client

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sehun.android_pet_community.R
import com.sehun.android_pet_community.databinding.FragmentClient4Binding
import com.sehun.android_pet_community.databinding.FragmentManager4Binding

class ClientFragment4 : Fragment() {
    private  var mbinding : FragmentClient4Binding? = null
    private val binding get() = mbinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mbinding = FragmentClient4Binding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        // onDestroyView 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mbinding = null
        super.onDestroyView()
    }


}