package com.sehun.android_pet_community.Client

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sehun.android_pet_community.Model.ClientDTO
import com.sehun.android_pet_community.R
import kotlinx.android.synthetic.main.activity_client_input1.*

class ClientInput1Activity : AppCompatActivity() {
    var firestore : FirebaseFirestore? = null
    var clientDTO : ClientDTO? = null
    var auth : FirebaseAuth? = null
    var uid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_input1)
        init()
        FirstInput_NextBtn.setOnClickListener {
            FirstDataInput()
            finish()
        }
    }

    private fun FirstDataInput() {
         clientDTO = ClientDTO()
        clientDTO!!.userName = FirstInput_UserName.text.toString()
        clientDTO!!.address = FirstInput_Address.text.toString()
        clientDTO!!.job = FirstInput_Job.text.toString()
        clientDTO!!.age = FirstInput_Age.text.toString()
        clientDTO!!.uid = auth?.currentUser?.uid


        firestore?.collection("user_info")?.document(uid!!)?.set(clientDTO!!)?.addOnSuccessListener {
            Toast.makeText(this, "데이터가 추가되었습니다.", Toast.LENGTH_SHORT).show()
        }?.addOnFailureListener {
                exception ->
            Log.w("MainActivity", "Error getting documents: $exception")
        }

    }

    private fun init() {
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        uid = auth?.currentUser?.uid
    }
}