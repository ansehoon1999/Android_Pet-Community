package com.sehun.android_pet_community.Client

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sehun.android_pet_community.Model.ClientDTO
import com.sehun.android_pet_community.Model.ClientPetDTO
import com.sehun.android_pet_community.R
import kotlinx.android.synthetic.main.activity_client_input2.*

class ClientInput2Activity : AppCompatActivity() {
    var firestore : FirebaseFirestore? = null
    var clientPetDTO : ClientPetDTO? = null
    var auth : FirebaseAuth? = null
    var uid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_input2)

        init()
        SecondInput_OkayBtn.setOnClickListener {
            SecondDataInput()
            finish()
        }

    }

    private fun SecondDataInput() {
        clientPetDTO = ClientPetDTO()
        clientPetDTO!!.petName = SecondInput_PetName.text.toString()
        clientPetDTO!!.petAge = SecondInput_PetAge.text.toString()
        clientPetDTO!!.petType = SecondInput_PetType.text.toString()
        clientPetDTO!!.allergy = SecondInput_Allergy.text.toString()
        clientPetDTO!!.uid = auth?.currentUser?.uid
        firestore?.collection("user_pet_info")?.document(uid!!)?.set(clientPetDTO!!)?.addOnSuccessListener {
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