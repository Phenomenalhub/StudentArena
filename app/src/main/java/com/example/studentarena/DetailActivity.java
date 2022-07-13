package com.example.studentarena;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.studentarena.model.Post;
import com.parse.ParseFile;
import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvPrice;
    TextView tvContactInfo;
    TextView tvDescription;
    TextView tvUsername;
    ImageView ivImage;
    TextView tvCreatedAt;
    ImageView ivProfileImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        tvTitle = findViewById(R.id.tvTitle);
        tvPrice = findViewById(R.id.tvPrice);
        tvContactInfo = findViewById(R.id.tvContactInfo);
        tvDescription=findViewById(R.id.tvDescription);
        tvUsername = findViewById(R.id.tvUsername);
        ivImage = findViewById(R.id.ivImage);
        tvCreatedAt = findViewById(R.id.tvCreatedAt);
        ivProfileImage = findViewById(R.id.ivProfileImage);
        Post post = Parcels.unwrap(getIntent().getParcelableExtra("Posts"));
        tvTitle.setText(post.getTitle());
        tvPrice.setText("$" + post.getPrice());
        tvContactInfo.setText(post.getContact());
        tvDescription.setText(post.getDescription());
        tvUsername.setText(post.getUser().getUsername());
        tvCreatedAt.setText(post.getCreatedAt().toString());
        ParseFile image = post.getImage();
        ParseFile userImage = post.getUser().getProfileImage();
        Glide.with(this).load(image.getUrl()).transform(new CenterCrop(),new RoundedCorners(50)).into(ivImage);
        if (userImage != null) {
            Glide.with(this).load(userImage.getUrl()).centerCrop().circleCrop().into(ivProfileImage);
        }else{
            ivProfileImage.setVisibility(View.GONE);
        }
    }
}
