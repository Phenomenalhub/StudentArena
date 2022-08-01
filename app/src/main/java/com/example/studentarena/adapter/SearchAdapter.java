package com.example.studentarena.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.example.studentarena.R;
import com.example.studentarena.model.Post;
import com.parse.ParseFile;
import com.parse.ParseUser;
import org.parceler.Parcels;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);

    }
    public SearchAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void addAll(List<Post> objects) {
        posts.addAll(objects);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvPrice;
        private TextView tvTitle;
        private TextView tvShortAddress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvShortAddress = itemView.findViewById(R.id.tvShortAddress);
        }
        public void bind(Post post) {
            // Bind the post data to the view elements
            tvTitle.setText(post.getTitle());
            tvPrice.setText("$" + post.getPrice());
            tvShortAddress.setText(post.getShortAddress());
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
        }
    }
}
