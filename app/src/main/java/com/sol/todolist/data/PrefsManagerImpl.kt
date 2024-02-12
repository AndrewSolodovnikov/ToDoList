package com.sol.todolist.data

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.sol.todolist.PrefsManager
import com.sol.todolist.ToDoItem

/**
 * Use to manage work with SharedPreference
 */
class PrefsManagerImpl(app: Application) : PrefsManager {
    private val sharedPref : SharedPreferences = app.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    override fun getTodoItem(): ToDoItem {
        val titleFromPrefs = sharedPref.getString("titleKey", "") ?: ""
        val descriptionFromPrefs = sharedPref.getString("descriptionKey", "") ?: ""
        val numberFromPrefs = sharedPref.getInt("numberKey", 0)?.toInt() ?: 0
        return ToDoItem(0, titleFromPrefs, descriptionFromPrefs, numberFromPrefs)
    }

    override fun saveDataInPrefs(key: String, value: Any) {
        with(sharedPref.edit()) {
            putString(key, value.toString())
            apply()
            Log.d("prefstesting", "sharePref been applied")
        }
    }

}