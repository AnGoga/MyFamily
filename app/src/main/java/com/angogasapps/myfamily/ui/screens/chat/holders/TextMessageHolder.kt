package com.angogasapps.myfamily.ui.screens.chat.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.angogasapps.myfamily.R;

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
