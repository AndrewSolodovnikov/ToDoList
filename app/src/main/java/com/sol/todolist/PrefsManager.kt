package com.sol.todolist

interface PrefsManager {
    abstract fun getTodoItem() : ToDoItem
    fun saveDataInPrefs(key: String, value: Any)
}