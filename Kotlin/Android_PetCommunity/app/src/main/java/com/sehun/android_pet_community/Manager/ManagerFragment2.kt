package com.sehun.android_pet_community.Manager

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.sehun.android_pet_community.Model.ManagerSaleDTO
import com.sehun.android_pet_community.R
import com.sehun.android_pet_community.ReservationActivity
import com.sehun.android_pet_community.databinding.ActivityClientManagerIntroductionBinding
import com.sehun.android_pet_community.databinding.FragmentManager2Binding
import kotlinx.android.synthetic.main.list_item.view.*


class ManagerFragment2 : Fragment() {
    private  var mbinding : FragmentManager2Binding? = null
    private val binding get() = mbinding!!

    var firestore : FirebaseFirestore? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mbinding = FragmentManager2Binding.inflate(inflater, container, false)

        firestore = FirebaseFirestore.getInstance()

        binding.managerRecyclerView.adapter = RecyclerViewAdapter()
        binding.managerRecyclerView.layoutManager = LinearLayoutManager(activity)
        binding.fab.setOnClickListener {
            val intent = Intent(requireActivity(), ReservationActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onDestroyView() {
        // onDestroyView 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mbinding = null
        super.onDestroyView()
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var managerSaleDTO : ArrayList<ManagerSaleDTO> = arrayListOf()

        init {
            firestore?.collection("manager_sale_info")?.addSnapshotListener {
                querySnapshot, firebaseFirestoreException ->

                managerSaleDTO.clear()


            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

            return ViewHolder(view)
        }

        inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {}

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = holder.itemView
            viewHolder.list_SalesTitle.text = managerSaleDTO[position].salestitle
            viewHolder.list_SalesAddress.text = managerSaleDTO[position].address
            viewHolder.list_hash1.text = managerSaleDTO[position].hashtag1
            viewHolder.list_hash2.text = managerSaleDTO[position].hashtag2
            viewHolder.list_hash3.text = managerSaleDTO[position].hashtag3

        }

        override fun getItemCount(): Int {
            return managerSaleDTO.size
        }


    }












}