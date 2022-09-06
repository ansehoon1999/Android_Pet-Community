package com.sehun.android_pet_community.Client

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
import com.sehun.android_pet_community.Model.ClientPetDTO
import com.sehun.android_pet_community.R
import kotlinx.android.synthetic.main.activity_client_input1.*
import kotlinx.android.synthetic.main.activity_client_input2.*
import java.text.SimpleDateFormat
import java.util.*

class ClientInput2Activity : AppCompatActivity() {
    var storage : FirebaseStorage? = null
    var firestore : FirebaseFirestore? = null
    var clientPetDTO : ClientPetDTO? = null
    var auth : FirebaseAuth? = null
    var uid : String? = null
    var PICK_IMAGE_FROM_ALBUM = 1
    var photoUri : Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_input2)

        init()

        SecondInput_Petprofile.setOnClickListener {
            var photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        }
        SecondInput_OkayBtn.setOnClickListener {
            SecondDataInput()
            finish()
        }

    }

    private fun SecondDataInput() {

        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var imageFileName = "IMAGE_" + timestamp + "_.png"
        var storageRef = storage?.reference?.child("user_pet_images")?.child(imageFileName)
        storageRef?.putFile(photoUri!!)?.continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->


            clientPetDTO = ClientPetDTO()
            clientPetDTO!!.petName = SecondInput_PetName.text.toString()
            clientPetDTO!!.petAge = SecondInput_PetAge.text.toString()
            clientPetDTO!!.petType = SecondInput_PetType.text.toString()
            clientPetDTO!!.allergy = SecondInput_Allergy.text.toString()
            clientPetDTO!!.uid = auth?.currentUser?.uid
            clientPetDTO!!.uri = uri?.toString()


            firestore?.collection("user_pet_info")?.document(uid!!)?.set(clientPetDTO!!)
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
                SecondInput_Petprofile.setImageURI(photoUri)
            } else {
                finish()
            }
        }
    }

}