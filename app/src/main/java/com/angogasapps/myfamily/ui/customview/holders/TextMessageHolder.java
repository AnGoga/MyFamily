package com.angogasapps.myfamily.ui.customview.holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class TextMessageHolder extends AppBaseViewHolder{

    public TextMessageHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    protected void initLeftFields() {
        super.initLeftFields();
        ((TextView)view.findViewById(R.id.leftMessageText)).setText(value);
    }

    @Override
    protected void initRightFields() {
        super.initRightFields();
        ((TextView)view.findViewById(R.id.rightMessageText)).setText(value);
    }
}
