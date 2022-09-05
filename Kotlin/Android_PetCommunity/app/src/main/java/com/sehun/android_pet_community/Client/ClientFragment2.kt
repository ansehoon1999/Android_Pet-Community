package com.sehun.android_pet_community.Client

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
import kotlinx.android.synthetic.main.fragment_client2.view.*
import kotlinx.android.synthetic.main.list_item.view.*


class ClientFragment2 : Fragment() {
    var firestore : FirebaseFirestore? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_client2, container, false)

        firestore = FirebaseFirestore.getInstance()
        // ListView보다 재사용성이 훨씬 좋다. 가로, 세로, Grid 형식 모두 지원
        // viewholder : 각각의 뷰를 보관하는 Holder 객체
        // LayoutManager : 아이템 배치를 담당 (LinearLayoutManager -  가로/세로)
        // 뷰를 뷰의 데이터에 바인딩


        view.recyclerView.adapter = RecyclerViewAdapter()
        view.recyclerView.layoutManager = LinearLayoutManager(activity)

        return view
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var managerSaleDTO : ArrayList<ManagerSaleDTO> = arrayListOf()

        init {
            firestore?.collection("manager_sale_info")?.addSnapshotListener {
                querySnapshot, firebaseeFirestoreException ->

                managerSaleDTO.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(ManagerSaleDTO::class.java)
                    managerSaleDTO.add(item!!)
                }

                notifyDataSetChanged()
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView
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