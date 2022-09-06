package com.sehun.android_pet_community.Manager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.sehun.android_pet_community.Model.ClientDTO
import com.sehun.android_pet_community.Model.ManagerDTO
import com.sehun.android_pet_community.R
import kotlinx.android.synthetic.main.activity_client_input1.*
import kotlinx.android.synthetic.main.activity_client_input1.FirstInput_Address
import kotlinx.android.synthetic.main.activity_client_input1.FirstInput_Age
import kotlinx.android.synthetic.main.activity_client_input1.FirstInput_Job
import kotlinx.android.synthetic.main.activity_client_input1.FirstInput_UserName
import kotlinx.android.synthetic.main.activity_manager_input1.*
import java.text.SimpleDateFormat
import java.util.*

class ManagerInput1Activity : AppCompatActivity() {
    var firestore : FirebaseFirestore? = null
    var storage : FirebaseStorage? = null

    var managerDTO : ManagerDTO? = null
    var auth : FirebaseAuth? = null
    var uid : String? = null
    var PICK_IMAGE_FROM_ALBUM = 0
    var photoUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager_input1)

        init()

        ManagerFragment1_Profile.setOnClickListener {
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        }

        ManagerFragment1_next.setOnClickListener {
            FirstDataInput()
            finish()
        }
    }

    private fun FirstDataInput() {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"
        var storageRef = storage?.reference?.child("manager_images")?.child(imageFileName)
        storageRef?.putFile(photoUri!!)?.continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->


            managerDTO = ManagerDTO()
            managerDTO!!.userName = ManagerFragment1_userName.text.toString()
            managerDTO!!.address = ManagerFragment1_address.text.toString()
            managerDTO!!.job = ManagerFragment1_job.text.toString()
            managerDTO!!.age = ManagerFragment1_age.text.toString()
            managerDTO!!.uid = auth?.currentUser?.uid


            firestore?.collection("manager_info")?.document(uid!!)?.set(managerDTO!!)
                ?.addOnSuccessListener {
                    Toast.makeText(this, "데이터가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener { exception ->
                Log.w("MainActivity", "Error getting documents: $exception")
            }
        }
    }

    private fun init() {
        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        uid = auth?.currentUser?.uid
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_FROM_ALBUM) {
            if (resultCode == Activity.RESULT_OK)
            {
                photoUri = data?.data
                ManagerFragment1_Profile.setImageURI(photoUri)
            } else {
                finish()
            }
        }
    }
}