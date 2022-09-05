package com.sehun.android_pet_community.Manager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sehun.android_pet_community.Model.ClientPetDTO
import com.sehun.android_pet_community.Model.ManagerDTO
import com.sehun.android_pet_community.Model.ManagerSaleDTO
import com.sehun.android_pet_community.R
import kotlinx.android.synthetic.main.activity_client_input2.*
import kotlinx.android.synthetic.main.activity_client_input2.SecondInput_PetName
import kotlinx.android.synthetic.main.activity_manager_input2.*

class ManagerInput2Activity : AppCompatActivity() {
    var firestore : FirebaseFirestore? = null
    var managerSaleDTO : ManagerSaleDTO? = null
    var auth : FirebaseAuth? = null
    var uid : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_input2)
        init()
        ManagerFragment1_okay.setOnClickListener {
            SecondDataInput()
            finish()
        }

    }

    private fun SecondDataInput() {
        managerSaleDTO = ManagerSaleDTO()
        managerSaleDTO!!.salestitle = ManagerFragment1_SalesTitle.text.toString()
        managerSaleDTO!!.address = ManagerFragment1_address2.text.toString()
        managerSaleDTO!!.hashtag1 = ManagerFragment1_hashtag1.text.toString()
        managerSaleDTO!!.hashtag2 = ManagerFragment1_hashtag2.text.toString()
        managerSaleDTO!!.hashtag3 = ManagerFragment1_hashtag3.text.toString()
        managerSaleDTO!!.memo = ManagerFragment1_memo.text.toString()
        managerSaleDTO!!.uid = auth?.currentUser?.uid
        firestore?.collection("manager_sale_info")?.document(uid!!)?.set(managerSaleDTO!!)?.addOnSuccessListener {
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