package com.sehun.android_pet_community.Adapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.sehun.android_pet_community.Manager.*


class ManagerAdapter(fm: FragmentManager, val fragmentCount : Int) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return ManagerFragment1()
            1 -> return ManagerFragment2()
            2 -> return ManagerFragment3()
            3 -> return ManagerFragment4()
            else -> {throw IllegalStateException("$position is illegal") }
        }
    }

    override fun getCount(): Int = fragmentCount

}