package com.angogasapps.myfamily.ui.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.ui.customview.holders.AppBaseViewHolder;
import com.angogasapps.myfamily.ui.customview.holders.ImageMessageHolder;
import com.angogasapps.myfamily.ui.customview.holders.TextMessageHolder;
import com.angogasapps.myfamily.utils.ChatAdapterUtils;
import com.angogasapps.myfamily.utils.StringFormater;

import java.util.ArrayList;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.TYPE_IMAGE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.TYPE_TEXT_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.UID;
import static com.angogasapps.myfamily.utils.WithUsers.*;

public class ChatAdapter extends RecyclerView.Adapter<AppBaseViewHolder> {

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
    public AppBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new TextMessageHolder(inflater.inflate(R.layout.text_messsage_layout, parent, false));
        else if (viewType == 1)
            return  new ImageMessageHolder(inflater.inflate(R.layout.image_message_holder, parent, false));
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull AppBaseViewHolder holder, int position) {

        String typeOfThisElement = messagesList.get(position).getType();

        if (messagesList.get(position).getFrom().equals(UID)) {
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.INVISIBLE);
            holder.fromName = holder.view.findViewById(R.id.rightMessageFromName);
            holder.timestampText = holder.view.findViewById(R.id.rightMessageTime);

            if (typeOfThisElement.equals(TYPE_TEXT_MESSAGE))
                ((TextMessageHolder)holder).text = holder.view.findViewById(R.id.rightMessageText);
            else if (typeOfThisElement.equals(TYPE_IMAGE_MESSAGE))
                ((ImageMessageHolder)holder).imageView = holder.view.findViewById(R.id.rightMessageImage);

        } else {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.INVISIBLE);
            holder.fromName = holder.view.findViewById(R.id.leftMessageFromName);
            holder.timestampText = holder.view.findViewById(R.id.leftMessageTime);

            if (typeOfThisElement.equals(TYPE_TEXT_MESSAGE))
                ((TextMessageHolder)holder).text = holder.view.findViewById(R.id.leftMessageText);
            else if (typeOfThisElement.equals(TYPE_IMAGE_MESSAGE))
                ((ImageMessageHolder)holder).imageView = holder.view.findViewById(R.id.leftMessageImage);



            holder.userAvatar.setImageBitmap(getMemberImageById(messagesList.get(position).getFrom(), this.context));
        }

        holder.fromName.setText(getMemberNameById(messagesList.get(position).getFrom()));
        holder.timestampText.setText(StringFormater.formatLongToTime(messagesList.get(position).getTime()));

        if (typeOfThisElement.equals(TYPE_TEXT_MESSAGE))
            ((TextMessageHolder)holder).text.setText(messagesList.get(position).getValue().toString());
        else if (typeOfThisElement.equals(TYPE_IMAGE_MESSAGE))
            ((ImageMessageHolder)holder).imageView.setImageBitmap(null);
    }


    @Override
    public int getItemViewType(int position) {
        switch (messagesList.get(position).getType()) {
            case TYPE_TEXT_MESSAGE:
                return 0;
            case TYPE_IMAGE_MESSAGE:
                return 1;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }


    public void addMessage(Message message, boolean scrollToBottom){
        boolean isContains = false;
        for (Message element : messagesList){
            if (element.equals(message))
                isContains = true;
        }

        messagesList = ChatAdapterUtils.sortMessagesList(messagesList);

        if (!isContains) {
            if (!scrollToBottom) {
                messagesList.add(0, message);
                notifyItemInserted(0);
            } else {
                messagesList.add(message);
                notifyItemInserted(messagesList.size() - 1);
            }
        }
    }
}

