package com.angogasapps.myfamily.firebase;


import android.graphics.Bitmap;

import com.angogasapps.myfamily.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;


public class FirebaseVarsAndConsts {

    public static final String FIREBASE_URL = "https://myfamily-1601b.firebaseio.com";
    public static final String FIREBASE_DYNAMIC_LINKS_HOST = "https://angogasapps.page.link";
    public static final String FIREBASE_DYNAMIC_LINKS_HOST_FAMILY_INVITE = "https://angogasapps.page.link/families";

    public static FirebaseAuth AUTH;
    public static DatabaseReference DATABASE_ROOT;
    public static StorageReference STORAGE_ROOT;
    public static FirebaseDynamicLinks DYNAMIC_LINK_ROOT;
    public static User USER = new User();
    public static String UID;

//    public static volatile HashMap<String, User> familyMembersMap = new HashMap<>();
    public static volatile HashMap<String, Bitmap> chatImageMessangesMap = new HashMap<>();
    public static volatile Bitmap familyEmblemImage = null;

    public static final String TYPE_NODE = "node";

    public static final String TYPE_TEXT_MESSAGE = "message";
    public static final String TYPE_IMAGE_MESSAGE = "image";
    public static final String TYPE_VOICE_MESSAGE = "voice";

    public static final String NODE_USERS = "users";
    public static final String NODE_FAMILIES = "families";
    public static final String NODE_CHAT = "chat";
    public static final String NODE_BUY_LIST = "buy_list";
    public static final String NODE_NEWS = "news";
    public static final String NODE_CLOCK = "family_clock";

    public static final String CHILD_ROOT_NODE = "root_node";
    public static final String NODE_IMAGE_STORAGE = "image_storage";
    public static final String NODE_FILE_STORAGE = "file_storage";
    public static final String NODE_VIDEO_STORAGE = "video_storage";
    public static final String NODE_NOTE_STORAGE = "note_storage";

    public static final String ROOT_FOLDER = "root_folder";

    public static final String DATA = "data";

    public static final String CHILD_ID = "id";
    public static final String CHILD_PHONE = "phone";
    public static final String CHILD_FAMILY = "family";
    public static final String CHILD_NAME = "name";
    public static final String CHILD_TOKEN = "token";
    public static final String CHILD_BIRTHDAY = "birthday";
    public static final String CHILD_PHOTO_URL = "photoURL";
    public static final String CHILD_TYPE = "type";
    public static final String CHILD_FROM = "from";
    public static final String CHILD_VALUE = "value";
    public static final String CHILD_TIME = "time";

    public static final String CHILD_PRODUCTS = "products";
    public static final String CHILD_COMMENT = "comment";

    public static final String CHILD_FROM_PHONE = "fromPhone";
    public static final String CHILD_TIME_CREATE = "timeCreate";

//    public static final String CHILD_PHOTO_FAMILY_STORAGE = "family_photo_storage";
    public static final String CHILD_BASE_FOLDER = "base_folder";

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
