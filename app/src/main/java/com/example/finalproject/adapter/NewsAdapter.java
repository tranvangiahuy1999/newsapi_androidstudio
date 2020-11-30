package com.example.finalproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Home;
import com.example.finalproject.R;
import com.example.finalproject.Reading;
import com.example.finalproject.Splash;
import com.example.finalproject.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private List<Article> articleList;
    Context context;

    public NewsAdapter(Context context, List<Article> articleList){
        this.context = context;
        this.articleList = articleList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listItem = inflater.inflate(R.layout.card_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Article article = articleList.get(position);

        holder.itemTitle.setText(article.getTitle());
        holder.itemAuthor.setText(article.getAuthor());
        holder.itemDate.setText(article.getPublishedAt());
        holder.itemProgressbar.setVisibility(View.GONE);
        if(article.getUrlToImage() != null && !article.getUrlToImage().equals("")){
            Picasso.get().load(article.getUrlToImage()).into(holder.itemImage);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent readingScreen = new Intent(context, Reading.class);
                readingScreen.putExtra("article", article);
                readingScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(readingScreen);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImage;
        TextView itemTitle, itemAuthor, itemDate;
        ProgressBar itemProgressbar;
        LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);

            this.linearLayout = itemView.findViewById(R.id.linearLayout);
            this.itemImage = itemView.findViewById(R.id.itemImage);
            this.itemTitle = itemView.findViewById(R.id.itemTitle);
            this.itemAuthor = itemView.findViewById(R.id.itemAuthor);
            this.itemProgressbar = itemView.findViewById(R.id.itemProgressbar);
            this.itemDate = itemView.findViewById(R.id.itemDate);
        }
    }
}
