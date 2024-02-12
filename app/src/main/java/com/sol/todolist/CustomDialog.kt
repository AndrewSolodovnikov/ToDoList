import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.sol.todolist.CustomDialogViewModel
import com.sol.todolist.MainActivity
import com.sol.todolist.R
import com.sol.todolist.ToDoItem
import com.sol.todolist.WhatItemEnum

class CustomDialog(private var activity: MainActivity,
                   private var whatItem: WhatItemEnum, private var item: ToDoItem?) :
    DialogFragment(), View.OnClickListener {

    private val mCustomDialogViewModel: CustomDialogViewModel by activityViewModels()

    private lateinit var okButton: Button
    private lateinit var cancelButton: Button

    private lateinit var dialogLabel: TextView
    private lateinit var inputFieldTitle : EditText
    private lateinit var inputFieldDescription : EditText
    private lateinit var inputFieldNumber : EditText

    //private lateinit var itemAddListener: ItemAddListener // Интерфейс для передачи данных в MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.dialog_template, container, false)

        initView(view)
        dialogSizeControl()

        if (whatItem == WhatItemEnum.ITEM) {
            updateExistingItem()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        dialogSizeControl()
    }

    private fun updateExistingItem() {
        dialogLabel.text = "Update Item"
        inputFieldTitle.setText(item?.title)
        inputFieldDescription.setText(item?.description)
        inputFieldNumber.setText(item?.number.toString())

        Log.d("Clickeddd", "updateExistingItem")

    }

    private fun dialogSizeControl() {
        val params: ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ActionBar.LayoutParams.WRAP_CONTENT
        params.height = ActionBar.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as WindowManager.LayoutParams
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

    private fun initView(view: View) {
        inputFieldTitle = view.findViewById(R.id.dialog_input_title)
        okButton = view.findViewById<Button>(R.id.dialog_ok_button)
        cancelButton = view.findViewById<Button>(R.id.dialog_cancel_button)
        okButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)
        inputFieldTitle = view.findViewById(R.id.dialog_input_title)
        inputFieldDescription = view.findViewById(R.id.dialog_input_description)
        inputFieldNumber = view.findViewById(R.id.dialog_input_number)
        dialogLabel = view.findViewById(R.id.dialogLabel)

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
        mCustomDialogViewModel.getTodoItemFromPrefs()

        //TODO НЕ ПЕРЕНОСИТСЯ В ONRESUME 54 МИН УРОК 5 MVVM
        if (whatItem == WhatItemEnum.FAB_BUTTON) {
            mCustomDialogViewModel.todoItemListResult.observe(this, Observer {
                inputFieldTitle.setText(it.title)
                inputFieldDescription.setText(it.description)
                inputFieldNumber.setText(it.number)
                Log.d("roomcheck", "-> $it")
            })

            /*
            //val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            val titleFromPrefs = mCustomDialogViewModel.getTodoItemFromPrefs("TitleKey")
            val descriptionFromPrefs = mCustomDialogViewModel.getTodoItemFromPrefs("DescriptionKey")
            val numberFromPrefs = mCustomDialogViewModel.getTodoItemFromPrefs("NumberKey")
            inputFieldTitle.setText(titleFromPrefs)
            inputFieldDescription.setText(descriptionFromPrefs)
            inputFieldNumber.setText(numberFromPrefs)
            Log.d("prefstesting", "onStart sharePref been called")

             */
        }
    }

    override fun onStop() {
        super.onStop()
        if (whatItem == WhatItemEnum.FAB_BUTTON) {
            val inputTitleResult = inputFieldTitle.text.toString()
            val inputDescriptionResult = inputFieldDescription.text.toString()
            val inputNumberResult = inputFieldNumber.text.toString().toInt()

            mCustomDialogViewModel.saveDataInPrefs("titleKey", inputTitleResult)
            mCustomDialogViewModel.saveDataInPrefs("descriptionKey", inputDescriptionResult)
            mCustomDialogViewModel.saveDataInPrefs("numberKey", inputNumberResult)

            /*
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            Log.d("prefstesting", "onStop been called")

            val inputTitleResult = inputFieldTitle.text.toString()
            val inputDescriptionResult = inputFieldDescription.text.toString()
            val inputNumberResult = inputFieldNumber.text.toString().toInt()

            with(sharedPref.edit()) {
                putString("TitleKey", inputTitleResult)
                putString("DescriptionKey", inputDescriptionResult)
                putInt("NumberKey", inputNumberResult)
                apply()
                Log.d("prefstesting", "sharePref been applied")
            }
             */

        }
}
    }