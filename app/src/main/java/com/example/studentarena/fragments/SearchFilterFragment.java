package com.example.studentarena.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.studentarena.Post;
import com.example.studentarena.R;
import com.example.studentarena.adapter.SearchAdapter;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchFilterFragment extends DialogFragment {
    protected SearchAdapter adapter;
    private RecyclerView rvSearch;
    private TextInputEditText textInputEditTextMin;
    private TextInputEditText textInputEditTextMax;
    private Button ibDone;

    public SearchFilterFragment(SearchAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_filter, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindValues(view);
        setDoneFilterOnClickListener();
    }

    private void bindValues(View view) {
        textInputEditTextMin = view.findViewById(R.id.textInputEditTextMin);
        textInputEditTextMax = view.findViewById(R.id.textInputEditTextMax);
        ibDone = view.findViewById(R.id.ibDone);
        rvSearch = getActivity().findViewById(R.id.rvSearch);
    }

    private void setDoneFilterOnClickListener() {
        ibDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    private void search(){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class)
                .include(Post.KEY_USER);
        Double minPrice = Double.parseDouble(String.valueOf(textInputEditTextMin.getText()));
        Double maxPrice = Double.parseDouble(String.valueOf(textInputEditTextMax.getText()));
        if (minPrice != null) {
            query.whereGreaterThanOrEqualTo(Post.KEY_PRICE, minPrice);
        }
        if (maxPrice != null) {
            query.whereLessThanOrEqualTo(Post.KEY_PRICE, maxPrice);
        }
        query.addAscendingOrder (Post.KEY_PRICE);
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> objects, ParseException e) {
                adapter.addAll(objects);
                dismiss();
            }
        });
    }
}