package com.example.studentarena.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.studentarena.EndlessRecyclerViewScrollListener;
import com.example.studentarena.LoginActivity;
import com.example.studentarena.model.Post;
import com.example.studentarena.R;
import com.example.studentarena.adapter.PostsAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FeedFragment extends Fragment {
    private static final String TAG = "Feed Fragment";
    public EndlessRecyclerViewScrollListener scrollListener;
    private SwipeRefreshLayout swipeContainer;
    RecyclerView rvPosts;
    protected PostsAdapter adapter;
    protected List<Post> allPosts;
    private ProgressBar progressBar;

    public FeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.ibLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent i = new Intent(getContext(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });

        rvPosts = view.findViewById(R.id.rvMessages);

        // initialize the array that will hold posts and create a PostsAdapter
        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvPosts.setLayoutManager(gridLayoutManager);
        progressBar = view.findViewById(R.id.progressBar);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allPosts.clear();
                queryPosts(0);
                adapter.notifyDataSetChanged();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);
        // set the adapter on the recycler view
        rvPosts.setAdapter(adapter);
        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryPosts(totalItemsCount);
            }
        };
        rvPosts.addOnScrollListener(scrollListener);
        queryPosts(0);
    }
    private void queryPosts(int skip) {
        // specify what type of data we want to query - Post.class
        ParseQuery.getQuery(Post.class)
                .include(Post.KEY_USER)
                .whereNotEqualTo(Post.KEY_USER, ParseUser.getCurrentUser())
                .setLimit(20)
                .setSkip(skip)
                .addDescendingOrder("createdAt")
                .findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }
                for (Post post : posts) {
                    Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser().getUsername());
                }
                // save received posts to list and notify adapter of new data
                allPosts.addAll(posts);
                swipeContainer.setRefreshing(false);// swipeContainer.setRefreshing(false) once the network request has completed successfully.
                progressBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        });
    }
}