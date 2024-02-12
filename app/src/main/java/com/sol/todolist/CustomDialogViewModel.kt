package com.sol.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sol.todolist.data.PrefsManagerImpl

class CustomDialogViewModel(app: Application) : AndroidViewModel(app) {
    private val prefsManager: PrefsManager = PrefsManagerImpl(app)

    private val todoItemList: MutableLiveData<ToDoItem> = MutableLiveData()
    val todoItemListResult: LiveData<ToDoItem> = todoItemList
    fun getTodoItemFromPrefs() {
        val result = prefsManager.getTodoItem()
        todoItemList.postValue(result)
    }

    fun saveDataInPrefs(key: String, value: Any) {
        prefsManager.saveDataInPrefs(key, value)
    }

}