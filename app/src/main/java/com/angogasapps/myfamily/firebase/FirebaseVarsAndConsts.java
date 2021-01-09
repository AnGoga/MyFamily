package com.angogasapps.myfamily.firebase;


import android.graphics.Bitmap;

import com.angogasapps.myfamily.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;


public class FirebaseVarsAndConsts {
    public static FirebaseAuth AUTH;
    public static DatabaseReference DATABASE_ROOT;
    public static StorageReference STORAGE_ROOT;
    public static User USER;
    public static String UID;

    public static volatile HashMap<String, User> familyMembersMap = new HashMap<>();
    public static volatile HashMap<String, Bitmap> familyMembersImagesMap = new HashMap<>();
    public static volatile HashMap<String, String> familyMembersRolesMap = new HashMap<>();
    public static volatile HashMap<String, Bitmap> chatImageMessangesMap = new HashMap<>();


    public static final String TYPE_TEXT_MESSAGE = "message";
    public static final String TYPE_IMAGE_MESSAGE = "image";
    public static final String TYPE_VOICE_MESSAGE = "voice";

    //firebase realtime database nodes
    public static final String NODE_USERS = "users";
    public static final String NODE_FAMILIES = "families";
    public static final String NODE_CHAT = "chat";

    //firebase realtime database childes
    public static final String CHILD_ID = "id";
    public static final String CHILD_PHONE = "phone";
    public static final String CHILD_FAMILY = "family";
    public static final String CHILD_NAME = "name";
    public static final String CHILD_BIRTHDAY = "birthday";
    public static final String CHILD_PHOTO_URL = "photoURL";
    public static final String CHILD_TYPE = "type";
    public static final String CHILD_FROM = "from";
    public static final String CHILD_VALUE = "value";
    public static final String CHILD_TIME = "time";

    public static final String CHILD_MESSANGES = "messanges";
    public static final String CHILD_MEMBERS = "members";
    public static final String CHILD_EMBLEM = "emblem";
    public static final String CHILD_ROLE = "role";

    public static final String ROLE_CREATOR = "creator";
    public static final String ROLE_MEMBER = "member";

    public static final String FOLDER_FAMILY_EMBLEMS = "family_emblems";
    public static final String FOLDER_USERS_PHOTOS = "users_photos";
    public static final String FOLDER_IMAGE_MESSAGE = "image_message";
    public static final String FOLDER_VOICE_MESSAGE = "voice_message";

    public static final String DEFAULT_URL = "default";


}