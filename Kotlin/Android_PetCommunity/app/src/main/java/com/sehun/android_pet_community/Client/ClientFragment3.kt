package com.sehun.android_pet_community.Client

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sehun.android_pet_community.MessageActivity
import com.sehun.android_pet_community.Model.ChatModel
import com.sehun.android_pet_community.R
import kotlinx.android.synthetic.main.fragment_client3.view.*

import java.util.*

class ClientFragment3 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_client3, container, false)

        view.chatfragment_recyclerview.adapter = ChatRecyclerViewAdapter(requireActivity())
        view.chatfragment_recyclerview.layoutManager = LinearLayoutManager(inflater.getContext())

        return view

    }

    inner class ChatRecyclerViewAdapter(context: Context) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        var firebaseStorage: FirebaseStorage? = null
        var storageReference: StorageReference? = null
        val chatModels: MutableList<ChatModel> = mutableListOf()
        var uid = FirebaseAuth.getInstance().currentUser!!.uid

        val destinationUsers = ArrayList<String?>()

        init {

            FirebaseFirestore.getInstance().collection("chatrooms").get()
                .addOnSuccessListener { querySnapshot ->
                    for (snapshot in querySnapshot!!) {
                        val data: ChatModel = snapshot.toObject(ChatModel::class.java)
                        chatModels.add(data!!)
                    }
                    notifyDataSetChanged()
                }
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var imageView: ImageView
            var textView_title: TextView
            var textView_last_message: TextView

            init {
                imageView = view.findViewById<View>(R.id.chatitem_imageview) as ImageView
                textView_title = view.findViewById<View>(R.id.chatitem_textview_title) as TextView
                textView_last_message =
                    view.findViewById<View>(R.id.chatitem_textview_lastMessage) as TextView
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
            return CustomViewHolder(view)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val customViewHolder: CustomViewHolder = holder as CustomViewHolder
            var destinationUid: String? = null
            //일일 챗방에 있는 유저를 채크한다
            for (user in chatModels[position].usersList) {
                if (user != uid) {
                    destinationUid = user
                    destinationUsers.add(destinationUid)
                }
            }



            firebaseStorage = FirebaseStorage.getInstance()
            storageReference = firebaseStorage!!.getReference()
            val imageRef: StorageReference =
                storageReference!!.child("ManagerImages").child("$destinationUid.png")
            imageRef.downloadUrl.addOnSuccessListener { uri -> //에러나는 부분
                Glide.with(this@ClientFragment3)
                    .load(uri)
                    .apply(RequestOptions().circleCrop())
                    .into(customViewHolder.imageView)
            }
            //메시지를 내림차순으로 정렬 후 마지막 메세지의 키값을 가져옴

            customViewHolder.textView_last_message.text = chatModels[position].comments.get(chatModels[position].comments.lastIndex).values.toList().get(0)
            customViewHolder.itemView.setOnClickListener {
                val intent = Intent(context, MessageActivity::class.java)
                //클릭하면 넘어가는 부분
                intent.putExtra("destinationUid", destinationUsers[position])
                startActivity(intent)
            }
        }

        override fun getItemCount(): Int {
            return chatModels.size
        }

    }
}