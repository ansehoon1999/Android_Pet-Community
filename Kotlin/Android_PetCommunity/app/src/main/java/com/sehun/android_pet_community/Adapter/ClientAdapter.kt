package com.sehun.android_pet_community.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sehun.android_pet_community.Client.*

// adapter: 보여지는 view와 view에 올리는 data를 연결시켜주는 역할
class ClientAdapter(fm : FragmentManager, val fragmentCount : Int) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return ClientFragment1()
            1 -> return ClientFragment2()
            2 -> return ClientFragment3()
            3 -> return ClientFragment4()
            else -> {throw IllegalStateException("$position is illegal") }
        }
    }

    override fun getCount(): Int = fragmentCount

}