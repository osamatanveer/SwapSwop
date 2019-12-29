package com.example.swapswop.Fragments;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.swapswop.Classes.Offer;
import com.example.swapswop.Classes.SelectPhotoDialog;
import com.example.swapswop.Classes.SelectPhotoDialog.OnPhotoSelectedListener;
import com.example.swapswop.Classes.UniversalImageLoader;
import com.example.swapswop.Classes.User;
import com.example.swapswop.R;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.swapswop.RegisterActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.snapshot.ChildKey;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Queue;
/**
 * This class is the logic behind displaying the add screen and uploading the details of the offer
 * to the database
 * @author Osama Tanveer
 * @version 1.3, 8 May 2019
 */

public class AddFragment extends Fragment implements SelectPhotoDialog.OnPhotoSelectedListener {

    private final String TAG = "AddFragment";
    private long offersCount;
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth      auth;
    private String            userId;
    private String            username;

    @Override
    public void getImagePath(Uri imagePath) {
        Log.d(TAG, "getImagePath: setting the image to imageview");
        UniversalImageLoader.setImage(imagePath.toString(), postImage);
        //assign to global variable
        selectedBitmap = null;
        selectedUri = imagePath;
    }

    @Override
    public void getImageBitmap(Bitmap bitmap) {
        Log.d(TAG, "getImageBitmap: setting the image to imageview");
        postImage.setImageBitmap(bitmap);
        //assign to a global variable
        selectedUri = null;
        selectedBitmap = bitmap;
    }

    ImageView postImage;
    EditText  title, description, category, country, stateProvince, city, contactEmail;
    Button    post;

    ProgressBar progressBar;
    Bitmap selectedBitmap;

    Uri    selectedUri;
    byte[] uploadBytes;
    double progress = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view     = inflater.inflate(R.layout.fragment_add, container, false);
        postImage     = view.findViewById(R.id.post_image);
        title         = view.findViewById(R.id.input_title);
        description   = view.findViewById(R.id.input_description);
        category      = view.findViewById(R.id.category);
        country       = view.findViewById(R.id.input_country);
        stateProvince = view.findViewById(R.id.input_state_province);
        city          = view.findViewById(R.id.input_city);
        contactEmail  = view.findViewById(R.id.input_email);
        post          = view.findViewById(R.id.btn_post);
        progressBar   = view.findViewById(R.id.progress_bar);
        auth          = FirebaseAuth.getInstance();
        userId        = auth.getCurrentUser().getUid();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).build();
        ImageLoader.getInstance().init(config);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        init();



        reference.child("Offers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    offersCount = dataSnapshot.getChildrenCount();
                }
                else {
                    offersCount = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    /**
     * This method initializes the buttons by setting the on click listeners.
     */
    private void init() {

        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: opening dialog to choose new photo");
                SelectPhotoDialog dialog = new SelectPhotoDialog();
                assert getFragmentManager() != null;
                dialog.show(getFragmentManager(),getString(R.string.dialog_select_photo));
                dialog.setTargetFragment(AddFragment.this, 1);
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "OnClick: attempting to post the picture");
                if(!isEmpty(title.getText().toString())
                        && !isEmpty(description.getText().toString())
                        && !isEmpty(category.getText().toString())
                        && !isEmpty(country.getText().toString())
                        && !isEmpty(stateProvince.getText().toString())
                        && !isEmpty(city.getText().toString())
                        && !isEmpty(contactEmail.getText().toString())) {

                    // In case bitmap present and uri not present
                    if(selectedBitmap != null && selectedUri == null){
                        uploadNewPhoto(selectedBitmap);
                    }
                    else if (selectedBitmap == null && selectedUri != null) {
                        uploadNewPhoto(selectedUri);
                    }
                }
                else {
                    Toast.makeText(getActivity(), "All fields must be filled out", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * This method uploads the picture with a bitmap.
     * @param selectedBitmap the bitmap of the picture to upload
     */
    private void uploadNewPhoto(Bitmap selectedBitmap) {
        BackgroundImageResize resize = new BackgroundImageResize(selectedBitmap);
        Uri uri = null;
        resize.execute(uri);
    }

    /**
     * This method upload a picture with a uri.
     * @param selectedUri the uri of the picture
     */
    private void uploadNewPhoto(Uri selectedUri) {
        BackgroundImageResize resize = new BackgroundImageResize(null);
        resize.execute(selectedUri);
    }

    /**
     * This is an inner class it resizes the image to upload so that it uploads a small size to the
     * database.
     * @author Osama Tanveer
     * @version 1.2, 8 May 2019
     */
    public class BackgroundImageResize extends AsyncTask<Uri, Integer, byte[]>{
        Bitmap bitmap;

        public BackgroundImageResize(Bitmap bitmap){
            if (bitmap != null) {
                this.bitmap = bitmap;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(), "Compressing image", Toast.LENGTH_SHORT).show();
            showProgressBar();
        }

        @Override
        protected byte[] doInBackground(Uri... uris) {
            if (bitmap == null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uris[0]);
                } catch (IOException e) {
                    Log.e(TAG, "doInBackground: IO Exception: " +e.getMessage());
                }
            }
            byte[] bytes = null;
            bytes = getBytesFromBitmap(bitmap, 100);
            return bytes;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            super.onPostExecute(bytes);
            uploadBytes = bytes;
            hideProgressBar();
            executeUploadTask();
        }
    }

    private void executeUploadTask() {
        Toast.makeText(getActivity(), "Uploading image", Toast.LENGTH_SHORT).show();

        final String imageNameInDataBase =System.currentTimeMillis() + "." + getFileExtension(selectedUri);

        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child("Offers").child(imageNameInDataBase);

        UploadTask uploadTask = storageReference.putBytes(uploadBytes);

        final String titleInString = title.getText().toString();
        final String descriptionInString = description.getText().toString();
        final String locationInString    = city.getText().toString() + " / " +
                stateProvince.getText().toString() + " / " +
                country.getText().toString();
        final String categoryInString = category.getText().toString();


        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        final Uri secUri = uri;
                        Offer offer      = new Offer();

                        offer.setTitle(titleInString);
                        offer.setUserId(userId);
                        offer.setUserName("");
                        offer.setImageUrl(secUri.toString());
                        offer.setCategory(categoryInString);
                        offer.setProcessType("swap");
                        offer.setDate(Calendar.getInstance().getTime());
                        offer.setOrder(offersCount * -1);
                        offer.setDescription(descriptionInString);
                        offer.setLocation(locationInString);

                        String offerId = reference.push().getKey();
                        reference.child("Offers").child(offerId).setValue(offer);

                    }
                });

                resetFields();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Could not upload image", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double currentProgress = (100 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                if (currentProgress > progress + 15) {
                    progress = (100 * taskSnapshot.getBytesTransferred()) /taskSnapshot.getTotalByteCount();
                    Toast.makeText(getActivity(), progress + "%", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * This method is used to get the bytes from bitmap.
     * @param bitmap the bitmap of the picture
     * @param quality the quality the picture is supposed to be
     * @return the picture in a bytes array
     */
    public static byte[] getBytesFromBitmap(Bitmap bitmap, int quality) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality,stream);
        return stream.toByteArray();
    }

    /**
     * This method resets the fields.
     */
    private void resetFields() {
        UniversalImageLoader.setImage("", postImage);
        title.setText("");
        description.setText("");
        category.setText("");
        country.setText("");
        stateProvince.setText("");
        city.setText("");
        contactEmail.setText("");
    }

    /**
     * This method shows the progress bar
     */
    private void showProgressBar(){
        progressBar.setVisibility(View.VISIBLE);

    }

    /**
     * This method is used to hide progress bar.
     */
    private void hideProgressBar(){
        if(progressBar.getVisibility() == View.VISIBLE){
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Checks if a string is empty or not
     * @param string the string to check
     * @return true if empty, false otherwise
     */
    private boolean isEmpty(String string){
        return string.equals("");
    }

    /**
     * This method gets the file extension of the uploaded file.
     * @param uri the uri of the picture
     * @return the extension of the file
     */
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime   = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}

