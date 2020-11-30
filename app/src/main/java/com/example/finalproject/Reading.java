package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.model.Article;
import com.squareup.picasso.Picasso;

public class Reading extends AppCompatActivity {
    private ImageView readingImage;
    private ProgressBar readingProgressbar;
    private Button backBtn;
    private TextView readingTitle, readingAuthor, readingNewsDate, readingContent, readingSource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_new);
        InitView();

        Intent i = getIntent();
        Article article = (Article)i.getSerializableExtra("article");

        Picasso.get().load(article.getUrlToImage()).into(readingImage);
        readingProgressbar.setVisibility(View.GONE);

        readingTitle.setText(article.getTitle());
        readingAuthor.setText(article.getAuthor());
        readingNewsDate.setText(article.getPublishedAt());
        readingSource.setText(article.getSource().getName());
        readingContent.setText(article.getDescription());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reading.this.finish();
            }
        });
    }

    private void InitView(){
        readingImage = findViewById(R.id.readingImage);
        readingProgressbar = findViewById(R.id.readingProgressbar);
        backBtn = findViewById(R.id.backBtn);
        readingTitle = findViewById(R.id.readingTitle);
        readingAuthor = findViewById(R.id.readingAuthor);
        readingNewsDate = findViewById(R.id.readingNewsDate);
        readingSource = findViewById(R.id.readingSource);
        readingContent = findViewById(R.id.readingContent);
    }
}
