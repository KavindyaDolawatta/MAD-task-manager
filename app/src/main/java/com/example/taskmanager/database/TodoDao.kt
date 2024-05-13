package com.example.taskmanager.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {

    @Insert
    suspend fun insert(toDo: ToDo)
    @Delete
    suspend fun delete(toDo: ToDo)

    @Update
    suspend fun update(toDo: ToDo)

    @Query("SELECT * FROM ToDo")
    fun getAllTodoItems():List<ToDo>


}