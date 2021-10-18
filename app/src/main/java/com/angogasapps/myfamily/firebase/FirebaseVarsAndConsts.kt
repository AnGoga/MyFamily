package com.angogasapps.myfamily.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import kotlin.jvm.Volatile
import android.graphics.Bitmap
import com.angogasapps.myfamily.models.User
import java.util.HashMap



const val FIREBASE_URL = "https://myfamily-1601b.firebaseio.com"
const val FIREBASE_DYNAMIC_LINKS_HOST = "https://angogasapps.page.link"
const val FIREBASE_DYNAMIC_LINKS_HOST_FAMILY_INVITE = "https://angogasapps.page.link/families"

lateinit var AUTH: FirebaseAuth
lateinit var DATABASE_ROOT: DatabaseReference
lateinit var STORAGE_ROOT: StorageReference
lateinit var DYNAMIC_LINK_ROOT: FirebaseDynamicLinks

var USER = User()
lateinit var UID: String

@Volatile
var chatImageMessangesMap = HashMap<String, Bitmap>()

@JvmField
@Volatile
var familyEmblemImage: Bitmap? = null
const val TYPE_NODE = "node"
const val TYPE_TEXT_MESSAGE = "message"
const val TYPE_IMAGE_MESSAGE = "image"
const val TYPE_VOICE_MESSAGE = "voice"
const val TYPE_ALARM_CLOCK_MESSAGE = "alarm_clock_message"
const val NODE_USERS = "users"
const val NODE_FAMILIES = "families"
const val NODE_CHAT = "chat"
const val NODE_BUY_LIST = "buy_list"
const val NODE_NEWS = "news"
const val NODE_CLOCK = "family_clock"
const val CHILD_ROOT_NODE = "root_node"
const val NODE_IMAGE_STORAGE = "image_storage"
const val NODE_FILE_STORAGE = "file_storage"
const val NODE_VIDEO_STORAGE = "video_storage"
const val NODE_NOTE_STORAGE = "note_storage"
const val ROOT_FOLDER = "root_folder"
const val DATA = "data"
const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_FAMILY = "family"
const val CHILD_NAME = "name"
const val CHILD_TOKEN = "token"
const val CHILD_BIRTHDAY = "birthday"
const val CHILD_PHOTO_URL = "photoURL"
const val CHILD_TYPE = "type"
const val CHILD_FROM = "from"
const val CHILD_VALUE = "value"
const val CHILD_TIME = "time"
const val CHILD_TO = "to"
const val CHILD_FROM_ID = "fromId"
const val CHILD_OPTIONS = "options"
const val CHILD_DATA = "data"
const val CHILD_PRODUCTS = "products"
const val CHILD_COMMENT = "comment"
const val CHILD_FROM_PHONE = "fromPhone"
const val CHILD_TIME_CREATE = "timeCreate"

//    public static final String CHILD_PHOTO_FAMILY_STORAGE = "family_photo_storage";
const val CHILD_BASE_FOLDER = "base_folder"
const val CHILD_MESSANGES = "messanges"
const val CHILD_MEMBERS = "members"
const val CHILD_EMBLEM = "emblem"
const val CHILD_ROLE = "role"
const val ROLE_CREATOR = "creator"
const val ROLE_MEMBER = "member"
const val FOLDER_FAMILY_EMBLEMS = "family_emblems"
const val FOLDER_USERS_PHOTOS = "users_photos"
const val FOLDER_IMAGE_MESSAGE = "image_message"
const val FOLDER_VOICE_MESSAGE = "voice_message"
const val DEFAULT_URL = "default"
