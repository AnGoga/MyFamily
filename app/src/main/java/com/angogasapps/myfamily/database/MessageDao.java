package com.angogasapps.myfamily.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.angogasapps.myfamily.models.Message;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM Message")
    List<Message> getAll();

    @Query("SELECT * FROM Message WHERE id = :id")
    Message getById(String id);

//    @Insert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Message message);

    @Update
    void update(Message message);

    @Delete
    void delete(Message message);
}
