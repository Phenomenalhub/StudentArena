package com.example.studentarena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;

import com.example.studentarena.adapter.PostsAdapter;
import com.example.studentarena.adapter.SearchAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView rvSearch;
    private SearchView searchView;
    protected SearchAdapter adapter;
    protected List<Post> allPosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        rvSearch = findViewById(R.id.rvSearch);
        searchView = findViewById(R.id.searchView);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvSearch.setLayoutManager(gridLayoutManager);
        allPosts = new ArrayList<>();
        adapter = new SearchAdapter(this,allPosts);
        rvSearch.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryPosts(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryPosts(newText);
                return false;
            }
        });
    }

    private void queryPosts(String query) {
        allPosts.clear();
        ParseQuery.getQuery(Post.class)
        .include(Post.KEY_USER)
        .whereContains("title",query)
        .addDescendingOrder("createdAt")
        .findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                allPosts.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}