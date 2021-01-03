package com.angogasapps.myfamily.ui.customview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.objects.Message;
import com.angogasapps.myfamily.utils.StringFormater;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.angogasapps.myfamily.firebase.FirebaseHelper.UID;
import static com.angogasapps.myfamily.firebase.FirebaseHelper.familyMembersMap;
import static com.angogasapps.myfamily.utils.WithUsers.*;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageHolder> {

    private ArrayList<Message> messages;
    private Context context;
    private LayoutInflater inflater;

    public ChatAdapter(Context context, ArrayList<Message> messageArrayList) {
        this.messages = messageArrayList;
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
    }

    @NonNull
    @Override
    public ChatAdapter.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = inflater.inflate(R.layout.messsage_layout, parent, false);
        return new MessageHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {

        if (messages.get(position).getFrom().equals(UID)) {
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

            holder.userAvatar.setImageBitmap(getMemberImageById(messages.get(position).getFrom(), this.context));
        }

        holder.fromName.setText(getMemberNameById(messages.get(position).getFrom()));
        //object -> text <- image
        holder.text.setText(messages.get(position).getValue().toString());
        //time
        holder.timestampText.setText(StringFormater.formatLongToTime(messages.get(position).getTime()));


        Log.i("tag", getMemberNameById(messages.get(position).getFrom()));

    }


    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void setMessanges(ArrayList<Message> messageArrayList){
        this.messages = messageArrayList;
        //from -> to
        notifyDataSetChanged();
    }

    public static class MessageHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout, rightLayout;
        TextView fromName, text, timestampText;
        CircleImageView userAvatar;
        View view;
        public MessageHolder(@NonNull View itemView) {
            super(itemView);
            leftLayout = itemView.findViewById(R.id.leftChatLayout);
            rightLayout = itemView.findViewById(R.id.rightChatLayout);
            view = itemView;
        }
    }
}
