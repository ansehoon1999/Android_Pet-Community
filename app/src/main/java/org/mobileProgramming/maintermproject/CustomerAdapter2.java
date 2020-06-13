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
public class CustomerAdapter2 extends RecyclerView.Adapter<CustomerAdapter2.CustomViewHolder> {
    private ArrayList<User> arrayList; //아까 만든 class에서의 User
    private Context context;

    public CustomerAdapter2(ArrayList<User> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item2, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    } //adapter에 연결이 되고난 후 viewholder를 최초로 만들어 낸다.

    @Override
    public void onBindViewHolder(@NonNull final CustomerAdapter2.CustomViewHolder holder, final int position) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
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

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView); //view 에서 상속을 받았기 때문에 itemView에서 찾아야한다
            this.iv_profile = itemView.findViewById(R.id.iv_profile2);
            this.list_SalesTitle = itemView.findViewById(R.id.list_SalesTitle2);
            this.list_SalesAddress = itemView.findViewById(R.id.list_SalesAddress2);
            this.hash1 = itemView.findViewById(R.id.list_hash12);
            this.hash2 = itemView.findViewById(R.id.list_hash22);
            this.hash3 = itemView.findViewById(R.id.list_hash32);
        }
    }
}