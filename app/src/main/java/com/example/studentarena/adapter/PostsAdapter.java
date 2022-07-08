package com.example.studentarena.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.studentarena.DetailActivity;
import com.example.studentarena.MainActivity;
import com.example.studentarena.Post;
import com.example.studentarena.R;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

import org.parceler.Parcels;
import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private MainActivity activity;

    @NonNull
    @Override
    public PostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);

    }
    public PostsAdapter(Context context, List<Post> posts, MainActivity activity) {
        this.context = context;
        this.posts = posts;
        this.activity = activity;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvPrice;
        private TextView tvTitle;
        private TextView tvMiles;
        private int distanceToUser;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvMiles = itemView.findViewById(R.id.tvMiles);
        }
        public void bind(Post post) {
            // Bind the post data to the view elements
            tvTitle.setText(post.getTitle());
            tvPrice.setText("$" + post.getPrice());
//            tvMiles.setText(String.format(String.valueOf(setDistanceFromUser(post.getParseGeoPoint("location")))));
            ParseFile image = post.getImage();
            if (image != null) {
                ivImage.setVisibility(View.VISIBLE);
                Glide.with(context).load(image.getUrl()).transform(new CenterCrop(),new RoundedCorners(25)).into(ivImage);
            } else{
                ivImage.setVisibility(View.GONE);
            }
            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, DetailActivity.class);

                    i.putExtra("Posts", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });

            ParseGeoPoint postLocation = post.getLocation();
            setDistanceFromUser(postLocation);
        }

        private void setDistanceFromUser(ParseGeoPoint postLocation) {
            if (activity.getUserLocation() != null ) {
                distanceToUser = (int) postLocation.distanceInMilesTo(activity.getUserLocation());
                tvMiles.setText(distanceToUser + " mi.");
            }
        }
    }
}
