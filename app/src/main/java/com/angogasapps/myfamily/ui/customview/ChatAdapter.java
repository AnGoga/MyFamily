package com.angogasapps.myfamily.ui.customview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.ui.customview.holders.AppBaseViewHolder;
import com.angogasapps.myfamily.ui.customview.holders.ImageMessageHolder;
import com.angogasapps.myfamily.ui.customview.holders.TextMessageHolder;
import com.angogasapps.myfamily.ui.customview.holders.VoiceMessageHolder;
import com.angogasapps.myfamily.utils.ChatAdapterUtils;

import java.util.ArrayList;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_IMAGE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_TEXT_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.TYPE_VOICE_MESSAGE;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.UID;
import static com.angogasapps.myfamily.utils.WithMessages.arrayContainsMessage;

public class ChatAdapter extends RecyclerView.Adapter<AppBaseViewHolder> {

    private ArrayList<Message> messagesList;
    private Activity activity;
    private Context context;
    private LayoutInflater inflater;

    public ChatAdapter(Activity activity, ArrayList<Message> messageArrayList) {
        this.messagesList = messageArrayList;
        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.inflater = LayoutInflater.from(this.context);
    }

    @NonNull
    @Override
    public AppBaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new TextMessageHolder(inflater.inflate(R.layout.text_messsage_holder, parent, false));
        else if (viewType == 1)
            return  new ImageMessageHolder(inflater.inflate(R.layout.image_message_holder, parent, false));
        else if (viewType == 2)
            return new VoiceMessageHolder(inflater.inflate(R.layout.voice_message_holder, parent, false));
        return null;
    }


    @Override
    public void onBindViewHolder(@NonNull AppBaseViewHolder holder, int position) {
        Message message = messagesList.get(position);

        holder.init(message.getFrom(), message.getTime(), message.getId(), message.getValue(), activity);
        if (message.getFrom().equals(UID)){
            holder.initRightLayout();
        }else{
            holder.initLeftLayout();
        }
    }


    @Override
    public int getItemViewType(int position) {
        switch (messagesList.get(position).getType()) {
            case TYPE_TEXT_MESSAGE:
                return 0;
            case TYPE_IMAGE_MESSAGE:
                return 1;
            case TYPE_VOICE_MESSAGE:
                return 2;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }


    public void addMessage(Message message, boolean scrollToBottom){

        messagesList = ChatAdapterUtils.sortMessagesList(messagesList);

        if (!arrayContainsMessage(messagesList, message)) {
            if (scrollToBottom) {
                messagesList.add(message);
                notifyItemInserted(messagesList.size() - 1);
            } else {
                messagesList.add(0, message);
                notifyItemInserted(0);
            }
        }
    }
}

