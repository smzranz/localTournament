package com.example.shamshir.localtournaments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class AddTournamentActivity extends AppCompatActivity {

     Calendar myCalendar;

    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";

    private static final int PICK_IMAGE_REQUEST = 234;
    private Uri filePath;

    //firebase objects
    private StorageReference storageReference;
    private DatabaseReference mDatabase;

    private DatabaseReference mMessagesReference;



    Spinner spinner;
    Spinner DistrictSpinner;

    String[] arrayForSpinner = {"Football", "Cricket", "VolleyBall","ShootOut","Other"};
    String[] arrayForDistSpinner = {"Alappuzha",
            "Ernakulam",
            "Idukki",
            "Kannur",
            "Kasaragod",
            "Kollam",
            "Kottayam",
            "Kozhikode",
            "Malappuram",
            "Palakkad",
            "Pathanamthitta",
            "Thiruvananthapuram",
            "Thrissur",
            "Wayanad","Other"};

    EditText titleTextField;
    EditText placeTextField;
    EditText dateTextField;
    EditText priceTextField;
    EditText contactTextField;
    EditText totalTeamsTextField;
    EditText moreDetailsTextField;
    Spinner districtSpinner;
    
    
    ImageButton postImage;
    Spinner gameSelectSpinner;
    Button addTournament;


    DatabaseReference databaseGames;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tournament);


//        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        mMessagesReference = FirebaseDatabase.getInstance().getReference("messages");
        titleTextField = (EditText) findViewById(R.id.titleTextEdit);
        placeTextField = (EditText) findViewById(R.id.placeTextEdit);
        dateTextField = (EditText) findViewById(R.id.dateTextEdit);
        priceTextField = (EditText) findViewById(R.id.priceTextEdit);
        postImage = (ImageButton) findViewById(R.id.imageView2);
        addTournament = (Button) findViewById(R.id.addTournament);
        gameSelectSpinner = (Spinner) findViewById(R.id.spinnerGameSelect);
        contactTextField = (EditText) findViewById(R.id.contactTextEdit);
        totalTeamsTextField = (EditText) findViewById(R.id.totalTeamsTextEdit);
        moreDetailsTextField = (EditText) findViewById(R.id.otherDescTextEdit);
        districtSpinner = (Spinner) findViewById(R.id.spinnerDistrictSelect);


            spinner = (Spinner) findViewById(R.id.spinnerGameSelect);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayForSpinner);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);


        DistrictSpinner = (Spinner) findViewById(R.id.spinnerDistrictSelect);
        ArrayAdapter<String> distdataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, arrayForDistSpinner);
        distdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        DistrictSpinner.setAdapter(distdataAdapter);



          myCalendar = Calendar.getInstance();

       // EditText edittext= (EditText) findViewById(R.id.Birthday);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateTextField.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddTournamentActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });





        addTournament.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                addTournament();

            }

        });
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                PhotoPicker.builder()
//                        .setPhotoCount(1)
//                        .setShowCamera(true)
//                        .setPreviewEnabled(true)
//                        .start(AddTournamentActivity.this, PhotoPicker.REQUEST_CODE);

                showFileChooser();
            }

        });

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS);
        databaseGames = FirebaseDatabase.getInstance().getReference("Games");
        users = FirebaseDatabase.getInstance().getReference("Users");

    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    String title ;
    String place ;
    String date ;
    String price ;
    String gameSelected;
    String contact;
    String totalTeams;
    String moreDetails;
    String district;


    private void addTournament() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        String userID = pref.getString("idName", null);

         title = titleTextField.getText().toString().trim();
         place = placeTextField.getText().toString().trim();
         date = dateTextField.getText().toString().trim();
         price = priceTextField.getText().toString().trim();
        gameSelected = gameSelectSpinner.getSelectedItem().toString().trim();
        contact = contactTextField.getText().toString().trim();
        totalTeams = totalTeamsTextField.getText().toString().trim();
        moreDetails = moreDetailsTextField.getText().toString().trim();
        district =  districtSpinner.getSelectedItem().toString().trim();

        if (!TextUtils.isEmpty(title) || !TextUtils.isEmpty(place) || !TextUtils.isEmpty(date) || !TextUtils.isEmpty(price)|| !TextUtils.isEmpty(contact) || !TextUtils.isEmpty(totalTeams)

                || !TextUtils.isEmpty(district)    || !TextUtils.isEmpty(gameSelected)
                )



        {


            if (filePath!= null) {

                uploadFile();

            }else {

                String uploadId = mDatabase.push().getKey();


                Product artist = new Product(uploadId, title, place, date,gameSelected, price, "No Image",contact,totalTeams,moreDetails,district,userID);


                databaseGames.child(uploadId).setValue(artist);

                    users.child(userID).child("My Tornaments").child(uploadId).setValue(uploadId);
                Toast.makeText(getApplicationContext(), "Data Added Succesfully", Toast.LENGTH_LONG).show();
                goBack();

            }



        } else {


            Toast.makeText(getApplicationContext(), "All Fields Required", Toast.LENGTH_LONG).show();

        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
             filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                postImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        
    }


    public void goBack(){

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }

    }


    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //dismissing the progress dialog
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();
                            String uploadId = mDatabase.push().getKey();
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            String userID = pref.getString("idName", null);

                            Product artist = new Product(uploadId, title, place, date,gameSelected, price, taskSnapshot.getDownloadUrl().toString(),contact,totalTeams,moreDetails,district,userID);



                            databaseGames.child(uploadId).setValue(artist);
                            Message message = new Message(title+":"+gameSelected+" in " + place +"("+district+")"+ " on " + date, gameSelected, null);
                        mMessagesReference.push().setValue(message);
                            users.child(userID).child("My Tornaments").child(uploadId).setValue(uploadId);
                            Toast.makeText(getApplicationContext(), "Data Added Succesfully", Toast.LENGTH_LONG).show();
                            goBack();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Log.i(exception.getMessage(), "onFailure: ");
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //displaying the upload progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            //display an error if no file is selected
        }
    }


    private void updateLabel() {
        String myFormat = "EEE,dd/MMM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateTextField.setText(sdf.format(myCalendar.getTime()));
    }
}