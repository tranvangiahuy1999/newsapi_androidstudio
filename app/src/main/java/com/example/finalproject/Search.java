package com.example.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.adapter.NewsAdapter;
import com.example.finalproject.model.Article;
import com.example.finalproject.model.ResponseModel;
import com.example.finalproject.restful.APIClient;
import com.example.finalproject.restful.APIInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Search extends AppCompatActivity {
    private Button backBtnSearchBar, searchBtnSearchBar, backwardBtn, backPageBtn, frontPageBtn, frontwardBtn;
    private EditText editTextSearchBar;
    private TextView resultsWarn, currentPageText;
    private RecyclerView searchScrRecyclerView;
    private LinearLayout loadingProgress;
    private String query = "";

    public List<Article> articleSearchList;
    private static final String API_KEY = "5d4644c1abfb41e0aeb0486795df432f";

    NewsAdapter adapter;
    private int currentPage = 1, totalPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setDisplayShowHomeEnabled(false);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_action_bar_search, null);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        this.getSupportActionBar().setCustomView(view, layoutParams);
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        View view1 = getSupportActionBar().getCustomView();

        backBtnSearchBar = view1.findViewById(R.id.backBtnSearchBar);
        searchBtnSearchBar = view1.findViewById(R.id.searchBtnSearchBar);
        editTextSearchBar = view1.findViewById(R.id.editTextSearchBar);

        initView();
        searchHandler();
        backHandler();
        pagerHandler();
    }

    private void pageCalculate(int results){
        totalPage = results/20;
        int temp = results%20;
        if(temp > 0){
            totalPage = totalPage + 1;
        }
    }

    private void pagerHandler(){
        backwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPage = 1;

                if(!query.equals("")){
                    loadingProgress.setVisibility(View.VISIBLE);
                    fetchAPI(query, Integer.toString(currentPage));
                }

            }
        });

        backPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!query.equals("")){
                    int temp = currentPage - 1;
                    if(temp >= 1 && temp <= totalPage){
                        loadingProgress.setVisibility(View.VISIBLE);
                        currentPage = temp;
                        fetchAPI(query, Integer.toString(currentPage));
                    }
                }
            }
        });

        frontPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!query.equals("")){
                    int temp = currentPage + 1;
                    if(temp >= 1 && temp <= totalPage){
                        loadingProgress.setVisibility(View.VISIBLE);
                        currentPage = temp;
                        fetchAPI(query, Integer.toString(currentPage));
                    }
                }
            }
        });

        frontwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!query.equals("")){
                    loadingProgress.setVisibility(View.VISIBLE);
                    currentPage = totalPage;
                    fetchAPI(query, Integer.toString(currentPage));
                }
            }
        });
    }

    private void backHandler(){
        backBtnSearchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search.this.finish();
            }
        });
    }

    private void searchHandler(){
            searchBtnSearchBar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    query = editTextSearchBar.getText().toString();
                    if(!query.equals("")){
                        currentPage = 1;
                        loadingProgress.setVisibility(View.VISIBLE);
                        fetchAPI(query, Integer.toString(currentPage));
                    }
                }
            });
    }

    private void fetchAPI(String query, final String currentPage){
        final APIInterface apiService1 = APIClient.getClient().create(APIInterface.class);
        Call<ResponseModel> call1 = apiService1.getSearchNews(query,API_KEY, currentPage);
        call1.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.body().getStatus().equals("ok")) {
                    articleSearchList = response.body().getArticles();
                    if(articleSearchList.size()>0) {
                        pageCalculate(response.body().getTotalResults());

                        adapter = new NewsAdapter(getApplicationContext(), articleSearchList);
                        searchScrRecyclerView.setHasFixedSize(true);
                        searchScrRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        searchScrRecyclerView.setAdapter(adapter);

                        currentPageText.setText(currentPage);

                        loadingProgress.setVisibility(View.GONE);
                        resultsWarn.setVisibility(View.GONE);
                    } else {
                        resultsWarn.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("EEE", t.toString());
            }
        });
    }

    private void initView(){
        loadingProgress = findViewById(R.id.loadingProgress);
        searchScrRecyclerView = findViewById(R.id.searchScrRecyclerView);
        resultsWarn = findViewById(R.id.resultsText);

        backwardBtn = findViewById(R.id.backwardBtnSearchSc);
        backPageBtn = findViewById(R.id.backPageBtnSearchSc);
        currentPageText = findViewById(R.id.currentPageTextSearchSc);
        frontPageBtn = findViewById(R.id.frontPageBtnSearchSc);
        frontwardBtn = findViewById(R.id.frontwardBtnSearchSc);
    }
}
