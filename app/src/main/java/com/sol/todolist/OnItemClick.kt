package com.sol.todolist

interface OnItemClick {
    fun itemClicked(item: ToDoItem)
    fun deleteItem(item: ToDoItem)
}