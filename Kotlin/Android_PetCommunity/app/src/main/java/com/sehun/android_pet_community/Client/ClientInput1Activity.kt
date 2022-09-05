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
    var username : String? = null
    var address : String? = null
    var job : String? = null
    var age : String? = null
    var firestore : FirebaseFirestore? = null
    var clientDTO : ClientDTO? = null

    var auth : FirebaseAuth? = null
    var uid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_input1)


        firestore = FirebaseFirestore.getInstance()



//        init()

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


        firestore!!.collection("user_info")
            .add(clientDTO!!)
            .addOnSuccessListener {
                Toast.makeText(this, "데이터가 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                exception ->
                Log.w("MainActivity", "Error getting documents: $exception")
            }

    }

    private fun init() {
        username = FirstInput_UserName.text.toString()
        address = FirstInput_Address.text.toString()
        job = FirstInput_Job.text.toString()
        age = FirstInput_Age.text.toString()
        uid = auth?.currentUser?.uid
        firestore = FirebaseFirestore.getInstance()
    }
}