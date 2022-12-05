package com.sehun.android_pet_community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sehun.android_pet_community.Model.ChatModel
import com.sehun.android_pet_community.databinding.ActivityMessageBinding
import java.util.ArrayList

class MessageActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private  var mbinding : ActivityMessageBinding? = null
    private val binding get() = mbinding!!
    private val TAG = "experiment"
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

        destinationUid = intent.getStringExtra("destinationUid")
        firebaseFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        myUid = auth?.currentUser?.uid

        checkChatRoom()

        binding.navView.setCheckedItem(R.id.nav_user)
        binding.navView.setNavigationItemSelectedListener(this)
        binding.messageActivityButton.setOnClickListener {
            Log.d(TAG, chatRoomUid.toString())

            if (chatRoomUid == null) {
                val chatModel = ChatModel()
                chatModel.users!!.put(myUid!!, true)
                chatModel.users!!.put(destinationUid!!, true)

                val list1 : MutableList<String> = mutableListOf()
                list1.add(myUid!!)
                list1.add(destinationUid!!)
                chatModel.usersList = list1.toList()

                val list2 : MutableList<Map<String, String>> = mutableListOf()
                list2.add(hashMapOf(
                    myUid!! to binding.messageActivityEditText.text.toString()
                ))
                chatModel.comments = list2.toList()

                binding.messageActivityButton.isEnabled = true
                FirebaseFirestore.getInstance().collection("chatrooms").add(chatModel)
                    .addOnSuccessListener {
                        binding.messageActivityEditText.text.clear()
                        checkChatRoom()
                    }
            } else {
                val map = hashMapOf(
                    myUid!! to binding.messageActivityEditText.text.toString()
                )

                FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomUid!!)
                    .update("comments", FieldValue.arrayUnion(map))
                    .addOnSuccessListener {
                        binding.messageActivityEditText.text.clear()
                    }
            }


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



        }




    private fun checkChatRoom() {
        FirebaseFirestore.getInstance().collection("chatrooms")
            .get().addOnSuccessListener { snapshots ->
                for (snapshot in snapshots) {
                    val data = snapshot.toObject(ChatModel::class.java)
                    if (data.users!!.containsKey(myUid) && data.users!!.containsKey(destinationUid)) {
                        chatRoomUid = snapshot.id
                        binding.messageActivityButton.isEnabled = true
                        binding.messageActivityRecyclerview.layoutManager = LinearLayoutManager(this)
//                        binding.messageActivityRecyclerview.adapter = RecyclerViewAdapter()
                    }
                }
            }
    }

//    class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
//    {
//
//        var comments: List<ChatModel.Comment?>? = null
//        var userModel: User? = null
//        fun RecyclerViewAdapter() {
//            comments = ArrayList()
//            FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid)
//                .addListenerForSingleValueEvent(object : ValueEventListener() {
//                    fun onDataChange(dataSnapshot: DataSnapshot) {
//                        userModel = dataSnapshot.getValue(User::class.java)
//                        getMessageList()
//                    }
//
//                    fun onCancelled(databaseError: DatabaseError) {}
//                })
//        }
//    }


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

