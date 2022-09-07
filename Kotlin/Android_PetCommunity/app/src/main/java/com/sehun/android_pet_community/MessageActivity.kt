package com.sehun.android_pet_community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sehun.android_pet_community.Model.ChatModel
import kotlinx.android.synthetic.main.activity_message.*

class MessageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var firebaseFirestore: FirebaseFirestore? = null
    var destinationUid: String? = null
    var auth: FirebaseAuth? = null
    var uid: String? = null
    var chatRoomUid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        destinationUid = intent.getStringExtra("destinationUid")
        firebaseFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        uid = auth?.currentUser?.uid

        nav_view.setCheckedItem(R.id.nav_user)
        nav_view.setNavigationItemSelectedListener(this)
        messageActivity_button.setOnClickListener {
            val chatModel: ChatModel? = ChatModel()
            chatModel?.user?.put(uid!!, true)
            chatModel?.user?.put(destinationUid!!, true)
            if (chatRoomUid == null) {
                messageActivity_button.setEnabled(false)
                firebaseFirestore?.collection("chatrooms")?.document()?.set(chatModel!!)
                    ?.addOnSuccessListener {
                        checkChatRoom()
                        Log.d("thisss", "DocumentSnapshot successfully written!")
                    }
                    ?.addOnFailureListener { e -> Log.w("thisss", "Error writing document", e) }
            } else {
                var comment: ChatModel.Comment? = ChatModel.Comment()
                comment!!.uid = uid
                comment!!.message = messageActivity_editText.text.toString()

                firebaseFirestore?.collection("chatrooms")?.document(chatRoomUid!!)
                    ?.collection("comments")?.document()?.set(comment)
                    ?.addOnSuccessListener {
                        messageActivity_editText.setText("")
                    }
            }

            checkChatRoom()

        }


    }

    private fun checkChatRoom() {
        firebaseFirestore?.collection("chatrooms")?.document()
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

