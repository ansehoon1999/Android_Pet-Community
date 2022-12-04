package com.sehun.android_pet_community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sehun.android_pet_community.Model.ChatModel
import com.sehun.android_pet_community.databinding.ActivityMessageBinding

class MessageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private  var mbinding : ActivityMessageBinding? = null
    private val binding get() = mbinding!!

    var firebaseFirestore: FirebaseFirestore? = null
    var DDestinationUid: String? = null
    var destinationUid: String? = null
    var auth: FirebaseAuth? = null
    var myUid: String? = null
    var chatRoomUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkChatRoom()
        destinationUid = intent.getStringExtra("destinationUid")
        firebaseFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        myUid = auth?.currentUser?.uid

        binding.navView.setCheckedItem(R.id.nav_user)
        binding.navView.setNavigationItemSelectedListener(this)
        binding.messageActivityButton.setOnClickListener {
            val chatModel = ChatModel()
            chatModel.users!!.put(myUid!!, true)
            chatModel.users!!.put(destinationUid!!, true)

            FirebaseFirestore.getInstance().collection("chatrooms").add(chatModel)


        }


//            if (chatRoomUid == null) {
//                binding.messageActivityButton.setEnabled(false)
//                firebaseFirestore?.collection("chatrooms")?.document(uid!!)
//                    ?.collection(uid!!)!!.document(uid!! + destinationUid).set(chatModel!!)
//                    ?.addOnSuccessListener {
//                        checkChatRoom()
//                        Log.d("thisss", "DocumentSnapshot successfully written!")
//                    }
//                    ?.addOnFailureListener { e -> Log.w("thisss", "Error writing document", e) }
//            } else {
//                var comment: ChatModel.Comment? = ChatModel.Comment()
//                comment!!.uid = uid
//                comment!!.message = binding.messageActivityEditText.text.toString()
//
//                firebaseFirestore?.collection("chatrooms")?.document(chatRoomUid!!)
//                    ?.collection("comments")?.document()?.set(comment)
//                    ?.addOnSuccessListener {
//                        binding.messageActivityEditText.setText("")
//                    }


            checkChatRoom()

        }




    private fun checkChatRoom() {
        FirebaseFirestore.getInstance()?.collection("chatrooms")
            .get()!!.addOnSuccessListener { snapshots ->
                for (snapshot in snapshots) {
                    val data = snapshot.toObject(ChatModel::class.java)
                    if (data.users!!.containsKey(myUid) && data.users!!.containsKey(destinationUid)) {
                        chatRoomUid = snapshots.metadata.toString()

                        Log.d("experiment", chatRoomUid.toString())

                    }
                }
            }
    }


    override fun onNavigationItemSelected(item : MenuItem) : Boolean {
        when(item.itemId) {
            R.id.nav_gps -> {
                val intent: Intent = Intent(this, MapActivity::class.java)
                intent.putExtra("destinationUid", destinationUid)
                startActivity(intent)
                return true
            }
        }
        return false
    }
}

