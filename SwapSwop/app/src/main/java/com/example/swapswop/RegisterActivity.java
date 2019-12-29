package com.example.swapswop;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.swapswop.Classes.Offer;
import com.example.swapswop.Classes.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

/**
 * This class is used to handle the backend of the register activity. It is used to register the
 * user.
 * @author Osama Tanveer
 * @version 1.2, 13 May 2019
 */
public class RegisterActivity extends AppCompatActivity {
    // constants
    private final int PICK_IMAGE_REQUEST = 1;

    // firebase database variables
    private FirebaseAuth      auth;
    private FirebaseDatabase  database;
    private DatabaseReference reference;
    private StorageReference  storageReference;

    // other variables
    private MaterialEditText  firstName, lastName, gender, username, email, password, reenteredPassword, phoneNumber, defaultLocation;
    private ImageView         profilePicture;
    private Button            registerButton;
    private String            profilePictureNameInDatabase;
    private Uri               mImageUri;
    private UploadTask        uploadTask;


    // Location
    //FusedLocationProviderClient client;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        profilePicture    = (ImageView) findViewById(R.id.profile_picture);
        firstName         = (MaterialEditText) findViewById(R.id.first_name);
        lastName          = (MaterialEditText) findViewById(R.id.last_name);
        gender            = (MaterialEditText) findViewById(R.id.gender);
        username          = (MaterialEditText) findViewById(R.id.username);
        email             = (MaterialEditText) findViewById(R.id.email);
        password          = (MaterialEditText) findViewById(R.id.password);
        reenteredPassword = (MaterialEditText) findViewById(R.id.reenterPassword);
        phoneNumber       = (MaterialEditText) findViewById(R.id.phoneNumber);
        defaultLocation   = (MaterialEditText) findViewById(R.id.location);
        registerButton    = (Button) findViewById(R.id.register);

        auth              = FirebaseAuth.getInstance();

        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

//        Getting default location
//        client = LocationServices.getFusedLocationProviderClient(this);
//        requestPermission();
//        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        client.getLastLocation().addOnSuccessListener(RegisterActivity.this, new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    Geocoder geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());
//                    try {
//                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
//                        Address obj = addresses.get(0);
//                        String add = obj.getAddressLine(0);
//                        add = add + "\n" + obj.getCountryName();
//                        add = add + "\n" + obj.getCountryCode();
//                        add = add + "\n" + obj.getAdminArea();
//                        add = add + "\n" + obj.getPostalCode();
//                        add = add + "\n" + obj.getSubAdminArea();
//                        add = add + "\n" + obj.getLocality();
//                        add = add + "\n" + obj.getSubThoroughfare();
//                        defaultLocation.setText(add);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String textFirstName         = firstName.getText().toString();
                final String textLastName          = lastName.getText().toString();
                final String textGender            = gender.getText().toString();
                final String textUsername          = username.getText().toString();
                final String textEmail             = email.getText().toString();
                final String textPassword          = password.getText().toString();
                final String textReenteredPassword = reenteredPassword.getText().toString();
                final String textPhoneNumber       = phoneNumber.getText().toString();
                final String textLocation          = defaultLocation.getText().toString();

                if (TextUtils.isEmpty(textUsername) || TextUtils.isEmpty(textEmail) || TextUtils.isEmpty(textPassword)){
                    Toast.makeText(RegisterActivity.this, "All fields are required.", Toast.LENGTH_SHORT).show();
                } else if (textPassword.length() < 6){
                    Toast.makeText(RegisterActivity.this, "Password must be atleast 6 characters long.", Toast.LENGTH_SHORT).show();
                } else if (!textPassword.equals(textReenteredPassword)){
                    Toast.makeText(RegisterActivity.this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(textEmail, textPassword)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = auth.getCurrentUser();
                                        String userID = user.getUid();
                                        Log.d("tag","user id is: " + userID);
                                        database = FirebaseDatabase.getInstance();
                                        reference = database.getReference("Users").child(userID);

                                        currentUser = new User(textFirstName, textLastName);
                                        currentUser.setGender(textGender);
                                        currentUser.setUsername(textUsername);
                                        currentUser.setEmail(textEmail);
                                        currentUser.setPhoneNumber(textPhoneNumber);
                                        currentUser.setDefaultLocation(textLocation);
                                        currentUser.setUserID(user.getUid());
                                        Log.d("RegisterActivityMyLog: ", currentUser.getGender());

                                        //Picture
                                        profilePictureNameInDatabase = System.currentTimeMillis() + "." + getFileExtension(mImageUri);
                                        storageReference = FirebaseStorage.getInstance().getReference()
                                                .child("ProfPics").child(profilePictureNameInDatabase);
                                        uploadTask = storageReference.putFile(mImageUri);

                                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        currentUser.setProfilePicUrl(uri.toString() + "");
                                                        reference.setValue(currentUser);
                                                        Log.d("RegisterActivity", currentUser.getProfilePicUrl());
                                                    }
                                                });
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(RegisterActivity.this, "Could not upload image", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText( RegisterActivity.this, "Could not upload " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * Gets the file extension of the image uri
     * @param uri the uri of the image
     * @return the file extension
     */
    private String getFileExtension(Uri uri){
        ContentResolver cR   = getContentResolver();
        MimeTypeMap     mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /**
     * Method to open the file chooser
     */
    private void openFileChooser() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
                && data.getData() != null) {
            mImageUri = data.getData();

            Log.d("My Debug",data.getData().toString());
            Picasso.with(this).load(mImageUri).into(profilePicture);
        }
    }

    // Getting permission for location

    /**
     * Requesting the permission for location.
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
