package com.angogasapps.myfamily.objects;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.ui.screens.chat.ChatFragment;
import com.github.chrisbanes.photoview.PhotoView;

public class ChatImageShower {
    private AppCompatActivity context;
    private ImageShowerDialog dialog;

    public ChatImageShower(AppCompatActivity context){
        this.context = context;
        this.dialog = new ImageShowerDialog();
    }


    public void showImage(ImageView imageView){
        dialog.setImage(imageView);

        dialog.show(this.context.getSupportFragmentManager().beginTransaction(), ImageShowerDialog.TAG);

    }

    public void dismiss(){
        dialog.dismiss();
    }

    public static class ImageShowerDialog extends DialogFragment {
        public static final String TAG = "ImageShowerDialog";

        private PhotoView imageView;

        private ImageView sharedImageView;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            View rootView = inflater.inflate(R.layout.dialog_image_shower, container, false);

            imageView = rootView.findViewById(R.id.imageView);
//            imageView.setTransitionName(sharedImageView.getTransitionName());

            imageView.setImageBitmap(((BitmapDrawable)sharedImageView.getDrawable()).getBitmap());

            return rootView;
        }


        public void setImage(ImageView imageView){
            this.sharedImageView = imageView;
        }


        @Override
        public int show(@NonNull FragmentTransaction transaction, @Nullable String tag) {
//            transaction.addSharedElement(sharedImageView, sharedImageView.getTransitionName());
            return super.show(transaction, tag);
        }
    }
}
