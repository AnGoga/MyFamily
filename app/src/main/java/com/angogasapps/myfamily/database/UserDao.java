package com.angogasapps.myfamily.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM TransactionUser")
    List<TransactionUser> getAll();

    @Query("SELECT * FROM TransactionUser WHERE id = :id")
    TransactionUser getById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TransactionUser user);

    @Update
    void update(TransactionUser user);

    @Delete
    void delete(TransactionUser user);
}
