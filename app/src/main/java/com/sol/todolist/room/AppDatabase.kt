package com.sol.todolist.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sol.todolist.ToDoItem

@Database(entities = [ToDoItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): ToDoDao
}