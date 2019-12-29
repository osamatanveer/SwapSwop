package com.example.swapswop;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.swapswop.Classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

/**
 * This class is used by the users to see their own profiles
 * @author Mohammed Sameer Yaseen, Osama Tanveer, Suleyman Semih Demir
 */
public class ViewProfileActivity extends AppCompatActivity {

    private FirebaseAuth      auth;
    private FirebaseDatabase  database;
    private FirebaseUser      user;
    private DatabaseReference reference;

    private String            userID;
    private TextView username, email, phoneNumber, gender, name;
    String usernameString, emailString, phoneNumberString, genderString, nameString, profilePictureURLString;
    ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        // initializing the firebase database variables
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userID = user.getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        // Initializing
        profilePicture = findViewById(R.id.imageview);
        username       = findViewById(R.id.username);
        email          = findViewById(R.id.email);
        phoneNumber    = findViewById(R.id.phoneNumber);
        gender         = findViewById(R.id.gender);
        name           = findViewById(R.id.name);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child("Users").child(userID).getValue(User.class);
                if (user != null) {
                    usernameString          = user.getUsername();
                    emailString             = user.getEmail();
                    phoneNumberString       = user.getPhoneNumber();
                    genderString            = user.getGender();
                    nameString              = user.getFirstName() + " " + user.getLastName();
                    profilePictureURLString = user.getProfilePicUrl();

                    username.setText(usernameString);
                    email.setText(emailString);
                    phoneNumber.setText(phoneNumberString);
                    gender.setText(genderString);
                    name.setText(nameString);

                    if (profilePictureURLString != null) {
                        if (!profilePictureURLString.equals("")) {

                            Log.d("RegisterActivity: ", "");
                            Picasso.with(getBaseContext()).load(profilePictureURLString).into(profilePicture);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
