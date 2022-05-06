package com.hos.imagepro.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.hos.imagepro.room.User;
import com.hos.imagepro.room.Volunteer;

@Database(entities = {User.class, Volunteer.class},version = 2,exportSchema = false)
public abstract class UserDatabase extends RoomDatabase
{
    public abstract UserDao getUserDao();
    public abstract VolunteerDao getVolunteerDao();

}
