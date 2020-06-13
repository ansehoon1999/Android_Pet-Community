package org.mobileProgramming.maintermproject;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.mobileProgramming.maintermproject.model.User;


import java.util.ArrayList;

//adapter의 기본 구색
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;
    private ArrayList<User> arrayList; //아까 만든 class에서의 User
    private Context context;

    public CustomAdapter(ArrayList<User> arrayList, Context context, OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    } //adapter에 연결이 되고난 후 viewholder를 최초로 만들어 낸다.

    @Override
    public void onBindViewHolder(@NonNull final CustomAdapter.CustomViewHolder holder, final int position) {


        FirebaseStorage  firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        StorageReference imageRef = storageReference.child("ManagerSalesImages").child(arrayList.get(position).uid+".png");
        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(holder.itemView)
                        .load(uri)
                        .apply(new RequestOptions().circleCrop())
                        .into(holder.iv_profile);
            }
        });
        holder.list_SalesTitle.setText(arrayList.get(position).Salestitle);
        holder.list_SalesAddress.setText(arrayList.get(position).address);
        holder.hash1.setText(arrayList.get(position).hashtag1);
        holder.hash2.setText(arrayList.get(position).hashtag2);
        holder.hash3.setText(arrayList.get(position).hashtag3);
        holder.button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        //삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder { //listview에서 만든 것들을 여기로 불러 놓을 거임
        ImageView iv_profile;
        TextView list_SalesTitle;
        TextView list_SalesAddress;
        Button hash1;
        Button hash2;
        Button hash3;

        Button button3;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView); //view 에서 상속을 받았기 때문에 itemView에서 찾아야한다
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.list_SalesTitle = itemView.findViewById(R.id.list_SalesTitle);
            this.list_SalesAddress = itemView.findViewById(R.id.list_SalesAddress);
            this.hash1 = itemView.findViewById(R.id.list_hash1);
            this.hash2 = itemView.findViewById(R.id.list_hash2);
            this.hash3 = itemView.findViewById(R.id.list_hash3);
            this.button3 = itemView.findViewById(R.id.button3);
        }
    }
}
