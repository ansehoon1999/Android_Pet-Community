package com.sehun.android_pet_community.Client

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.sehun.android_pet_community.Model.ManagerPossibleTime
import com.sehun.android_pet_community.WaitActivity
import com.sehun.android_pet_community.databinding.ActivityClientManagerIntroductionBinding

class ClientManagerIntroductionActivity : AppCompatActivity() {
    private  var mbinding :ActivityClientManagerIntroductionBinding ? = null
    private val binding get() = mbinding!!
    var firebaseFirestore : FirebaseFirestore? = null
    var address : String? = null
    var title : String? = null
    private lateinit var selected : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mbinding = ActivityClientManagerIntroductionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseFirestore = FirebaseFirestore.getInstance()
        val destinationUid : String? = intent.getStringExtra("destinationUid")

        val docRef = firebaseFirestore?.collection("manager_sale_info")?.document(destinationUid!!)
        docRef?.get()?.addOnSuccessListener {
            document ->
            binding.ManagerIntroductionTitle.text = document["salestitle"].toString()
            binding.ManagerIntroductionAddress.text = document["address"].toString()
            address = document["memo"].toString()
            title = document["address"].toString()
            binding.ManagerIntroductionMemo.text = document["memo"].toString()

            Glide.with(binding.ManagerIntroductionImage.context).load(document["uri"]).into(binding.ManagerIntroductionImage)

        }


        val docRef2 = firebaseFirestore?.collection("ManagerPossibleTime")?.document(destinationUid!!)!!
        docRef2.get().addOnSuccessListener { document ->
            val data : ManagerPossibleTime? = document.toObject(ManagerPossibleTime::class.java)
            val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, data!!.time.values.toList())
            binding.ManagerIntroductionSpinner.adapter = spinnerAdapter
            binding.ManagerIntroductionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selected = data!!.time.values.toList().get(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }

        }



        binding.ManagerIntroductionBtn.setOnClickListener {
            Toast.makeText(applicationContext, selected, Toast.LENGTH_SHORT).show()

            val  intent : Intent = Intent(this, WaitActivity::class.java)
            intent.putExtra("destinationUid", destinationUid)
            intent.putExtra("ReservationTime", selected)
            intent.putExtra("address", address)
            intent.putExtra("salestitle", title)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        // onDestroy 에서 binding class 인스턴스 참조를 정리해주어야 한다.
        mbinding = null
        super.onDestroy()
    }
}