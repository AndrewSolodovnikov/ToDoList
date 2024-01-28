package com.sol.todolist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ToDoItem(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "task_title") val title: String,
    @ColumnInfo(name = "task_description") val description: String,
    @ColumnInfo(name = "task_number") val number: Int
)
