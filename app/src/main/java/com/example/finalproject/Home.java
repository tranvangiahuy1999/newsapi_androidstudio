package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class Home extends AppCompatActivity {
    private static final String API_KEY = "5d4644c1abfb41e0aeb0486795df432f";

    public List<Article> articleList;

    TextView currentPageText;
    RecyclerView recyclerView;
    LinearLayout loadingNotify;
    Button reconnectBtn, searchBtn, backwardBtn, backPageBtn, frontPageBtn, frontwardBtn;
    ProgressBar homeProgressBar;

    NewsAdapter adapter;

    private int currentPage = 1, totalPage = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        this.getSupportActionBar().setDisplayShowHomeEnabled(false);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.getSupportActionBar().setDisplayShowCustomEnabled(true);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_action_bar, null);

        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        this.getSupportActionBar().setCustomView(view, layoutParams);
        Toolbar parent = (Toolbar) view.getParent();
        parent.setContentInsetsAbsolute(0, 0);

        View view1 = getSupportActionBar().getCustomView();

        searchBtn = view1.findViewById(R.id.searchBtn);

        viewInit();
        fetchAPI(Integer.toString(currentPage));
        reconnectHandler();
        searchHandler();
        pagerHandler();
    }

    private void pagerHandler(){
        backwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingNotify.setVisibility(View.VISIBLE);
                currentPage = 1;
                fetchAPI(Integer.toString(currentPage));
            }
        });

        backPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    int temp = currentPage - 1;
                    if(temp >= 1 && temp <= totalPage){
                        loadingNotify.setVisibility(View.VISIBLE);
                        currentPage = temp;
                        fetchAPI(Integer.toString(currentPage));
                    }
            }
        });

        frontPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    int temp = currentPage + 1;
                    if(temp >= 1 && temp <= totalPage){
                        loadingNotify.setVisibility(View.VISIBLE);
                        currentPage = temp;
                        fetchAPI(Integer.toString(currentPage));
                    }
            }
        });

        frontwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingNotify.setVisibility(View.VISIBLE);
                currentPage = totalPage;
                fetchAPI(Integer.toString(currentPage));
            }
        });
    }

    private void pageCalculate(int results){
        totalPage = results/20;
        int temp = results%20;
        if(temp > 0){
            totalPage = totalPage + 1;
        }
    }

    private void searchHandler(){
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchScr = new Intent(Home.this, Search.class);
                Home.this.startActivity(searchScr);
            }
        });
    }

    private void reconnectHandler(){
        reconnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeProgressBar.setVisibility(View.VISIBLE);
                reconnectBtn.setVisibility(View.GONE);
                fetchAPI(Integer.toString(currentPage));
            }
        });
    }

    private void viewInit(){
        homeProgressBar = findViewById(R.id.homeProgressBar);
        loadingNotify = findViewById(R.id.loadingNotify);
        reconnectBtn = findViewById(R.id.reconnectBtn);
        recyclerView = findViewById(R.id.recyclerView);

        backwardBtn = findViewById(R.id.backwardBtnHomeSc);
        backPageBtn = findViewById(R.id.backPageBtnHomeSc);
        currentPageText = findViewById(R.id.currentPageTextHomeSc);
        frontPageBtn = findViewById(R.id.frontPageBtnHomeSc);
        frontwardBtn = findViewById(R.id.frontwardBtnHomeSc);
    }

    private void fetchAPI(final String currentPage){
        final APIInterface apiService = APIClient.getClient().create(APIInterface.class);
        Call<ResponseModel> call = apiService.getLatestNews("us",API_KEY, currentPage);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if(response.body().getStatus().equals("ok")) {
                    articleList = response.body().getArticles();
                    if(articleList.size()>0) {
                        pageCalculate(response.body().getTotalResults());

                        adapter = new NewsAdapter(getApplicationContext(), articleList);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerView.setAdapter(adapter);

                        currentPageText.setText(currentPage);

                        loadingNotify.setVisibility(View.GONE);
                        homeProgressBar.setVisibility(View.VISIBLE);
                        reconnectBtn.setVisibility(View.GONE);
                    }
                } else {
                    loadingNotify.setVisibility(View.VISIBLE);
                    homeProgressBar.setVisibility(View.GONE);
                    reconnectBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("EEE", t.toString());
            }
        });
    }
}
