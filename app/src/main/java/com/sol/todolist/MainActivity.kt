package com.sol.todolist

import CustomDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.OnClickListener
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var stubContainer: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerview: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        recyclerview = findViewById<RecyclerView>(R.id.main_recycler_view)
        stubContainer = findViewById(R.id.main_no_items_container)
        fab = findViewById(R.id.main_fab)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ToDoItem>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (item in 1..50) {
            data.add(ToDoItem("title", "description", item))
        }

        if (data.isEmpty()) {
            stubContainer.visibility = VISIBLE
            recyclerview.visibility = INVISIBLE
        } else {
            stubContainer.visibility = INVISIBLE
            recyclerview.visibility = VISIBLE
        }

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        fab.setOnClickListener(View.OnClickListener {
            val customDialog = CustomDialog(this) {
                "Add new task"
            }
            customDialog.show()
            //adapter.addItems(ToDoItem("New item", "Add new item", 444))
            Log.d("ClickFAB", "Click FAB button")
        })
    }
}