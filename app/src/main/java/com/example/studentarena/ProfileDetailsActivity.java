package com.example.studentarena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.studentarena.adapter.ProfileAdapter;
import com.example.studentarena.fragments.ProfileFragment;
import com.example.studentarena.model.Post;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ProfileDetailsActivity extends AppCompatActivity {
    private Context context;
    TextView tvTitle;
    TextView tvPrice;
    TextView tvContactInfo;
    TextView tvDescription;
    TextView tvUsername;
    ImageView ivImage;
    TextView tvCreatedAt;
    ImageView ivProfileImage;
    ImageButton ibDelete;
    RecyclerView rvProfile;
    Post post;
    int postPosition;
    protected ProfileAdapter adapter;
    public static final String TAG = "ProfileDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_details);
        rvProfile = findViewById(R.id.rvProfile);
        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvContactInfo = findViewById(R.id.tvContactInfo);
        tvDescription = findViewById(R.id.tvDescription);
        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        ibDelete = findViewById(R.id.ibDelete);
        post = Parcels.unwrap(getIntent().getParcelableExtra("Posts"));
        postPosition = getIntent().getIntExtra("Position", 0);
        tvTitle.setText(post.getTitle());
        tvPrice.setText("$" + post.getPrice());
        tvContactInfo.setText(post.getContact());
        tvDescription.setText(post.getDescription());
        tvUsername.setText(post.getUser().getUsername());
        tvCreatedAt.setText(post.getCreatedAt().toString());
        ParseFile image = post.getImage();
        ParseFile userImage = post.getUser().getProfileImage();
        Glide.with(this).load(image.getUrl()).transform(new CenterCrop(), new RoundedCorners(50)).into(ivImage);
        if (userImage != null) {
            Glide.with(this).load(userImage.getUrl()).centerCrop().circleCrop().into(ivProfileImage);
        } else {
            ivProfileImage.setVisibility(View.GONE);
        }
        ibDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileDetailsActivity.this);
                builder.setTitle("Are you sure you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                               ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
                               query.whereContains("objectId", post.getObjectId());
                               query.findInBackground(new FindCallback<Post>() {
                                   @Override
                                   public void done(List<Post> objects, ParseException e) {
                                       objects.get(0).deleteInBackground();
                                       ProfileFragment.adapter.removePost(postPosition);
                                       Log.i(TAG, String.valueOf(postPosition));
                                       finish();
                                   }
                               });
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }
                )
                        .create();
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}