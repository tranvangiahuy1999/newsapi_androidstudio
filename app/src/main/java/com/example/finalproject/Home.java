package com.example.finalproject;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private static String api_key="apiKey=5d4644c1abfb41e0aeb0486795df432f";
    private static String base_headline_url="https://newsapi.org/v2/top-headlines?country=us";
    private static String base_seacrh_url="https://newsapi.org/v2/everything?q=";
    private static String page_url="page=";

    ArrayList<Articles> articlesArrayList;
    public String status;
    public int totalResults;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        getAPI();
    }

    private void viewInit(){
        //find by ID in here
    }

    private void getAPI(){
        articlesArrayList = new ArrayList<>();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = base_headline_url+"&"+api_key;
        final JsonObjectRequest  arrayRequest = new JsonObjectRequest  (
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray articles;
                        try{
                            status = response.getString("status");
                            totalResults = response.getInt("totalResults");
                            articles = response.getJSONArray("articles");
                            for (int i = 0; i < articles.length(); i++){
                                JSONObject object;
                                String source_name, author, title, description, url, urlToImage,publishedAt, content;

                                object = articles.getJSONObject(i);
                                source_name = object.getJSONObject("source").getString("name");
                                author = object.getString("author");
                                title=object.getString("title");
                                description=object.getString("description");
                                url=object.getString("url");
                                urlToImage=object.getString("urlToImage");
                                publishedAt=object.getString("publishedAt");
                                content=object.getString("content");

                                Articles article = new Articles(source_name, author, title, description, url, urlToImage,publishedAt, content);
                                articlesArrayList.add(article);
                            }
                        } catch (Exception e){

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                    }
                });
        queue.add(arrayRequest);
    }
}
