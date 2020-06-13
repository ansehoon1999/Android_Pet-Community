package org.mobileProgramming.maintermproject.ManagerFragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import org.mobileProgramming.maintermproject.ManagerFragment1.ManagerFragment1_FirstInput;
import org.mobileProgramming.maintermproject.ManagerFragment1.ManagerFragment1_SecondInput;
import org.mobileProgramming.maintermproject.R;


public class ManagerFragment1 extends Fragment {
    ImageButton ManagerFragment1_card10;
    TextView google_nickname2;
    Button ManagerFragment1_userInfoBtn;
    Button ManagerFragment1_userInfoChangeBtn;
    private Bundle bundle;
    private String displayname;
    private String myAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_manager1, container, false);

        bundle = getArguments();
        if(bundle != null) {
            myAddress = bundle.getString("MyAddress");
            displayname = bundle.getString("displayname");
        }
        google_nickname2 = (TextView) rootview.findViewById(R.id.google_nickname2);
        google_nickname2.setText(displayname+"님,");


        ManagerFragment1_userInfoBtn = (Button) rootview.findViewById(R.id.ManagerFragment1_userInfoBtn);
       ManagerFragment1_userInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getActivity(), ManagerFragment1_FirstInput.class);
                intent.putExtra("myAddress", myAddress);
                Toast.makeText(getActivity(), myAddress, Toast.LENGTH_SHORT).show();
                            startActivity(intent);

            }
        });


        ManagerFragment1_userInfoChangeBtn = (Button) rootview.findViewById(R.id.ManagerFragment1_userInfoChangeBtn);
        ManagerFragment1_userInfoChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Reserveintent = new Intent(getActivity(), ManagerFragment1_SecondInput.class);
                Reserveintent.putExtra("myAddress", myAddress);
                startActivity(Reserveintent);
            }
        });

        ManagerFragment1_card10 = (ImageButton) rootview.findViewById(R.id.ManagerFragment1_card10);
        ManagerFragment1_card10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/darktnt/221874430739"));
                startActivity(intent);
            }
        });
        return rootview;
    }
}
