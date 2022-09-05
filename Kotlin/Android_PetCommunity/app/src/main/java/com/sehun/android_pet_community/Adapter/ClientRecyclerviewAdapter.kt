package com.sehun.android_pet_community.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.sehun.android_pet_community.Model.ClientDTO
import com.sehun.android_pet_community.R
import android.view.View

class ClientRecyclerviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var clientDTOList : ArrayList<ClientDTO> = arrayListOf()

    init {
        var uid = FirebaseAuth.getInstance().currentUser?.uid


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)


        return CustomViewHolder(view)
    }

    inner class CustomViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {


    }

    override fun getItemCount(): Int { return clientDTOList.size }


}