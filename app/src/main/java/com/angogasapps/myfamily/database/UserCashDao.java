package com.angogasapps.myfamily.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserCashDao {

    @Query("SELECT * FROM usercash")
    List<UserCash> getAll();

    @Query("SELECT * FROM usercash WHERE id = :id")
    UserCash getById(long id);

    @Insert
    void insert(UserCash employee);

    @Update
    void update(UserCash employee);

    @Delete
    void delete(UserCash employee);
}
