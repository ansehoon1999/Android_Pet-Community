package com.sehun.android_pet_community

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.googlecode.tesseract.android.TessBaseAPI
import com.sehun.android_pet_community.Model.ChatModel
import com.sehun.android_pet_community.Model.ReservationModel
import com.sehun.android_pet_community.databinding.ActivityMessageBinding
import org.intellij.lang.annotations.Language
import org.mobileProgramming.maintermproject.ProgressCircleDialog
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

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
    private var mContext: Context? = null
//    private var m_Tess: TessBaseAPI? = null
//    private var mDataPath = ""
//    private val mLanguageList = arrayOf("eng", "kor") // 언어
//    private var mCurrentPhotoPath: String? = null
//    private var m_objProgressCircle: ProgressCircleDialog? = null
//    private var m_messageHandler: MessageHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mbinding = ActivityMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

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
        when (item.itemId) {
            R.id.nav_yes -> {
                FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomUid!!)
                    .update("users", FieldValue.arrayRemove(hashMapOf(myUid!! to false)))
                FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomUid!!)
                    .update("users", FieldValue.arrayUnion(hashMapOf(myUid!! to true)))

                binding.messageActivityMatching.text = "Matching Now"
                binding.messageActivityEditText.isEnabled = true
                binding.messageActivityButton.isEnabled = true
            }
            R.id.nav_no -> {
                binding.messageActivityMatching.text = "Matching Decline"
                binding.messageActivityEditText.isEnabled = false
                binding.messageActivityButton.isEnabled = false
            }

            R.id.nav_gps -> {
                val intent: Intent = Intent(this, MapActivity::class.java)
                intent.putExtra("destinationUid", destinationUid)
                startActivity(intent)
                return true
            }

            R.id.nav_ocr -> {
//                m_objProgressCircle = ProgressCircleDialog(this@MessageActivity) //로딩 프로그래스
//
//                m_messageHandler = MessageHandler()
//
//                if (CameraOnOffFlag) // "사진찍기" 버튼이 눌리면 true
//                {
//                    PermissionCheck()
//                    Tesseract()
//                }
//
//                if (CameraOnOffFlag) {
//                    try {
//                        dispatchTakePictureIntent()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
//                    }
//                }
//
                return true
            }

        }

        return false
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            ConstantDefine.PERMISSION_CODE -> Toast.makeText(
//                this,
//                "권한이 허용되었습니다",
//                Toast.LENGTH_SHORT
//            ).show()
//            ConstantDefine.ACT_TAKE_PIC -> if (resultCode == RESULT_OK) {
//                try {
//                    val file = File(mCurrentPhotoPath)
//                    var rotatedBitmap: Bitmap? = null
//                    val bitmap = MediaStore.Images.Media.getBitmap(
//                        contentResolver,
//                        FileProvider.getUriForFile(
//                            this@MessageActivity,
//                            applicationContext.packageName + ".fileprovider", file
//                        )
//                    )
//                    if (bitmap != null) {
//                        val ei = ExifInterface(mCurrentPhotoPath!!)
//                        val orientation = ei.getAttributeInt(
//                            ExifInterface.TAG_ORIENTATION,
//                            ExifInterface.ORIENTATION_UNDEFINED
//                        )
//                        when (orientation) {
//                            ExifInterface.ORIENTATION_ROTATE_90 -> rotatedBitmap =
//                                rotateImage(bitmap, 90f)
//                            ExifInterface.ORIENTATION_ROTATE_180 -> rotatedBitmap =
//                                rotateImage(bitmap, 180f)
//                            ExifInterface.ORIENTATION_ROTATE_270 -> rotatedBitmap =
//                                rotateImage(bitmap, 270f)
//                            ExifInterface.ORIENTATION_NORMAL -> rotatedBitmap = bitmap
//                            else -> rotatedBitmap = bitmap
//                        }
//                        val ocrThread = OCRThread(rotatedBitmap!!)
//                        ocrThread.isDaemon = true
//                        ocrThread.start()
//                        Toast.makeText(
//                            mContext,
//                            resources.getString(R.string.LoadingMessage),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } catch (e: Exception) {
//                }
//            }
//            ConstantDefine.Allargy -> {}
//        }
//    }


//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String?>,
//        grantResult: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResult)
//        if (requestCode == 0) {
//        } else {
//        }
//    }
//
//    fun rotateImage(source: Bitmap, angle: Float): Bitmap? {
//        val matrix = Matrix()
//        matrix.postRotate(angle)
//        return Bitmap.createBitmap(
//            source, 0, 0, source.width, source.height,
//            matrix, true
//        )
//    }
//    fun PermissionCheck() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED && checkSelfPermission(
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                ) == PackageManager.PERMISSION_DENIED && checkSelfPermission(
//                    Manifest.permission.READ_EXTERNAL_STORAGE
//                ) == PackageManager.PERMISSION_DENIED
//            ) {
//                // 권한 없음
//                ActivityCompat.requestPermissions(
//                    this@MessageActivity, arrayOf(
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    ),
//                    ConstantDefine.PERMISSION_CODE
//                )
//            } else {
//                // 권한 있음
//            }
//        }
//    }

//    fun Tesseract() {
//        mDataPath = filesDir.toString() + "/tesseract/"
//
//        var lang = ""
//        for (Lanague in mLanguageList) {
//            checkFile(File(mDataPath + "tessdata/"), Lanague)
//            lang += Lanague + "+"
//        }
//        m_Tess = TessBaseAPI()
//        m_Tess!!.init(mDataPath, lang)
//    }
//
//    @Throws(IOException::class)
//    private fun createImageFile(): File? {
//        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val imageFileName = "JPEG_" + timeStamp + "_"
//        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        val image = File.createTempFile(
//            imageFileName,
//            ".jpg",
//            storageDir
//        )
//        mCurrentPhotoPath = image.absolutePath
//        return image
//    }
//    private fun checkFile (dir : File, Language : String) {
//        if (!dir.exists() && dir.mkdir()) {
//            copyFiles(Language)
//        }
//
//        if (dir.exists()) {
//            val datafilepath = mDataPath + "tessdata/" + Language + ".traineddata"
//            val datafile = File(datafilepath)
//            if(!datafile.exists()) {
//                copyFiles(Language)
//            }
//        }
//    }
//
//    @Throws(IOException::class)
//    private fun dispatchTakePictureIntent() {
//        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        if (takePictureIntent.resolveActivity(packageManager) != null) {
//            var photoFile: File? = null
//            try {
//                photoFile = createImageFile()
//            } catch (ex: IOException) {
//            }
//            if (photoFile != null) {
//                val photoUri = FileProvider.getUriForFile(
//                    this,
//                    this.applicationContext.packageName + ".fileprovider",
//                    photoFile
//                )
//                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
//                startActivityForResult(takePictureIntent, ACT_TAKE_PIC)
//            }
//        }
//    }
//
//    private fun copyFiles(Language: String) {
//        try {
//            var filepath = mDataPath + "/tessdata/" + Language + ".traineddata"
//            val assetManager = assets
//            val instream = assetManager.open("tessdata/$Language.traineddata")
//            val outstream: OutputStream = FileOutputStream(filepath)
//            val buffer = ByteArray(1024)
//            var read: Int
//            while (instream.read(buffer).also { read = it } != -1) {
//                outstream.write(buffer, 0, read)
//            }
//            outstream.flush()
//            outstream.close()
//            instream.close()
//
//        } catch (e : FileNotFoundException) {
//            e.printStackTrace()
//        } catch (e : IOException) {
//            e.printStackTrace()
//        }
//    }
//
//    inner class OCRThread (val rotatedImage: Bitmap) : Thread() {
//        override fun run() {
//            super.run()
//            var OCRresult: String? = null
//            m_Tess!!.setImage(rotatedImage)
//            OCRresult = m_Tess!!.getUTF8Text()
//            val message = Message.obtain()
//            message.what = RESULT_OCR
//            message.obj = OCRresult
//            m_messageHandler!!.sendMessage(message)
//        }
//
//        init {
//            if (!ProgressFlag) {
//                m_objProgressCircle = ProgressCircleDialog.show(mContext, "", "", true)
//            }
//            ProgressFlag = true
//        }
//    }
//    companion object ConstantDefine {
//        const val ACT_TAKE_PIC = 1
//        const val PERMISSION_CODE = ACT_TAKE_PIC + 1
//        const val RESULT_OCR = PERMISSION_CODE + 1
//        const val Allargy = RESULT_OCR + 1
//    }

}

