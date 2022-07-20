package com.example.studentarena;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import com.example.studentarena.adapter.SearchAdapter;
import com.example.studentarena.fragments.SearchFilterFragment;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    RecyclerView rvSearch;
    private SearchView searchView;
    private ImageButton filter;
    protected SearchAdapter adapter;
    protected List<Post> allPosts;
    private static final String TAG = "SearchFilterFragment";
    private String searchString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        rvSearch = findViewById(R.id.rvSearch);
        searchView = findViewById(R.id.searchView);
        filter = findViewById(R.id.ibFilter);

        StaggeredGridLayoutManager gridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvSearch.setLayoutManager(gridLayoutManager);
        allPosts = new ArrayList<>();
        adapter = new SearchAdapter(this,allPosts);
        rvSearch.setAdapter(adapter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFilterFragment SearchFilterFragment = new SearchFilterFragment(adapter);
                SearchFilterFragment.show(SearchActivity.this.getSupportFragmentManager(), TAG);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchString = query;
                queryPosts();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchString = newText;
                queryPosts();
                return false;
            }
        });
    }

    public void queryPosts() {
        allPosts.clear();
        ParseQuery query2 = ParseQuery.getQuery(Post.class)
        .include(Post.KEY_USER);
        if(searchString != null && !searchString.isEmpty()) {
            query2.whereContains("title",searchString);
        }
        query2.whereGreaterThanOrEqualTo(Post.KEY_PRICE, SearchFilterFragment.minPrice);
        query2.whereLessThanOrEqualTo(Post.KEY_PRICE, SearchFilterFragment.maxPrice);
        query2.addAscendingOrder("price");
        query2.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                allPosts.addAll(objects);
                adapter.notifyDataSetChanged();
            }
        });
    }
}