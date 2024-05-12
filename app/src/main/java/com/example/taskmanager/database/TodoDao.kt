package com.example.taskmanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(toDo: ToDo)
    @Delete
    suspend fun delete(toDo: ToDo)

    @Query("SELECT * FROM ToDo")
    fun getAllTodoItems():List<ToDo>


}