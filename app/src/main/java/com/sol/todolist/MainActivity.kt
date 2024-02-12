package com.sol.todolist

import CustomDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.sol.todolist.room.AppDatabase
import androidx.activity.viewModels
import androidx.fragment.app.DialogFragment

class MainActivity : AppCompatActivity(), ItemAddListener, OnItemClick {

    private val mMainViewModel: MainViewModel by viewModels()

    private lateinit var stubContainer: LinearLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerview: RecyclerView
    private lateinit var adapter: CustomAdapter // Declare adapter as a member variable
    private lateinit var todoLiveData: LiveData<List<ToDoItem>>
    private lateinit var data: List<ToDoItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val deleteIcon = ContextCompat.getDrawable(this, R.drawable.delete)
        val intrinsicWidth = deleteIcon?.intrinsicWidth
        val intrinsicHeight = deleteIcon?.intrinsicHeight
        val background = ColorDrawable()
        val backgroundColor = Color.parseColor("#f44336")
        val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

        // getting the recyclerview by its id
        recyclerview = findViewById<RecyclerView>(R.id.main_recycler_view)
        stubContainer = findViewById(R.id.main_no_items_container)
        fab = findViewById(R.id.main_fab)

        fab.setOnClickListener {
            val dialogFragment = CustomDialog(this, WhatItemEnum.FAB_BUTTON, null)
            //dialog.setItemAddListener(this)
            //dialog.show()
            dialogFragment.show(supportFragmentManager, "Custom Dialog")
        }

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // This will pass the ArrayList to our Adapter
        adapter = CustomAdapter(mutableListOf(), this) // Initialize the adapter
        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        // TODO GET ALL DATA QUERY
        mMainViewModel.getAllItems()
        mMainViewModel.todoItemListResult.observe(this, Observer {
            adapter.updateList(it)
            data = it

            screenDataValidation(it)

            Log.d("roomcheck", "-> $it")
        })

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // this method is called
                // when the item is moved.
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                val delItem: ToDoItem =
                    data.toMutableList().get(viewHolder.adapterPosition)

                // below line is to get the position
                // of the item at that position.
                val position = viewHolder.adapterPosition

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                data.toMutableList().removeAt(viewHolder.adapterPosition)

                // below line is to notify our item is removed from adapter.
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                // below line is to display our snackbar with action.
                // below line is to display our snackbar with action.
                // below line is to display our snackbar with action.
                Snackbar.make(recyclerview, "Deleted " + delItem.title, Snackbar.LENGTH_LONG)
                    .setAction(
                        "Undo",
                        View.OnClickListener {
                            // adding on click listener to our action of snack bar.
                            // below line is to add our item to array list with a position.
                            data.toMutableList().add(position, delItem)

                            // below line is to notify item is
                            // added to our adapter class.
                            adapter.notifyItemInserted(position)

                            //TODO
                            //db.todoDao().insertItem(delItem)
                        }).show()
                deleteItem(delItem)
            }
            // at last we are adding this
            // to our recycler view.

            // Let's draw our delete view
            override fun onChildDraw(canvas: Canvas,
                                     recyclerView: RecyclerView,
                                     viewHolder: RecyclerView.ViewHolder,
                                     dX: Float,
                                     dY: Float,
                                     actionState: Int,
                                     isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                val itemHeight = itemView.bottom - itemView.top

                // Draw the red delete background
                background.color = backgroundColor
                background.setBounds(
                    itemView.right + dX.toInt(),
                    itemView.top,
                    itemView.right,
                    itemView.bottom
                )
                background.draw(canvas)

                // Calculate position of delete icon
                val iconTop = itemView.top + (itemHeight - intrinsicHeight!!) / 2
                val iconMargin = (itemHeight - intrinsicHeight) / 2
                val iconLeft = itemView.right - iconMargin - intrinsicWidth!!
                val iconRight = itemView.right - iconMargin
                val iconBottom = iconTop + intrinsicHeight

                // Draw the delete icon
                deleteIcon?.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                deleteIcon?.draw(canvas)

                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }).attachToRecyclerView(recyclerview)
    }

    private fun screenDataValidation(toDoItems: List<ToDoItem>) {
        if (toDoItems.isEmpty()) {
            stubContainer.visibility = VISIBLE
            recyclerview.visibility = INVISIBLE
        } else {
            stubContainer.visibility = INVISIBLE
            recyclerview.visibility = VISIBLE
        }
    }

    override fun addItem(item: ToDoItem) {
        stubContainer.visibility = INVISIBLE
        recyclerview.visibility = VISIBLE
        mMainViewModel.insertItem(item)
        //TODO INSERT IN ROOM ITEM
        //db.todoDao().insertItem(item)
    }

    override fun updateItem(item: ToDoItem) {
        mMainViewModel.updateItem(item)
        //TODO UPDATE IN ROOM ITEM
        //db.todoDao().updateItem(item)
    }

    override fun itemClicked(item: ToDoItem) {
            val dialogFragment = CustomDialog(this, WhatItemEnum.ITEM, item)
            //dialog.setItemAddListener(this)
            //dialog.show()
            dialogFragment.show(supportFragmentManager, "Custom Dialog")


        Log.d("ItemClicked", "item: $item")
    }

    override fun deleteItem(item: ToDoItem) {
        //TODO DELETE IN ROOM ITEM
        mMainViewModel.deleteItem(item)
        //db.todoDao().deleteItem(item)
    }

}