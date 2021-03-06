package com.example.jackhaines.finalyearproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CameraActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Button btnCapture, btnUpload, btnMap;
    private ImageView imgCapture;
    static final int Image_Capture_Code = 1;
    String mCurrentPhotoPath;
    File photoFile;

    static double lat;
    static double lon;

    private SpinnerContent spinnerContentClass = new SpinnerContent();
    Spinner spinner, spinner2;

    ArrayAdapter arrayAdapter, arrayBirdAdapter;

    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        String[] species = {"Grebes and Divers", "Seabirds", "Waterfowl", "Herons, Egrets and Spoonbill", "Birds of Prey", "Gamebirds", "Wading Birds", "Pigeons and Doves", "Woodpeckers, Cuckoo, Kingfisher and Waxwing", "Swallows, Swift, Martins and Nightjar", "Parakeet", "Larks, Sparrows, Wagtails and Dunnock", "Thrushes, Chats, Flycatchers, Starling, Dipper and Wren", "Tits, Crests and Warblers", "Crows and shrikes", "Finches and Btuntings"};

        //Assigns buttons to Button classes
        btnCapture = findViewById(R.id.capture);
        btnUpload = findViewById(R.id.button2);
        btnMap = findViewById(R.id.button3);
        imgCapture = findViewById(R.id.imageView);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        spinner = findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(this);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, species);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);

        spinner2 =  findViewById(R.id.spinner);
        spinner2.setOnItemSelectedListener(this);

        arrayBirdAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        arrayBirdAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(arrayBirdAdapter);


        //TODO Rewrite button events to switch statements
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(CameraActivity.this);

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                }

                String type = "submit";
                String googleEmail = acct.getEmail();

                String latString = Double.toString(lat);
                String lonString = Double.toString(lon);

                String species = spinner2.getSelectedItem().toString();
                String encodedImage = BitmapToEncodedString(FileToBitmap(photoFile));

                BackendService backendService = new BackendService(CameraActivity.this);
                //First string is type and the following is data
                backendService.execute(type, googleEmail, latString, lonString, species, encodedImage);
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Start new activity
                Intent mapIntent = new Intent(CameraActivity.this, SearchActivity.class);
                CameraActivity.this.startActivity(mapIntent);
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(CameraActivity.this,
                                "com.example.android.fileprovider",
                                photoFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, Image_Capture_Code);
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_Capture_Code) {
            if (resultCode == RESULT_OK) {
                imgCapture.setImageBitmap(FileToBitmap(photoFile));
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //Convert File to Bitmap
    private Bitmap FileToBitmap(File file) {
        String filePath = file.getPath();
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return bitmap;
    }

    private String BitmapToEncodedString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (CameraActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {

            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null) {
                lat = location.getLatitude();
                lon = location.getLongitude();

            } else {
                Toast.makeText(this, "Your location is unknown", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int species = spinner.getSelectedItemPosition();
        arrayBirdAdapter.clear();
        switch (species){
            case 0:
                arrayBirdAdapter = spinnerContentClass.GrebalsAndDivers(arrayBirdAdapter);
                break;
            case 1:
                arrayBirdAdapter = spinnerContentClass.Seabirds(arrayBirdAdapter);
                break;
            case 2:
                arrayBirdAdapter = spinnerContentClass.Waterfowl(arrayBirdAdapter);
                break;
            case 3:
                arrayBirdAdapter = spinnerContentClass.HeronsEgretsAndSpoonbill(arrayBirdAdapter);
                break;
            case 4:
                arrayBirdAdapter = spinnerContentClass.BirdsOfPrey(arrayBirdAdapter);
                break;
            case 5:
                arrayBirdAdapter = spinnerContentClass.GameBirds(arrayBirdAdapter);
                break;
            case 6:
                arrayBirdAdapter = spinnerContentClass.WadingBirds(arrayBirdAdapter);
                break;
            case 7:
                arrayBirdAdapter = spinnerContentClass.PigeonsAndDoves(arrayBirdAdapter);
                break;
            case 8:
                arrayBirdAdapter = spinnerContentClass.WoodpeckersCuckooKingfisherWaxwing(arrayBirdAdapter);
                break;
            case 9:
                arrayBirdAdapter = spinnerContentClass.SwallowsSwiftMartinsAndNightjar(arrayBirdAdapter);
                break;
            case 10:
                arrayBirdAdapter = spinnerContentClass.Parakeet(arrayBirdAdapter);
                break;
            case 11:
                arrayBirdAdapter = spinnerContentClass.LarksSparrowsWagtailsAndDunnock(arrayBirdAdapter);
                break;
            case 12:
                arrayBirdAdapter = spinnerContentClass.ThrushesChatFlycatchersStarlingDipperAndWren(arrayBirdAdapter);
                break;
            case 13:
                arrayBirdAdapter = spinnerContentClass.TitsCrestsAndWarblers(arrayBirdAdapter);
                break;
            case 14:
                arrayBirdAdapter = spinnerContentClass.CrowsAndShrikes(arrayBirdAdapter);
                break;
            case 15:
                arrayBirdAdapter = spinnerContentClass.FinchesAndBuntings(arrayBirdAdapter);
                break;
        }
        arrayBirdAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

//This class is largely based on this guide
//https://developer.android.com/training/camera