package com.sehun.android_pet_community.Client

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.sehun.android_pet_community.Model.ManagerSaleDTO
import com.sehun.android_pet_community.R
import com.sehun.android_pet_community.WaitActivity
import kotlinx.android.synthetic.main.activity_client_manager_introduction.*

class ClientManagerIntroduction : AppCompatActivity() {
    var firebaseFirestore : FirebaseFirestore? = null
    var address : String? = null
    var title : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_manager_introduction)

        firebaseFirestore = FirebaseFirestore.getInstance()
        val destinationUid : String? = intent.getStringExtra("destinationUid")

        val docRef = firebaseFirestore?.collection("manager_sale_info")?.document(destinationUid!!)
        docRef?.get()?.addOnSuccessListener {
            document ->
            Manager_introduction_Title.text = document["salestitle"].toString()
            Manager_introduction_Address.text = document["address"].toString()
            address = document["memo"].toString()
            title = document["address"].toString()
            Manager_introduction_memo.text = document["memo"].toString()

            Glide.with(Manager_introduction_image.context).load(document["uri"]).into(Manager_introduction_image)

        }

        Manager_introduction_Btn.setOnClickListener {
            val  intent : Intent = Intent(this, WaitActivity::class.java)
            intent.putExtra("destinationUid", destinationUid)
            intent.putExtra("address", address)
            intent.putExtra("salestitle", title)
            startActivity(intent)
        }
    }
}