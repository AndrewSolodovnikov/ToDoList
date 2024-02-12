package com.sol.todolist.data

import android.content.Context
import androidx.room.Room
import com.sol.todolist.RoomManager
import com.sol.todolist.ToDoItem
import com.sol.todolist.room.AppDatabase

/**
 * Use to manage work with Room
 */
class RoomManagerImpl(private val context: Context) : RoomManager {
    private var db = Room.databaseBuilder(
        context, AppDatabase::class.java, "database-name")
        .allowMainThreadQueries()
        .build()

    override fun getAllItems() : List<ToDoItem> {
        return db.todoDao().getAllItems()
    }

    override fun insertItem(item: ToDoItem) {
        db.todoDao().insertItem(item)
    }

    override fun updateItem(item: ToDoItem) {
        db.todoDao().updateItem(item)
    }

    override fun deleteItem(item: ToDoItem) {
        db.todoDao().deleteItem(item)
    }
}