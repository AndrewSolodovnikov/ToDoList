package com.sol.todolist

import CustomDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sol.todolist.room.AppDatabase

class MainActivity : AppCompatActivity(), ItemAddListener {
    private lateinit var stubContainer: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: CustomAdapter // Declare adapter as a member variable
    private lateinit var db: AppDatabase
    private lateinit var todoLiveData: LiveData<List<ToDoItem>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        recyclerview = findViewById<RecyclerView>(R.id.main_recycler_view)
        stubContainer = findViewById(R.id.main_no_items_container)
        fab = findViewById(R.id.main_fab)

        fab.setOnClickListener {
            val dialog = CustomDialog(this)
            dialog.setItemAddListener(this)
            dialog.show()
        }

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // This will pass the ArrayList to our Adapter
        adapter = CustomAdapter(mutableListOf()) // Initialize the adapter
        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        //Room
        db = Room.databaseBuilder(
            applicationContext, AppDatabase::class.java, "database-name")
            .allowMainThreadQueries()
            .build()

        todoLiveData = db.todoDao().getAllItems()

        todoLiveData.observe(this, Observer {
            adapter.updateList(it)

            if (it.isEmpty()) {
                stubContainer.visibility = VISIBLE
                recyclerview.visibility = INVISIBLE
            } else {
                stubContainer.visibility = INVISIBLE
                recyclerview.visibility = VISIBLE
            }

            Log.d("roomcheck", "-> $it")
        })

        //val todoDao = db.todoDao()
        //val todo: List<ToDoItem> = todoDao.getAllItems()
    }

    override fun addItem(item: ToDoItem) {
        stubContainer.visibility = INVISIBLE
        recyclerview.visibility = VISIBLE
        db.todoDao().insertItem(item)
    }
}