package com.hos.imagepro.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hos.imagepro.data.UserDao;
import com.hos.imagepro.data.UserDatabase;
import com.hos.imagepro.room.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("Select * From  User where Name=:Name and Phone=:Phone")
    User getPerson(String Name, String Phone);

    @Insert
    public void insert(User user);

    @Update
    public void update(User user);

   @Query("Select * from User")
    public List<User>getAll();

   @Query("Select Name From User Where Phone=:Phone")
    public String getuser(String Phone);



}
