package org.mobileProgramming.maintermproject.Fragment4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.mobileProgramming.maintermproject.ProgressCircleDialog;
import org.mobileProgramming.maintermproject.R;
import org.mobileProgramming.maintermproject.mapActivity;
import org.mobileProgramming.maintermproject.model.ChatModel;
import org.mobileProgramming.maintermproject.model.ConstantDefine;
import org.mobileProgramming.maintermproject.model.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MessageActivity extends AppCompatActivity {
    private String destinationUid;
    private TextView textView;
    private Button button;
    private EditText editText;
    private DatabaseReference reference;
    private FirebaseAuth auth;
    private  String myUid;
    private String chatRoomUid;
    private RecyclerView recyclerView;
    private String myUiddestinationUid;
    private String destinationUidmyUid;
    private String ReservationInfoUid;
    private String manager;
    private String client;
    private String clientOkay;
    private String managerOkay;
    private String mainKey;
    ////////////////////////////
    private boolean CameraOnOffFlag = true;
    private String mCurrentPhotoPath;
    private boolean ProgressFlag = false;

    private ProgressCircleDialog m_objProgressCircle = null;
    private Context mContext;
    private TessBaseAPI m_Tess;
    private MessageHandler m_messageHandler;
    private String DestinationUid;
    private String PetAllergy;
    private String mDataPath="";
    private final String[] mLanguageList = {"eng","kor"}; // 언어
    private String date = " ";
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        mContext = this;
        mDatabase = FirebaseDatabase.getInstance().getReference("petInfo");
        myUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            destinationUid = getIntent().getStringExtra("destinationUid");//채팅을 당하는 아이디
             button = (Button) findViewById(R.id.messageActivity_button);
            editText = (EditText) findViewById(R.id.messageActivity_editText);
            textView = (TextView) findViewById(R.id.messageActivity_matching);
            recyclerView = (RecyclerView) findViewById(R.id.messageActivity_recyclerview);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_user);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.nav_yes:
                        reference = FirebaseDatabase.getInstance().getReference().child("ReservationInfo");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    mainKey = snapshot.getKey();
                                    myUiddestinationUid = snapshot.child("myUiddestinationUid").getValue(String.class);
                                    destinationUidmyUid = snapshot.child("destinationUidmyUid").getValue(String.class);
                                    client = snapshot.child("client").getValue(String.class);
                                    manager = snapshot.child("manager").getValue(String.class);
                                    Map<String, Object> map = new HashMap<>();
                                    if ((myUid + destinationUid).equals(myUiddestinationUid)
                                            || (destinationUid + myUid).equals(destinationUidmyUid)
                                            && myUid.equals(client)) {   //i'm client
                                        clientOkay = "true";
                                        Toast.makeText(MessageActivity.this, "Permit!", Toast.LENGTH_SHORT).show();
                                        map.put(myUid, "true1");
                                        FirebaseDatabase.getInstance().getReference().child("ReservationUserInfo")
                                                .child(mainKey).updateChildren(map);
                                        editText.setEnabled(true);
                                        button.setEnabled(true);

                                    }
                                    else if((myUid + destinationUid).equals(myUiddestinationUid)
                                            || (destinationUid + myUid).equals(myUiddestinationUid)
                                            && myUid.equals(manager)) {
                                        managerOkay = "true";
                                        map.put(myUid, "true2");
                                        FirebaseDatabase.getInstance().getReference().child("ReservationUserInfo")
                                                .child(mainKey).updateChildren(map);
                                        Toast.makeText(MessageActivity.this, "Permit!", Toast.LENGTH_SHORT).show();

                                        FirebaseDatabase.getInstance().getReference().child("ReservationInfo").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    String Uidclient = snapshot.child("client").getValue(String.class);
                                                    String Uidmanager = snapshot.child("manager").getValue(String.class);

                                                    if(Uidclient.equals(destinationUid) && Uidmanager.equals(myUid)) {
                                                        date = snapshot.child("date").getValue(String.class);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                                        FirebaseDatabase.getInstance().getReference().child("ManagerPossibleTime")
                                                .child(myUid).child("time").child(myUid+date).removeValue();

                                    }else {
                                        continue;
                                    }

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;

                    case R.id.nav_no :
                        reference = FirebaseDatabase.getInstance().getReference().child("ReservationInfo");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    mainKey = snapshot.getKey();
                                    myUiddestinationUid = snapshot.child("myUiddestinationUid").getValue(String.class);
                                    destinationUidmyUid = snapshot.child("destinationUidmyUid").getValue(String.class);
                                    client = snapshot.child("client").getValue(String.class);
                                    manager = snapshot.child("manager").getValue(String.class);
                                    Map<String, Object> map = new HashMap<>();
                                    if ((myUid + destinationUid).equals(myUiddestinationUid)
                                            || (destinationUid + myUid).equals(destinationUidmyUid)
                                            && myUid.equals(client)) {   //i'm client
                                        clientOkay = "false";
                                        Toast.makeText(MessageActivity.this, "No permit", Toast.LENGTH_SHORT).show();
                                        map.put(myUid, "false1");

                                        FirebaseDatabase.getInstance().getReference().child("ReservationUserInfo")
                                                .child(mainKey).updateChildren(map);

                                    }
                                    else if((myUid + destinationUid).equals(myUiddestinationUid)
                                            || (destinationUid + myUid).equals(myUiddestinationUid)
                                            && myUid.equals(manager)) {
                                        managerOkay = "false";
                                        map.put(myUid, "false2");

                                        FirebaseDatabase.getInstance().getReference().child("ReservationUserInfo")
                                                .child(mainKey).updateChildren(map);
                                        Toast.makeText(MessageActivity.this, "No permit", Toast.LENGTH_SHORT).show();
                                    }else {
                                        continue;
                                    }

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                        break;

                    case R.id.nav_user:
                        final Intent intent1 = new Intent(MessageActivity.this, MessageDestInfo.class);
                        final Bundle userInfoBundle = new Bundle();
                        auth = FirebaseAuth.getInstance();
                        reference = FirebaseDatabase.getInstance().getReference().child("clientInfo");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 list를 추출해냄
                                    String uid = snapshot.child("uid").getValue(String.class); //uid
                                    if (destinationUid.equals(uid)) {
                                        String userName = snapshot.child("userName").getValue(String.class); // username
                                        String address = snapshot.child("address").getValue(String.class); // address
                                        String job = snapshot.child("job").getValue(String.class); // job
                                        String age = snapshot.child("age").getValue(String.class); // age
                                        userInfoBundle.putString("userName",userName);
                                        userInfoBundle.putString("address",address);
                                        userInfoBundle.putString("job",job);
                                        userInfoBundle.putString("age",age);
                                        userInfoBundle.putString("destinationUid", destinationUid);
                                        intent1.putExtras(userInfoBundle);
                                        startActivity(intent1);
                                        break;
                                    } else {
                                        continue;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;
                    case R.id.nav_pet:
                        final Intent intent2 = new Intent(MessageActivity.this, MessageDestPetInfo.class);
                        final Bundle userInfoBundle2 = new Bundle();
                        auth = FirebaseAuth.getInstance();
                        reference = FirebaseDatabase.getInstance().getReference().child("petInfo");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) { //반복문으로 데이터 list를 추출해냄
                                    String uid = snapshot.child("uid").getValue(String.class); //uid
                                    if (destinationUid.equals(uid)) {

                                        String userName = snapshot.child("petName").getValue(String.class); // username
                                        String address = snapshot.child("petAge").getValue(String.class); // address
                                        String job = snapshot.child("petType").getValue(String.class); // job
                                        String age = snapshot.child("Allergy").getValue(String.class); // age
                                        userInfoBundle2.putString("petName",userName);
                                        userInfoBundle2.putString("petAge",address);
                                        userInfoBundle2.putString("petType",job);
                                        userInfoBundle2.putString("Allergy",age);
                                        userInfoBundle2.putString("destinationUid", destinationUid);
                                        intent2.putExtras(userInfoBundle2);
                                        startActivity(intent2);
                                        break;
                                    } else {
                                        continue;
                                    }


                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        break;

                    case R.id.nav_res:
                        FirebaseDatabase.getInstance().getReference().child("ReservationInfo").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String Uidclient = snapshot.child("client").getValue(String.class);
                                    String Uidmanager = snapshot.child("manager").getValue(String.class);

                                    if(Uidclient.equals(destinationUid) && Uidmanager.equals(myUid)) {
                                        date = snapshot.child("date").getValue(String.class);
                                        String address = snapshot.child("address").getValue(String.class);
                                        Intent intent3 = new Intent(MessageActivity.this, ReservationInfo.class);
                                        intent3.putExtra("date", date);
                                        intent3.putExtra("address", address);
                                        startActivity(intent3);
                                        break;
                                    }
                                    else {
                                        Toast.makeText(MessageActivity.this, "manager만 열 수 있습니다", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                        break;
                    case R.id.nav_ocr:
                                if(CameraOnOffFlag) {
                                    try {
                                        dispatchTakePictureIntent ();
                                    } catch (IOException e) {
                                        e.printStackTrace ();
                                    }
                                }

                        m_objProgressCircle = new ProgressCircleDialog(MessageActivity.this);//로딩 프로그래스
                        m_messageHandler = new MessageHandler();

                        if(CameraOnOffFlag) // "사진찍기" 버튼이 눌리면 true
                        {
                            PermissionCheck();
                            Tesseract();
                        }

                        break;
                    case R.id.nav_gps:
                        Intent intent = new Intent(MessageActivity.this, mapActivity.class);
                        intent.putExtra("destinationUid", destinationUid);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });

             DestinationUid = destinationUid;
            ReservationJob();

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChatModel chatModel = new ChatModel();
                    chatModel.users.put(myUid,true);
                    chatModel.users.put(destinationUid, true);
                    if(chatRoomUid == null) {
                        button.setEnabled(false);
                        FirebaseDatabase.getInstance().getReference().child("chatrooms").push().setValue(chatModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                checkChatRoom();
                            }
                        });
                    } else {
                        ChatModel.Comment comment = new ChatModel.Comment();
                        comment.uid = myUid;
                        comment.message = editText.getText().toString();
                        FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                editText.setText("");
                            }
                        });

                    }
                }
            });
            checkChatRoom();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case ConstantDefine.PERMISSION_CODE:
                Toast.makeText(this, "권한이 허용되었습니다", Toast.LENGTH_SHORT).show();
                break;
            case ConstantDefine.ACT_TAKE_PIC:
                if(resultCode==RESULT_OK) {
                    try {
                        File file = new File(mCurrentPhotoPath);
                        Bitmap rotatedBitmap = null;
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                                FileProvider.getUriForFile(MessageActivity.this,
                                        getApplicationContext().getPackageName() + ".fileprovider", file));

                        if(bitmap != null) {
                            ExifInterface ei = new ExifInterface(mCurrentPhotoPath);
                            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                                    ExifInterface.ORIENTATION_UNDEFINED);
                            switch (orientation) {

                                case ExifInterface.ORIENTATION_ROTATE_90:
                                    rotatedBitmap = rotateImage(bitmap, 90);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_180:
                                    rotatedBitmap = rotateImage(bitmap, 180);
                                    break;

                                case ExifInterface.ORIENTATION_ROTATE_270:
                                    rotatedBitmap = rotateImage(bitmap, 270);
                                    break;

                                case ExifInterface.ORIENTATION_NORMAL:
                                default:
                                    rotatedBitmap = bitmap;
                            }
                            OCRThread ocrThread = new OCRThread(rotatedBitmap);
                            ocrThread.setDaemon(true);
                            ocrThread.start();
                            Toast.makeText(mContext, getResources().getString(R.string.LoadingMessage), Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {

                    }
                }
                break;
            case ConstantDefine.Allargy:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        if (requestCode == 0) {
        } else {
        }
    }

    // 이미지를 원본과 같게 회전시킨다.
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public void PermissionCheck() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED &&
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                // 권한 없음
                ActivityCompat.requestPermissions(MessageActivity.this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        ConstantDefine.PERMISSION_CODE);
            } else {
                // 권한 있음
            }
        }
    }

    public void Tesseract() {
        //언어파일 경로
        mDataPath = getFilesDir() + "/tesseract/";

        //트레이닝데이터가 카피되어 있는지 체크
        String lang = "";
        for (String Language : mLanguageList) {
            checkFile(new File(mDataPath + "tessdata/"), Language);
            lang += Language + "+";
        }
        m_Tess = new TessBaseAPI();
        m_Tess.init(mDataPath, lang);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=  File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() throws IOException {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(getPackageManager())!= null) {
            File photoFile = null;

            try {
                photoFile = createImageFile ();
            } catch (IOException ex) {

            }

            if(photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        this.getApplicationContext().getPackageName()+".fileprovider",
                        photoFile);


                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, ConstantDefine.ACT_TAKE_PIC);
            }

        }

    }

    //copy file to device
    private void copyFiles(String Language) {
        try {
            String filepath = mDataPath + "/tessdata/" + Language + ".traineddata";
            AssetManager assetManager = getAssets();
            InputStream instream = assetManager.open("tessdata/"+Language+".traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //check file on the device
    private void checkFile(File dir, String Language) {
        //디렉토리가 없으면 디렉토리를 만들고 그후에 파일을 카피
        if (!dir.exists() && dir.mkdirs()) {
            copyFiles(Language);
        }
        //디렉토리가 있지만 파일이 없으면 파일카피 진행
        if (dir.exists()) {
            String datafilepath = mDataPath + "tessdata/" + Language + ".traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFiles(Language);
            }
        }
    }








    private class OCRThread extends Thread {
        private Bitmap rotatedImage;
        OCRThread(Bitmap rotatedImage) {
            this.rotatedImage = rotatedImage;
            if(!ProgressFlag) {
                m_objProgressCircle = ProgressCircleDialog.show(mContext, "", "", true);
            }
            ProgressFlag = true;
        }

        @Override
        public void run() {
            super.run();

            String OCRresult=  null;
            m_Tess.setImage(rotatedImage);
            OCRresult = m_Tess.getUTF8Text();

            Message message = Message.obtain();
            message.what = ConstantDefine.RESULT_OCR;
            message.obj = OCRresult;
            m_messageHandler.sendMessage(message);
        }
    }


    public class MessageHandler extends Handler {

        @Override
        public void handleMessage(final Message msg) {
            super.handleMessage(msg);

            switch(msg.what) {
                case ConstantDefine.RESULT_OCR:
                    final String tt = String.valueOf(msg.obj);
                    if(m_objProgressCircle.isShowing() && m_objProgressCircle !=null)
                        m_objProgressCircle.dismiss();
                    ProgressFlag = false;
                    Toast.makeText(mContext, "문자인식이 완료되었습니다", Toast.LENGTH_SHORT).show();

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String uid = snapshot.child("uid").getValue(String.class);
                                if(uid.equals(DestinationUid)) {
                                    String petAllgery = snapshot.child("petAllgery").getValue(String.class);
                                    PetAllergy = petAllgery;
                                    Toast.makeText(mContext, petAllgery, Toast.LENGTH_SHORT).show();
                                }
                            }
                            if(tt.contains(PetAllergy)) {
                                Toast.makeText(mContext, "급여 금지 성분이 포함되어 있습니다 \"" + PetAllergy + "\"", Toast.LENGTH_SHORT).show();
                            }
                            else    {
                                Toast.makeText(mContext, "급여 가능", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


            }
        }
    }





    void ReservationJob() {
        reference = FirebaseDatabase.getInstance().getReference().child("ReservationUserInfo");
        reference.addValueEventListener(new ValueEventListener() {
            String entire = "";
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String cost = snapshot.getValue().toString();
                    entire = entire + cost;

                    if(entire.contains("true1")&&entire.contains("true2")) {
                        textView.setText("matching now");
                        editText.setEnabled(true);
                        button.setEnabled(true);
                        break;
                    }
                    else if(entire.contains("false")) {
                        textView.setText("no matching now");
                        editText.setEnabled(false);
                        button.setEnabled(false);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    void checkChatRoom() {
            FirebaseDatabase.getInstance().getReference().child("chatrooms").orderByChild("users/"+myUid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        ChatModel chatModel = item.getValue(ChatModel.class); //
                        if(chatModel.users.containsKey(destinationUid)) {
                            chatRoomUid = item.getKey();
                            button.setEnabled(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(MessageActivity.this));
                            recyclerView.setAdapter(new RecyclerViewAdapter());
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

            List<ChatModel.Comment> comments;
            User userModel;
            public RecyclerViewAdapter() {
                comments = new ArrayList<>();

                FirebaseDatabase.getInstance().getReference().child("users").child(destinationUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userModel = dataSnapshot.getValue(User.class);
                        getMessageList();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            void getMessageList() {
                FirebaseDatabase.getInstance().getReference().child("chatrooms").child(chatRoomUid).child("comments").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        comments.clear();

                        for(DataSnapshot item : dataSnapshot.getChildren()) {
                            comments.add(item.getValue(ChatModel.Comment.class));
                        }
                        //메시지 갱신
                        notifyDataSetChanged();
                        recyclerView.scrollToPosition(comments.size()-1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @NonNull
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);

                return new MessageViewHolder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
                MessageViewHolder messageViewHolder = ((MessageViewHolder) holder);
                //내가 보낸 메시지
                if (comments.get(position).uid.equals(myUid))  {//내 uid {
                    messageViewHolder.textView_message.setText(comments.get(position).message);
                    messageViewHolder.linearLayout_destination.setVisibility(View.INVISIBLE);
                    messageViewHolder.textView_message.setTextSize(10);
                    messageViewHolder.linearLayout_main.setGravity(Gravity.RIGHT);
                } else {
                    messageViewHolder.linearLayout_destination.setVisibility(View.VISIBLE);
                    messageViewHolder.textView_message.setText(comments.get(position).message);
                    messageViewHolder.textView_message.setTextSize(10);
                    messageViewHolder.linearLayout_main.setGravity(Gravity.LEFT);
                }
            }
            @Override
            public int getItemCount() {
                return comments.size();
            }
            private class MessageViewHolder extends  RecyclerView.ViewHolder {
                public TextView textView_message;
                public LinearLayout linearLayout_destination;
                public LinearLayout linearLayout_main;


                public MessageViewHolder(View view) {
                    super(view);
                    textView_message = (TextView)  view.findViewById(R.id.messageItem_textView_message);
                    linearLayout_destination = (LinearLayout) view.findViewById(R.id.messageItem_linearlayout_destination);
                    linearLayout_main =  (LinearLayout) view.findViewById(R.id.message_linearlayout_main);

                }
            }

    }
}
