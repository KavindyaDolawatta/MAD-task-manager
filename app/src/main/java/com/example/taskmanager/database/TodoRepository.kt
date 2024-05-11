package com.example.taskmanager.database

class TodoRepository(
    private val db:TodoDatabase
) {

    suspend fun insert(toDo: ToDo) = db.getTodoDao().insert(toDo)
    suspend fun delete(toDo: ToDo) = db.getTodoDao().delete(toDo)

    fun getAllTodoItems():List<ToDo> = db.getTodoDao().getAllTodoItems()
}