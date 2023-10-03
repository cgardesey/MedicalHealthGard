package com.prepeez.medicalhealthguard.other;


import androidx.room.Dao;
import androidx.room.Insert;

@Dao
public interface MyDao {
    @Insert
    public void addUser(MyUser myUser);
}
