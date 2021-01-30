package com.angogasapps.myfamily.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;


import androidx.annotation.Nullable;

import com.angogasapps.myfamily.R;


import android.view.LayoutInflater;
import android.widget.TextView;

public class CardView extends androidx.cardview.widget.CardView {
    public int mCardDrawable;
    public String mCardName;
    public String mCardSubscript;

    public CardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttribute(context, attrs);
        initializeViews(context);
    }

    public CardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context, attrs);
        initializeViews(context);
    }
    private void initializeViews(Context context) {
        @SuppressLint("ServiceCast")
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.cardview, this);
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //set attributes
        ((TextView)this.findViewById(R.id.cardViewTextViewTextName)).setText(mCardName);
        ((TextView)this.findViewById(R.id.cardViewTextSubscript)).setText(mCardSubscript);
        ((ImageView)this.findViewById(R.id.cardViewDrawable)).setBackgroundResource(mCardDrawable);

    }
    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardView);

        //init vars
        mCardName = typedArray.getString(R.styleable.CardView_itemCardName);

        mCardSubscript = typedArray.getString(R.styleable.CardView_itemCardSubscript);

        mCardDrawable = typedArray.getResourceId(R.styleable.CardView_itemCardDrawable, 0);

        typedArray.recycle();
    }
}
