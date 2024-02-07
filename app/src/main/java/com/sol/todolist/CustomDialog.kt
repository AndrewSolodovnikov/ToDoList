import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.sol.todolist.ItemAddListener
import com.sol.todolist.MainActivity
import com.sol.todolist.R
import com.sol.todolist.ToDoItem
import com.sol.todolist.WhatItemEnum

class CustomDialog(private var activity: MainActivity,
                   private var whatItem: WhatItemEnum, private var item: ToDoItem?) :
    Dialog(activity), View.OnClickListener {

    private lateinit var okButton: Button
    private lateinit var cancelButton: Button

    private lateinit var dialogLabel: TextView
    private lateinit var inputFieldTitle : EditText
    private lateinit var inputFieldDescription : EditText
    private lateinit var inputFieldNumber : EditText

    //private lateinit var itemAddListener: ItemAddListener // Интерфейс для передачи данных в MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_template)
        inputFieldTitle = findViewById(R.id.dialog_input_title)

        initView()
        dialogSizeControl()

        if (whatItem == WhatItemEnum.ITEM) {
            updateExistingItem()
        }
    }

    private fun updateExistingItem() {
        dialogLabel.text = "Update Item"
        inputFieldTitle.setText(item?.title)
        inputFieldDescription.setText(item?.description)
        inputFieldNumber.setText(item?.number.toString())

        Log.d("Clickeddd", "updateExistingItem")

    }

    private fun dialogSizeControl() {
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(this.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        this.window?.attributes = lp
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.dialog_ok_button -> {
                okButtonClicker()
            }
            R.id.dialog_cancel_button -> {
                cancelButtonClicked()
            }
            else -> {
                elseBeenClicked()
            }
        }
    }

    private fun initView() {
        okButton = findViewById<Button>(R.id.dialog_ok_button)
        cancelButton = findViewById<Button>(R.id.dialog_cancel_button)
        okButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)
        inputFieldTitle = findViewById(R.id.dialog_input_title)
        inputFieldDescription = findViewById(R.id.dialog_input_description)
        inputFieldNumber = findViewById(R.id.dialog_input_number)
        dialogLabel = findViewById(R.id.dialogLabel)

    }

    private fun elseBeenClicked() {

    }

    private fun cancelButtonClicked() {
        dismiss()
    }

    private fun okButtonClicker() {
        when (whatItem) {
            WhatItemEnum.FAB_BUTTON -> {
                okNewItemBeenClicked()
            } WhatItemEnum.ITEM -> {
                okUpdateItemBeenClicked()
            }
        }

    }

    private fun okNewItemBeenClicked() {
        if (whatItem == WhatItemEnum.FAB_BUTTON) {
            createNewItem()
        } else {
            okUpdateItemBeenClicked()
        }
    }

    private fun okUpdateItemBeenClicked() {
        val inputTitleResult = inputFieldTitle.text.toString()
        val inputDescriptionResult = inputFieldDescription.text.toString()
        val inputNumberResult = inputFieldNumber.text.toString().toInt()

        activity.updateItem(ToDoItem
            (item?.id, inputTitleResult, inputDescriptionResult, inputNumberResult))

        dismiss()

    }

    private fun createNewItem() {
        val inputTitleResult = inputFieldTitle.text.toString()
        val inputDescriptionResult = inputFieldDescription.text.toString()
        val inputNumberResult = inputFieldNumber.text.toString().toInt()

        //itemAddListener
        activity.addItem(ToDoItem
            (null, inputTitleResult, inputDescriptionResult, inputNumberResult))

        Log.d("prefstesting", "clean prefs")
        inputFieldTitle.text.clear()
        inputFieldDescription.text.clear()
        inputFieldNumber.text.clear()

        dismiss()
    }

    override fun onStart() {
        super.onStart()
        if (whatItem == WhatItemEnum.FAB_BUTTON) {
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            val titleFromPrefs = sharedPref.getString("TitleKey", "")
            val descriptionFromPrefs = sharedPref.getString("DescriptionKey", "")
            val numberFromPrefs = sharedPref.getString("NumberKey", "")
            inputFieldTitle.setText(titleFromPrefs)
            inputFieldDescription.setText(descriptionFromPrefs)
            inputFieldNumber.setText(numberFromPrefs)
            Log.d("prefstesting", "onStart sharePref been called")
        }
    }

    override fun onStop() {
        super.onStop()
        if (whatItem == WhatItemEnum.FAB_BUTTON) {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            Log.d("prefstesting", "onStop been called")

            val inputTitleResult = inputFieldTitle.text.toString()
            val inputDescriptionResult = inputFieldDescription.text.toString()
            val inputNumberResult = inputFieldNumber.text.toString()

            with(sharedPref.edit()) {
                putString("TitleKey", inputTitleResult)
                putString("DescriptionKey", inputDescriptionResult)
                putString("NumberKey", inputNumberResult)
                apply()
                Log.d("prefstesting", "sharePref been applied")
            }
        }
}
    }