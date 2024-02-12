package com.sol.todolist.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sol.todolist.ToDoItem

@Dao
interface ToDoDao {
    @Query("SELECT * FROM ToDoItem")
    fun getAllItems(): List<ToDoItem>

    @Insert
    fun insertItem(toDoItem: ToDoItem)

    @Delete
    fun deleteItem(toDoItem: ToDoItem)

    @Update
    fun updateItem(toDoItem: ToDoItem)
}