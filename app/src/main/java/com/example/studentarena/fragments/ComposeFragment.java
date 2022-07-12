package com.example.studentarena.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studentarena.Post;
import com.example.studentarena.R;
import com.example.studentarena.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class ComposeFragment extends Fragment {
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE =  200;
    private static final String TAG = "ComposeFragment";
    private EditText etDescription;
    private EditText etContactinfo;
    private EditText etPrice;
    private EditText etAddress;
    private EditText etCity;
    private EditText etState;
    private EditText etZipcode;
    private EditText etTitle;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private ImageView ivPostImage2;
    private Button btnSubmit;
    private File photoFile;
    public String photoFileName = "photo1.jpg";
    public String photoFileName2 = "photo2.jpg";
    public String whichImageView = "";
    Post post = new Post();

    public ComposeFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_compose, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        etTitle = view.findViewById(R.id.etTitle);
        etDescription = view.findViewById(R.id.etDescription);
        etContactinfo = view.findViewById(R.id.etContactinfo);
        etAddress = view.findViewById(R.id.etAddress);
        etCity = view.findViewById(R.id.etCity);
        etState = view.findViewById(R.id.etState);
        etZipcode = view.findViewById(R.id.etZipcode);
        etPrice = view.findViewById(R.id.etPrice);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        ivPostImage2 = view.findViewById(R.id.ivPostImage2);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        ivPostImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichImageView = "ImageView2";
                launchCamera("ImageView2");
            }
        });
        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whichImageView = "ImageView1";
                launchCamera("ImageView1");
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDescription.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (photoFile == null || ivPostImage.getDrawable() == null){
                    Toast.makeText(getContext(), "There is no image!", Toast.LENGTH_SHORT).show();
                    return;
                }
                savePost ();
            }
        });
    }

    private void savePost() {

        post.setDescription(etDescription.getText().toString());
        post.setContact(etContactinfo.getText().toString());
        post.setPrice(etPrice.getText().toString());
        post.setTitle(etTitle.getText().toString());
        post.setUser((User)ParseUser.getCurrentUser());
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "Post save was successful!!");
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.flContainer, new FeedFragment())
                            .commit();
                }
            }
        });
    }

    private void launchCamera(String postImageViewString) {
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
        //Intent intent = new Intent( MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
        // Create a File reference for future access
        if (whichImageView.equals("ImageView2")) {
            photoFile = getPhotoFileUri(photoFileName2);
        } else {
            photoFile = getPhotoFileUri(photoFileName);
        }
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        intent.putExtra("PostImageView", postImageViewString);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                if(whichImageView.equals("ImageView1")){
                    ivPostImage.setImageBitmap(takenImage);
                    post.setImage(new ParseFile(photoFile));
                }
                else if(whichImageView.equals("ImageView2")){
                    ivPostImage2.setImageBitmap(takenImage);
                    post.setImage2(new ParseFile(photoFile));
                }
            } else { // Result was a failure
                Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public File getPhotoFileUri(String filename) {
        // Get safe storage directory for photos
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + filename);
    }
}