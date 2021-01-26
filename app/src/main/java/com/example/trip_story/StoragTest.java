package com.example.trip_story;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;

public class StoragTest extends AppCompatActivity {
    Button upload, choose;
    TextView alert;
    private  static  final  int PICK_IMAGE = 1;
    ArrayList<Uri> ImageList = new ArrayList<Uri>();
    private Uri ImageUri;
    private ProgressDialog progressDialog;
    private  int upload_count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storag_test);

        alert = findViewById(R.id.alert);
        upload = findViewById(R.id.upload_image);
        choose = findViewById(R.id.chooser);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Image Uploading Pleses wait......");
        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("Detailimage/");
                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                startActivityForResult(photoPickerIntent, PICK_IMAGE);
                //startActivityForResult(intent,PICK_IMAGE);
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                alert.setText("If Loading Takes too long plese Press the button again");
                StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ImageFolder");

                for(upload_count=0;upload_count<ImageList.size();upload_count++){

                    Uri IndividualImage = ImageList.get(upload_count);
                    final StorageReference ImageName = ImageFolder.child("Detailimage"+IndividualImage.getLastPathSegment());

                    ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                  String url = String.valueOf(uri);

                                  StorLink(url);
                                }
                            });
                        }

                        });
                    }
                }
            });
        }

    private void StorLink(String url) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("UserOne");
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("Imglink",url);

        databaseReference.push().setValue(hashMap);
        progressDialog.dismiss();
        alert.setText("Image Uploaded Successfully");
        upload.setVisibility(View.GONE);
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == PICK_IMAGE){
                if(resultCode == RESULT_OK){
                    if(data.getClipData()!=null){
                        int countClipData = data.getClipData().getItemCount();

                        int currentImageSelect = 0;
                        while (currentImageSelect<countClipData){
                        ImageUri = data.getClipData().getItemAt(currentImageSelect).getUri();
                        ImageList.add(ImageUri);
                        currentImageSelect = currentImageSelect +1;
                        }
                        alert.setVisibility(View.VISIBLE);
                        alert.setText("You Have Selected" + ImageList.size() + "Images");
                        choose.setVisibility(View.GONE);
                    }else{
                        Toast.makeText(this, "Plese Select Multiple Image", Toast.LENGTH_LONG).show();
                    }
            }
        }
    }
}