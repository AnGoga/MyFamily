package com.angogasapps.myfamily.ui.customview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.ui.customview.holders.MessageHolder;
import com.angogasapps.myfamily.utils.ChatAdapterUtils;
import com.angogasapps.myfamily.utils.StringFormater;

import java.util.ArrayList;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.UID;
import static com.angogasapps.myfamily.utils.WithUsers.*;

public class ChatAdapter extends RecyclerView.Adapter<MessageHolder> {

    private ArrayList<Message> messagesList;
    private Context context;
    private LayoutInflater inflater;

    public ChatAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.messagesList = messageArrayList;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.messsage_layout, parent, false);
        return new MessageHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {

        if (messagesList.get(position).getFrom().equals(UID)) {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.INVISIBLE);
            holder.fromName = holder.view.findViewById(R.id.rightMessageFromName);
            holder.text = holder.view.findViewById(R.id.rightMessageText);
            holder.timestampText = holder.view.findViewById(R.id.rightMessageTime);
        } else {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.INVISIBLE);
            holder.fromName = holder.view.findViewById(R.id.leftMessageFromName);
            holder.text = holder.view.findViewById(R.id.leftMessageText);
            holder.timestampText = holder.view.findViewById(R.id.leftMessageTime);
            holder.userAvatar = holder.view.findViewById(R.id.messageUserAvatar);

            holder.userAvatar.setImageBitmap(getMemberImageById(messagesList.get(position).getFrom(), this.context));
        }

        holder.fromName.setText(getMemberNameById(messagesList.get(position).getFrom()));
        //object -> text <- image
        holder.text.setText(messagesList.get(position).getValue().toString());
        //time
        holder.timestampText.setText(StringFormater.formatLongToTime(messagesList.get(position).getTime()));


        Log.i("tag", getMemberNameById(messagesList.get(position).getFrom()));

    }


    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    public void setMessanges(ArrayList<Message> messageArrayList){
        this.messagesList = messageArrayList;
        //from -> to
        notifyDataSetChanged();
    }

    public void addMessage(Message message, boolean scrollToTop){
        boolean isContains = false;
        for (Message element : messagesList){
            if (element.equals(message))
                isContains = true;
        }

        messagesList = ChatAdapterUtils.sortMessagesList(messagesList);

        if (!isContains) {
            if (!scrollToTop) {
                messagesList.add(0, message);
                notifyItemInserted(0);
            } else {
                messagesList.add(message);
                notifyItemInserted(messagesList.size() - 1);
            }
        }
    }


}
