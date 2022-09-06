package com.sehun.android_pet_community.Client

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.sehun.android_pet_community.Model.ClientDTO
import com.sehun.android_pet_community.R
import kotlinx.android.synthetic.main.activity_client_input1.*
import java.text.SimpleDateFormat
import java.util.*

class ClientInput1Activity : AppCompatActivity() {
    var storage : FirebaseStorage? = null
    var firestore : FirebaseFirestore? = null
    var clientDTO : ClientDTO? = null
    var auth : FirebaseAuth? = null
    var uid : String? = null
    var PICK_IMAGE_FROM_ALBUM = 0
    var photoUri : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_input1)
        init()
        FirstInput_Profile.setOnClickListener {
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)
        }
        FirstInput_NextBtn.setOnClickListener {
            FirstDataInput()
            finish()
        }
    }

    private fun FirstDataInput() {

        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"
        var storageRef = storage?.reference?.child("user_images")?.child(imageFileName)
        storageRef?.putFile(photoUri!!)?.continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->

            clientDTO = ClientDTO()
            clientDTO!!.userName = FirstInput_UserName.text.toString()
            clientDTO!!.address = FirstInput_Address.text.toString()
            clientDTO!!.job = FirstInput_Job.text.toString()
            clientDTO!!.age = FirstInput_Age.text.toString()
            clientDTO!!.uid = auth?.currentUser?.uid
            clientDTO!!.uri = uri?.toString()

            firestore?.collection("user_info")?.document(uid!!)?.set(clientDTO!!)
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
                FirstInput_Profile.setImageURI(photoUri)
            } else {
                finish()
            }
        }
    }




}