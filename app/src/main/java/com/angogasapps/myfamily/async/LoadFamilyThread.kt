package com.angogasapps.myfamily.async

import android.content.Context
import com.angogasapps.myfamily.models.User.Companion.from

import android.os.AsyncTask
import kotlin.jvm.Volatile
import android.graphics.Bitmap
import com.angogasapps.myfamily.async.LoadFamilyThread.FamilyDownloaderThread
import com.angogasapps.myfamily.async.LoadFamilyThread.FamilyEmblemDownloaderThread
import com.angogasapps.myfamily.async.LoadFamilyThread
import com.angogasapps.myfamily.async.LoadFamilyThread.MemberDownloaderThread
import com.angogasapps.myfamily.async.LoadFamilyThread.PhotoDownloaderThread
import android.graphics.BitmapFactory
import android.util.Log
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.angogasapps.myfamily.firebase.*
import com.angogasapps.myfamily.database.DatabaseManager
import com.angogasapps.myfamily.models.Family
import com.angogasapps.myfamily.models.User
import java.lang.Exception
import java.net.URL
import java.util.ArrayList
import java.util.HashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class LoadFamilyThread(private var context: Context) : AsyncTask<User?, Int?, ArrayList<User>?>() {
    @Volatile
    var familyMembersId = ArrayList<String>()

    @Volatile
    var familyMembersList = ArrayList<User>()

    @Volatile
    var usersRoleMap: HashMap<String, String> = HashMap()

    @Volatile
    var familyEmblemURL: String? = null

    @Volatile
    var familyEmblem: Bitmap? = null

    @Volatile
    lateinit var familyName: String

    @Volatile
    lateinit var pull: ExecutorService

    protected override fun doInBackground(vararg p0: User?): ArrayList<User>? {
        try {
            Log.v("tag", "Thread = " + Thread.currentThread())
            val familyDownloaderThread = FamilyDownloaderThread()
            val emblemDownloaderThread = FamilyEmblemDownloaderThread()
            familyDownloaderThread.start()
            familyDownloaderThread.join()
            downloadMembers()
            initUsersArray()
            downloadMembersPhoto()

//            ArrayList<User> a = familyMembersList;
            emblemDownloaderThread.start()
            initPhotos()
            if (emblemDownloaderThread.isAlive) emblemDownloaderThread.join()
            Log.i("load", "Загрузка завершена")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isEnd = true
        return null
    }

    private fun downloadMembers() {
        pull = Executors.newFixedThreadPool(2)
        val monitor = CountDownLatch(familyMembersId.size)
        for (i in familyMembersId.indices) {
            pull.submit(MemberDownloaderThread(familyMembersId[i], monitor))
        }
        pull.shutdown()
        try {
            monitor.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun downloadMembersPhoto() {
        pull = Executors.newFixedThreadPool(2)
        val monitor = CountDownLatch(familyMembersList.size)
        for (i in familyMembersList.indices) {
            pull.submit(PhotoDownloaderThread(familyMembersList[i].photoURL, i, monitor))
        }
        pull.shutdown()
        try {
            monitor.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun downloadBitmapByURL(url: String?): Bitmap? {
        try {
            val photoUrl = URL(url)
            val downloadStream = photoUrl.openStream()
            return BitmapFactory.decodeStream(downloadStream)
        } catch (e: Exception) {
            if (url != null) if (url != DEFAULT_URL) e.printStackTrace()
        }
        return null
    }

    private inner class FamilyDownloaderThread : Thread() {
        override fun run() {
            DATABASE_ROOT.child(NODE_FAMILIES).child(USER.family)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dataSnapshot in snapshot.child(CHILD_MEMBERS).children) {
                            familyMembersId.add(dataSnapshot.key!!)
                            val string = dataSnapshot.getValue(String::class.java)!!
                            usersRoleMap[dataSnapshot.key!!] = string
                        }
                        familyEmblemURL = snapshot.child(CHILD_EMBLEM).getValue(String::class.java)
                        familyName = snapshot.child(CHILD_NAME).getValue(String::class.java)!!
                        lock.countDown()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
            try {
                lock.await()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    private inner class MemberDownloaderThread(var memberId: String, var monitor: CountDownLatch) :
        Thread() {
        override fun run() {
            DATABASE_ROOT.child(NODE_USERS).child(memberId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
//                    familyMembersList.add(snapshot.getValue(User.class));
                        familyMembersList.add(from(snapshot))
                        Log.v("tag", "Thread = " + currentThread())
                        monitor.countDown()
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }
    }

    private inner class PhotoDownloaderThread(
        var photoUrl: String,
        var pos: Int,
        var monitor: CountDownLatch
    ) : Thread() {
        override fun run() {
            familyMembersList[pos].setBitmap(downloadBitmapByURL(photoUrl))
            monitor.countDown()
        }
    }

    private inner class FamilyEmblemDownloaderThread : Thread() {
        override fun run() {
            familyEmblem = downloadBitmapByURL(familyEmblemURL)
        }
    }

    private fun initPhotos() {
        familyEmblemImage = familyEmblem
        for (user in familyMembersList) {
            user.role = usersRoleMap[user.id]!!
        }
        //        Family.getInstance().setUsersList(familyMembersList);
        DatabaseManager.updateInfoForUsers(familyMembersList)
    }

    private fun initUsersArray() {
        Family.getInstance().usersList = familyMembersList
    }

    companion object {
        @Volatile
        var isEnd = false
        //    volatile static Lock lock = new ReentrantLock();
        @Volatile
        private var lock = CountDownLatch(1)
    }
}