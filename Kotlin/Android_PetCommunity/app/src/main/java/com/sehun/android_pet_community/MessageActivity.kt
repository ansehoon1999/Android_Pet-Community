package com.sehun.android_pet_community

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.sehun.android_pet_community.Model.ChatModel
import com.sehun.android_pet_community.Model.ReservationModel
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


        Log.d("experiment", "???")
        binding.navView.setCheckedItem(R.id.nav_user)
        binding.navView.setNavigationItemSelectedListener(this)
        binding.messageActivityButton.setOnClickListener {


            val map = hashMapOf(
                myUid!! to binding.messageActivityEditText.text.toString()
            )

            FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomUid!!)
                .update("comments", FieldValue.arrayUnion(map))
                .addOnSuccessListener {
                    binding.messageActivityEditText.text.clear()
                    checkChatRoom()
                }
        }

        checkChatRoom()
    }




    private fun checkChatRoom() {
        FirebaseFirestore.getInstance().collection("chatrooms")
            .get().addOnSuccessListener { snapshots ->
                for (snapshot in snapshots) {
                    val data = snapshot.toObject(ChatModel::class.java)
                    if ((data.users.contains(hashMapOf(myUid!! to true)) || (data.users.contains(hashMapOf(myUid!! to false))))
                        && (data.users.contains(hashMapOf(destinationUid!! to true)) || (data.users.contains(hashMapOf(destinationUid!! to false)))))
                        {
                            chatRoomUid = snapshot.id
                            binding.messageActivityButton.isEnabled = true
                            binding.messageActivityRecyclerview.layoutManager = LinearLayoutManager(this)
                            Log.d(TAG, chatRoomUid.toString())
                            binding.messageActivityRecyclerview.adapter = RecyclerViewAdapter(chatRoomUid)
                    }
                }
            }
    }

    inner class RecyclerViewAdapter(chatRoomUid : String?): RecyclerView.Adapter<RecyclerView.ViewHolder>()
    {
        var comments : List<Map<String, String>> = listOf()

        init {
            if (chatRoomUid != null) {
                FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomUid)
                    .get().addOnSuccessListener { document ->
                        comments = document.toObject(ChatModel::class.java)!!.comments

                        notifyDataSetChanged()
                    }
            }
        }

        inner class MessageViewHolder(view: View) :
            RecyclerView.ViewHolder(view) {
            var textView_message: TextView
            var linearLayout_destination: LinearLayout
            var linearLayout_main: LinearLayout

            init {
                textView_message =
                    view.findViewById<View>(R.id.messageItem_textView_message) as TextView
                linearLayout_destination =
                    view.findViewById<View>(R.id.messageItem_linearlayout_destination) as LinearLayout
                linearLayout_main =
                    view.findViewById<View>(R.id.message_linearlayout_main) as LinearLayout
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
            return MessageViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val messageViewHolder: MessageViewHolder = holder as MessageViewHolder
            //내가 보낸 메시지
            if (comments[position].containsKey(myUid)) { //내 uid {
                messageViewHolder.textView_message.setText(comments[position].getValue(myUid!!))
                messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE)
                messageViewHolder.textView_message.setTextSize(10f)
                messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT)
            } else {
                messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE)
                messageViewHolder.textView_message.setText(comments[position].getValue(destinationUid!!))
                messageViewHolder.textView_message.setTextSize(10f)
                messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT)


            }
        }

        override fun getItemCount(): Int {
            return comments.size
        }



    }


    override fun onNavigationItemSelected(item : MenuItem) : Boolean {
        when(item.itemId) {
            R.id.nav_yes -> {
                FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomUid!!)
                    .update("users", FieldValue.arrayRemove(hashMapOf(myUid!! to false)))
                FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomUid!!)
                    .update("users", FieldValue.arrayUnion(hashMapOf(myUid!! to true)))

                binding.messageActivityMatching.text = "Matching Now"
                binding.messageActivityEditText.isEnabled =true
                binding.messageActivityButton.isEnabled = true
            }
            R.id.nav_no -> {
                binding.messageActivityMatching.text = "Matching Decline"
                binding.messageActivityEditText.isEnabled =false
                binding.messageActivityButton.isEnabled = false
            }

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

