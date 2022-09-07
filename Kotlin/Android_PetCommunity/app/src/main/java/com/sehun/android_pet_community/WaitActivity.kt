package com.sehun.android_pet_community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_client_manager_introduction.*
import kotlinx.android.synthetic.main.activity_client_manager_introduction.Manager_introduction_Title
import kotlinx.android.synthetic.main.activity_wait.*

class WaitActivity : AppCompatActivity() {
    var firebaseFirestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wait)

        firebaseFirestore = FirebaseFirestore.getInstance()
        val destinationUid: String? = intent.getStringExtra("destinationUid")

        val docRef = firebaseFirestore?.collection("manager_sale_info")?.document(destinationUid!!)
        docRef?.get()?.addOnSuccessListener { document ->
            wait_Salestitle.text = document["salestitle"].toString()
            wait_address.text = document["address"].toString()
            wait_ReservationTime.text = document["memo"].toString()

        }

        waitOkay.setOnClickListener {
            val intent : Intent? = Intent(this, MessageActivity::class.java)
            intent?.putExtra("destinationUid", destinationUid)
            startActivity(intent)
        }

    }



}