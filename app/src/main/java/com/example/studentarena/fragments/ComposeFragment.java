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
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;

public class ComposeFragment extends Fragment {
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE =  200;
    private static final String TAG = "ComposeFragment";
    private TextInputLayout etDescription;
    private TextInputLayout etContactinfo;
    private TextInputLayout etPrice;
    private TextInputLayout etAddress;
    private TextInputLayout etCity;
    private TextInputLayout etState;
    private TextInputLayout etZipcode;
    private TextInputLayout etTitle;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private File photoFile;
    public String photoFileName = "profilephoto.jpg";

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
        etTitle = (TextInputLayout) view.findViewById(R.id.etTitle);
        etDescription = (TextInputLayout) view.findViewById(R.id.etDescription);
        etContactinfo = (TextInputLayout) view.findViewById(R.id.etContactinfo);
        etAddress = (TextInputLayout) view.findViewById(R.id.etAddress);
        etCity = (TextInputLayout) view.findViewById(R.id.etCity);
        etState = (TextInputLayout) view.findViewById(R.id.etState);
        etZipcode = (TextInputLayout) view.findViewById(R.id.etZipcode);
        etPrice = (TextInputLayout) view.findViewById(R.id.etPrice);
        btnCaptureImage = view.findViewById(R.id.btnCaptureImage);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etDescription.getEditText().toString().isEmpty()){
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
        Post post = new Post();
        post.setDescription(etDescription.getEditText().getText().toString());
        post.setContact(etContactinfo.getEditText().getText().toString());
        post.setPrice(Double.valueOf(String.valueOf(etPrice.getEditText().getText())));
        post.setTitle(etTitle.getEditText().getText().toString());
        post.setImage(new ParseFile(photoFile));
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

    private void launchCamera() {
        Intent intent = new Intent( MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
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
                // RESIZE BITMAP, Load the taken image into a preview
                ivPostImage.setImageBitmap(takenImage);
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