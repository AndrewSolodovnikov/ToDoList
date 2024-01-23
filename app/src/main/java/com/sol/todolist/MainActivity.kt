package com.sol.todolist

import CustomDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), ItemAddListener {
    private lateinit var stubContainer: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: CustomAdapter // Declare adapter as a member variable

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

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ToDoItem>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (item in 1..10) {
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
        adapter = CustomAdapter(data) // Initialize the adapter
        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter
    }

    override fun addItem(item: ToDoItem) {
        adapter.addItem(item)
    }
}
