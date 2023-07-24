package com.example.todolist;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

// interface means we only define  body and it's define on other  class

@Dao
public interface NoteDao {
     @Insert
    public  void insert(Note note);

     @Update
    public  void update(Note note);

     @Delete
    public  void delete(Note note);

     @Query("SELECT * FROM my_notes")
    public LiveData<List<Note>> getAllData( );
}
