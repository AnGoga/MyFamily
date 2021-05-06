package com.angogasapps.myfamily.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.databinding.CardviewBinding;
import com.angogasapps.myfamily.models.MainCardState;


import android.view.LayoutInflater;
import android.widget.TextView;

public class CardView extends androidx.cardview.widget.CardView {
    public int mCardDrawable;
    public String mCardName;
    public String mCardSubscript;

    private TextView textName;
    private TextView textSubscript;
    private ImageView imageView;

    private OnClickListener plugClickListener = v -> {
        AppApplication.showInDevelopingToast();
    };


//    private CardviewBinding binding;

    public CardView(@NonNull Context context, int mCardDrawable, String mCardName, String mCardSubscript) {
        super(context);
        this.mCardDrawable = mCardDrawable;
        this.mCardName = mCardName;
        this.mCardSubscript = mCardSubscript;
    }

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
//        binding = CardviewBinding.inflate(inflater, this, false);

    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //set attributes
        textName = this.findViewById(R.id.cardViewTextViewTextName);
        textSubscript = this.findViewById(R.id.cardViewTextSubscript);
        imageView = this.findViewById(R.id.cardViewDrawable);

//        ((TextView)this.findViewById(R.id.cardViewTextViewTextName)).setText(mCardName);
//        ((TextView)this.findViewById(R.id.cardViewTextSubscript)).setText(mCardSubscript);
//        ((ImageView)this.findViewById(R.id.cardViewDrawable)).setBackgroundResource(mCardDrawable);
        update(mCardName, mCardSubscript, mCardDrawable);

    }
    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CardView);

        //init vars
        mCardName = typedArray.getString(R.styleable.CardView_itemCardName);
        mCardSubscript = typedArray.getString(R.styleable.CardView_itemCardSubscript);
        mCardDrawable = typedArray.getResourceId(R.styleable.CardView_itemCardDrawable, 0);

        typedArray.recycle();
    }

    private void update(String name, String subscribe, int drawable) {
//        binding.cardViewTextViewTextName.setText(name);
//        binding.cardViewTextSubscript.setText(subscribe);
//        binding.cardViewDrawable.setImageResource(drawable);

        textName.setText(name);
        textSubscript.setText(subscribe);
        imageView.setBackgroundResource(drawable);
    }

    public void update(MainCardState state){
        this.textName.setText(state.getCardName());
        this.textSubscript.setText(state.getCardSubscript());
        this.imageView.setBackgroundResource(state.getCardDrawable());
        if (state.getActivityClass() != null){
            getRootView().setOnClickListener(v -> {
                getContext().startActivity(new Intent(getContext(), state.getActivityClass()));
            });
        }else{
            getRootView().setOnClickListener(plugClickListener);
        }
    }
}
