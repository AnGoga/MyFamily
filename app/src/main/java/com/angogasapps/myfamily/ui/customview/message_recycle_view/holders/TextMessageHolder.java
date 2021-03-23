package com.angogasapps.myfamily.ui.customview.message_recycle_view.holders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.TextMessageHolderBinding;

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
