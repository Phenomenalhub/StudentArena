package com.example.studentarena.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.studentarena.model.Post;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.studentarena.MainActivity;
//import com.example.studentarena.Post;
import com.example.studentarena.R;
import com.example.studentarena.model.User;
//import com.example.studentarena.User;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;

public class ComposeFragment extends Fragment {
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE =  200;
    public final static int PICK_PHOTO_CODE = 1046;
    private static final String TAG = "ComposeFragment";
    private TextInputLayout etDescription;
    private TextInputLayout etContactinfo;
    private TextInputLayout etPrice;
    private TextInputLayout etAddress;
    private TextInputLayout etCity;
    private TextInputLayout etState;
    private TextInputLayout etZipcode;
    private TextInputLayout etTitle;
    private Button btnUploadImage;
    private ImageView ivPreview;
    private ImageView ivPostImage;
    private Button btnSubmit;
    private File photoFile;
    public String photoFileName = "profilephoto.jpg";
    private Float latitude;
    private Float longitude;
    MainActivity activity;
    boolean posted = false;

    public ComposeFragment(MainActivity mainActivity) {
        activity = mainActivity;
    }
    String[] states = {"Alaska", "Alabama", "Arkansas", "American Samoa", "Arizona",
            "California", "Colorado", "Connecticut", "District of Columbia", "Delaware",
            "Florida", "Georgia", "Guam", "Hawaii", "Iowa", "Idaho", "Illinois", "Indiana",
            "Kansas", "Kentucky", "Louisiana", "Massachusetts", "Maryland", "Maine", "Michigan",
            "Minnesota", "Missouri", "Mississippi", "Montana", "North Carolina", "North Dakota",
            "Nebraska", "New Hampshire", "New Jersey", "New Mexico", "Nevada", "New York", "Ohio",
            "Oklahoma", "Oregon", "Pennsylvania", "Puerto Rico", "Rhode Island", "South Carolina",
            "South Dakota", "Tennessee", "Texas", "Utah", "Virginia", "Virgin Islands", "Vermont",
            "Washington", "Wisconsin", "West Virginia", "Wyoming"};
    AutoCompleteTextView autoCompleteTxt;
    ArrayAdapter<String> adapterItems;

    @Override
    public void onResume() {
        super.onResume();
        if (posted) {
            etState.getEditText().setText("");
        }
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
        btnUploadImage = view.findViewById(R.id.btnUploadImage);
        ivPreview = view.findViewById(R.id.ivPostImage);
        ivPostImage = view.findViewById(R.id.ivPostImage);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        autoCompleteTxt = view.findViewById(R.id.tvAutoComplete);
        adapterItems = new ArrayAdapter<String>(getContext(),R.layout.list_item,states);
        autoCompleteTxt.setAdapter(adapterItems);

        btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPickPhoto();
            }
        });

        ivPostImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCamera();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etTitle.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Tittle cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etPrice.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Price cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etDescription.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etAddress.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Street address cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etCity.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"City cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etState.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"State cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etContactinfo.getEditText().getText().toString().isEmpty()){
                    Toast.makeText(getContext(),"Contact Info. cannot be empty", Toast.LENGTH_SHORT).show();
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

    private void convertAddressToCoordinates(String addressURL, Post post){
        String newAddressURL = addressURL.replace(' ', '+');
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + newAddressURL + "&key="+getString(R.string.key);
        Log.i("String URL", newAddressURL);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JsonObject jsonObject = new JsonParser().parse(response).getAsJsonObject();
                JsonArray results = jsonObject.getAsJsonArray("results");
                Log.i("Api result", results.toString());
                JsonElement narrow = results.get(0);
                JsonObject geometry = ((JsonObject) narrow).getAsJsonObject("geometry");
                JsonObject location = geometry.getAsJsonObject("location");
                latitude = location.getAsJsonPrimitive("lat").getAsFloat();
                longitude = location.getAsJsonPrimitive("lng").getAsFloat();
                ParseGeoPoint coordinate = new ParseGeoPoint(latitude, longitude);
                post.setLocation(coordinate);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                    }
                });
                activity.bottomNavigationView.setSelectedItemId(R.id.action_home);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "https error");
            }
        });
        queue.add(stringRequest);
    }

    private void savePost() {
        Post post = new Post();
        post.setDescription(etDescription.getEditText().getText().toString());
        post.setContact(etContactinfo.getEditText().getText().toString());
        post.setPrice(Double.valueOf(String.valueOf(etPrice.getEditText().getText())));
        post.setTitle(etTitle.getEditText().getText().toString());
        String address = etAddress.getEditText().getText().toString()+", "+ etCity.getEditText().getText().toString()+", "+ etState.getEditText().getText().toString();
        Log.i("String result", address);
        post.setAddress(address);
        convertAddressToCoordinates(address, post);
        post.setUser((User)ParseUser.getCurrentUser());
        ParseFile img = new ParseFile(photoFile);
        img.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "error saving img", e);
                    return;
                }
                post.setImage(img);
                post.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null) {
                            Log.e(TAG, "Error while saving", e);
                            Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG, "Post save was successful!!");
                            posted = true;
                            activity.getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.flContainer, new FeedFragment(activity))
                                    .commit();
                        }
                    }
                });
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
    public void onPickPhoto() {
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Bring up gallery to select a photo

            Log.i(TAG, "onPickPhoto: got you");
        }
        startActivityForResult(intent, PICK_PHOTO_CODE);

    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {
            // check version of Android on device
            if(Build.VERSION.SDK_INT > 27){
                // on newer versions of Android, use the new decodeBitmap method
                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {
                // support older versions of Android by using getBitmap
                image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onPickPhoto: got you2");
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
        if ((data != null) && requestCode == PICK_PHOTO_CODE) {
            Uri photoUri = data.getData();
            Uri selectedImageURI = data.getData();
            photoFile = new File(getRealPathFromURI(selectedImageURI));

            // Load the image located at photoUri into selectedImage
            Bitmap selectedImage = loadFromUri(photoUri);
            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            Log.i(TAG, "onPickPhoto: got you3");
            // Load the selected image into a preview
            ivPreview.setImageBitmap(selectedImage);
        }
        posted = false;
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }
}