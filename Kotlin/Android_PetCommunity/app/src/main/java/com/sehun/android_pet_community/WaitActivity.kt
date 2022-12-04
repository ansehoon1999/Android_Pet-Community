package com.sehun.android_pet_community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sehun.android_pet_community.Model.ChatModel
import com.sehun.android_pet_community.Model.ReservationModel
import com.sehun.android_pet_community.databinding.ActivityWaitBinding


class WaitActivity : AppCompatActivity() {
    private  var mbinding : ActivityWaitBinding? = null
    private val binding get() = mbinding!!
    private val myUid = FirebaseAuth.getInstance().currentUser!!.uid

    var firebaseFirestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityWaitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val destinationUid = intent.getStringExtra("destinationUid")
        val ReservationTime = intent.getStringExtra("ReservationTime")
        val address = intent.getStringExtra("address")
        val salestitle = intent.getStringExtra("salestitle")

        binding.waitReservationTime.text = ReservationTime
        binding.waitAddress.text = address
        binding.waitSalestitle.text = salestitle

        val messageIntent = Intent(this, MessageActivity::class.java)
        messageIntent.putExtra("destinationUid", destinationUid)
        messageIntent.putExtra("Title", salestitle)
        messageIntent.putExtra("ReservationTime", destinationUid)
        messageIntent.putExtra("address", address)
        binding.waitOkay.setOnClickListener {
            val reservationModel : ReservationModel = ReservationModel()
            reservationModel.myUiddestinationUid = myUid + destinationUid
            reservationModel.destinationUidmyUid = destinationUid + myUid
            reservationModel.Salestitle = salestitle
            reservationModel.address = address
            reservationModel.date = ReservationTime
            reservationModel.client = myUid
            reservationModel.manager = destinationUid

            FirebaseFirestore.getInstance().collection("ReservationInfo").document(myUid + destinationUid)
                .set(reservationModel)

//            val chatModel = ChatModel()
//            chatModel.users!!.put(myUid, true)
//            chatModel.users!!.put(destinationUid!!, true)
//
//
////            FirebaseFirestore.getInstance().collection("chatrooms").add(chatModel)
//            FirebaseFirestore.getInstance().collection("chatrooms").document("sss").set(chatModel)

            startActivity(messageIntent)
        }
    }

    override fun onDestroy() {
        // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mbinding = null
        super.onDestroy()
    }

}