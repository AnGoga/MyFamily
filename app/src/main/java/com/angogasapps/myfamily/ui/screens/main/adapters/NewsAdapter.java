package com.angogasapps.myfamily.ui.screens.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.models.NewsEvent;
import com.angogasapps.myfamily.models.NewsObject;
import com.angogasapps.myfamily.ui.screens.main.holders.BaseNewsViewHolder;
import com.angogasapps.myfamily.ui.screens.main.holders.TextNewsViewHolder;

import java.util.ArrayList;

import static com.angogasapps.myfamily.models.NewsObject.TYPE_IMAGE;
import static com.angogasapps.myfamily.models.NewsObject.TYPE_TEXT;
import static com.angogasapps.myfamily.models.NewsObject.TYPE_VIDEO;

public class NewsAdapter extends RecyclerView.Adapter<BaseNewsViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<NewsObject> newsList = new ArrayList<>();

    public NewsAdapter(Context context, ArrayList<NewsObject> newsList){
        this.newsList = newsList;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public BaseNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0){
            return new TextNewsViewHolder(context, inflater.inflate(R.layout.text_news_view_holder, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseNewsViewHolder holder, int position) {
        NewsObject object = newsList.get(position);
        holder.update(object);
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        String type = newsList.get(position).getType();
        if (type.equals(TYPE_TEXT)) return 0;
        if (type.equals(TYPE_IMAGE)) return 1;
        if (type.equals(TYPE_VIDEO)) return 2;
        return -1;
    }

    public void update(NewsEvent event){
        if (event.getEvent().equals(NewsEvent.ENewsEvents.added)){
            notifyItemChanged(event.getIndex());
            return;
        }
        if (event.getEvent().equals(NewsEvent.ENewsEvents.removed)){
            notifyItemRemoved(event.getIndex());
            return;
        }
        if (event.getEvent().equals(NewsEvent.ENewsEvents.changed)){
            notifyItemChanged(event.getIndex());
            return;
        }
    }
}
