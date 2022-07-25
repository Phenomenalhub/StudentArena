package com.example.studentarena;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.studentarena.model.Message;
import com.example.studentarena.model.Post;
import com.example.studentarena.model.User;
import com.example.studentarena.utilities.GetRelativeTime;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "Detail Activity";
    TextView tvTitle;
    TextView tvPrice;
    TextView tvContactInfo;
    TextView tvDescription;
    TextView tvUsername;
    ImageView ivImage;
    TextView tvCreatedAt;
    ImageView ivProfileImage;
    EditText etDefaultMessage;
    Button btnDefaultSend;
    private Post mPost;

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
        etDefaultMessage = findViewById(R.id.etDefaultMessage);
        btnDefaultSend = findViewById(R.id.btnDefaultSend);
        mPost = Parcels.unwrap(getIntent().getParcelableExtra("Posts"));
        tvTitle.setText(mPost.getTitle());
        tvPrice.setText("$" + mPost.getPrice());
        tvContactInfo.setText("Contact: " + mPost.getContact());
        tvDescription.setText(mPost.getDescription());
        tvUsername.setText(mPost.getUser().getUsername());
        tvCreatedAt.setText(GetRelativeTime.getSimpleTime(mPost.getCreatedAt()));
        ParseFile image = mPost.getImage();
        ParseFile userImage = mPost.getUser().getProfileImage();
        Glide.with(this).load(image.getUrl()).transform(new CenterCrop(),new RoundedCorners(50)).into(ivImage);
        if (userImage != null) {
            Glide.with(this).load(userImage.getUrl()).centerCrop().circleCrop().into(ivProfileImage);
        }else{
            ivProfileImage.setVisibility(View.GONE);
        }
        findViewById(R.id.btnDefaultSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etDefaultMessage.getText().toString();
                Toast.makeText(DetailActivity.this, btnDefaultSend.getText().toString(), Toast.LENGTH_LONG).show();
                if (message != null) {
                    sendMessage(message);
                } else {
                    Log.e(TAG, "Not able to send message");
                }
            }
        });
    }
    private void sendMessage(String message) {
        Intent intent = new Intent(DetailActivity.this, ChatActivity.class);
        // Make new massage
        Message newMessage = new Message();
        newMessage.setSender((User)  ParseUser.getCurrentUser());
        User otherUser = mPost.getUser();
        newMessage.setReceiver(mPost.getUser());
        newMessage.setBody(message);
        newMessage.setTargetPost(mPost);
        newMessage.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                intent.putExtra("otherUser",Parcels.wrap(otherUser));
                intent.putExtra("targetPost", mPost);
                startActivity(intent);
            }
        });
    }
}
