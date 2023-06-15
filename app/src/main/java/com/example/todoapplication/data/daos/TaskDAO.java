package com.example.todoapplication.data.daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import com.example.todoapplication.data.models.Task;

import java.util.List;

@Dao
public interface TaskDAO {
    @Insert
    public void insert(Task... task);

    @Query("SELECT * FROM tasks")
    public List<Task> getTasks();

    @Query("SELECT * FROM tasks WHERE id = :id")
    public Task getTaskById(Long id);

    @Query("DELETE FROM tasks WHERE id = :id")
    public void deleteTaskById(Long id);

    @Update
    public void update(Task... task);
}
