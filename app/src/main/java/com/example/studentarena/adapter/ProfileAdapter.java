package com.example.studentarena.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.studentarena.Post;
import com.example.studentarena.ProfileDetailsActivity;
import com.example.studentarena.R;
import com.parse.ParseFile;

import org.parceler.Parcels;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder>{
    private Context context;
    private List<Post> posts;

    @NonNull
    @Override
    public ProfileAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_profile, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    public ProfileAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvDetail;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDetail = itemView.findViewById(R.id.tvTitle);
        }

        public void bind(Post post) {
            // Bind the post data to the view elements
            tvDetail.setText("$" + post.getPrice()+ " •  " + post.getTitle());
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
                    Intent i = new Intent(context, ProfileDetailsActivity.class);

                    i.putExtra("Posts", Parcels.wrap(post));
                    context.startActivity(i);
                }
            });
        }
    }
}
