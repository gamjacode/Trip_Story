package com.example.trip_story

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trip_story.model.ContentDTO
import com.example.trip_story.model.DetailDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_photo.*
import java.text.SimpleDateFormat
import java.util.*

class AddPhotoActivity : AppCompatActivity() {
    var PICK_IMAGE_FROM_ALBUM = 0   //request code
    var PICK_IMAGE = 1
    var storage: FirebaseStorage? = null
    var photoUri: Uri? = null      //이미지 uri를 담음
    var auth: FirebaseAuth? = null //user의 정보를 가져올 수 있도록
    var firestore: FirebaseFirestore? = null //db를 사용할 수 있도록
    var ImageList = ArrayList<Uri>()
    private var ImageUri: Uri? = null
    private var progressDialog: ProgressDialog? = null
    private var upload_count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_photo)

        //Initiate (초기화)
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Image Uploading Pleses wait......")

        //이 엑티비티를 실행하자마자 화면이 열릴 수 있도록 open해주는 코드
        var photoPickerIntent = Intent(Intent.ACTION_GET_CONTENT)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_IMAGE_FROM_ALBUM)

        //add image upload event
        addphoto_btn_upload.setOnClickListener() {
            progressDialog!!.show()
            alert.text = "If Loading Takes too long plese Press the button again"
            contentUpload()
            multiaction()
        }

        choose.setOnClickListener(){
            val multiphotopickintent = Intent(Intent.ACTION_GET_CONTENT)
            multiphotopickintent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            multiphotopickintent.type = "image/*"
            startActivityForResult(multiphotopickintent, PICK_IMAGE)
        }
        }
    private fun multiaction(){
        val ImageFolder = FirebaseStorage.getInstance().reference.child("ImageFolder2")
        upload_count = 0
        while (upload_count < ImageList.size) {
            val IndividualImage: Uri = ImageList.get(upload_count)
            val ImageName: StorageReference =
                ImageFolder.child("image" + IndividualImage.lastPathSegment)
            ImageName.putFile(IndividualImage).addOnSuccessListener {
                ImageName.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    StorLink(url)

                    var detailDTO = DetailDTO()    //데이터 모델을 만들어줌

                    detailDTO.multiimages = ImageList

                    detailDTO.timestamp = System.currentTimeMillis()

                    detailDTO.trip_name = addphoto_edit_name.text.toString()

                    detailDTO.imageUrl =  uri.toString()

                    firestore?.collection("detailimages")?.document()
                        ?.set(detailDTO) //images collection 안에 데이터를 넣음

                    setResult(Activity.RESULT_OK) //정상적으로 닫혔다는 flag값을 넘겨주기 위해

                    finish()
                }
            }
            upload_count++
        }
    }

    private fun StorLink(url: String) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("UserOne")
        val hashMap = HashMap<String, String>()
        hashMap["Imglink"] = url
        databaseReference.push().setValue(hashMap)
        progressDialog!!.dismiss()
        alert.setText("Image Uploaded Successfully")
        upload.setVisibility(View.GONE)
    }

    //선택한 이미지를 받는 부분
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //first
        if (requestCode == PICK_IMAGE_FROM_ALBUM) {
            //결과값이 사진을 선택 했을 때
            if (resultCode == Activity.RESULT_OK) {
                //사진을 선택 헀을때 이미지의 경로가 이쪽으로 넘어옴
                photoUri = data?.data //photoUri에 경로를 담아줌
                addphoto_image.setImageURI(photoUri) //이미지 뷰에 선택한 이미지 표시
            } else {
                //취소버튼을 눌렀을 때 작동하는 부분
                finish() //엑티비티를 닫음
            }
            //second
            if (requestCode == PICK_IMAGE) {
                if (resultCode == RESULT_OK) {
                    if (data!!.clipData != null) {
                        val countClipData = data.clipData!!.itemCount
                        var currentImageSelect = 0
                        while (currentImageSelect < countClipData) {
                            ImageUri = data.clipData!!.getItemAt(currentImageSelect).uri
                            ImageList.add(ImageUri!!)
                            currentImageSelect = currentImageSelect + 1
                        }

                    } else {
                        Toast.makeText(this, "Plese Select Multiple Image", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun contentUpload() {
        //Make filename
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())  //시간
        var imageFileName = "IMAGE_" + timestamp + "_.png"       //이름이 중복되지 않도록 날짜값을 파일명으로
        //스토리지 레퍼런스를 만들어서 이미지를 업로드
        var storageRef = storage?.reference?.child("images")?.child(imageFileName)

        //1.Promise method
        storageRef?.putFile(photoUri!!)?.continueWithTask {
            return@continueWithTask storageRef.downloadUrl
        }?.addOnSuccessListener { uri ->
            var contentDTO = ContentDTO()    //데이터 모델을 만들어줌

            contentDTO.imageUrl = uri.toString() //DTO안에 url을 넣어줌

            contentDTO.uid = auth?.currentUser?.uid

            contentDTO.userId = auth?.currentUser?.email

            contentDTO.explain = addphoto_edit_explain.text.toString()

            contentDTO.trip_name = addphoto_edit_name.text.toString()

            contentDTO.timestamp = System.currentTimeMillis()

            firestore?.collection("images")?.document()
                ?.set(contentDTO) //images collection 안에 데이터를 넣음

            setResult(Activity.RESULT_OK) //정상적으로 닫혔다는 flag값을 넘겨주기 위해

            finish()
        }
    }
/*
    fun multiupload(){
        //Make filename

        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())  //시간
        var imageFileName = "IMAGE_" + timestamp + "_.png"       //이름이 중복되지 않도록 날짜값을 파일명으로
        //스토리지 레퍼런스를 만들어서 이미지를 업로드
        var storageRef = storage?.reference?.child("multiimages")?.child(imageFileName)

        //1.Promise method
        storageRef?.putFile(photoUri!!)?.continueWithTask {
            return@continueWithTask storageRef.downloadUrl


        }?.addOnSuccessListener { uri ->
            var detailDTO = DetailDTO()    //데이터 모델을 만들어줌

            detailDTO.multiimages = ImageList

            detailDTO.timestamp = System.currentTimeMillis()

            detailDTO.trip_name = addphoto_edit_name.text.toString()

            detailDTO.imageUrl =  uri.toString()

            firestore?.collection("images")?.document()
                ?.set(detailDTO) //images collection 안에 데이터를 넣음

            setResult(Activity.RESULT_OK) //정상적으로 닫혔다는 flag값을 넘겨주기 위해

            finish()
        }

    }

 */
}