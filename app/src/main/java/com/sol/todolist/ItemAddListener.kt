package com.sol.todolist

interface ItemAddListener {
    fun addItem(item: ToDoItem)
    fun updateItem(item: ToDoItem)
}