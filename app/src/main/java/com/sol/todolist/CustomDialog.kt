import android.app.Dialog
import android.os.Bundle
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

class CustomDialog(private var activity: MainActivity, var isNewItem: Boolean, private var item: ToDoItem?) :
    Dialog(activity), View.OnClickListener {

    private lateinit var okButton: Button
    private lateinit var cancelButton: Button

    private lateinit var dialogLabel: TextView
    private lateinit var inputFieldTitle : EditText
    private lateinit var inputFieldDescription : EditText
    private lateinit var inputFieldNumber : EditText

    private lateinit var itemAddListener: ItemAddListener // Интерфейс для передачи данных в MainActivity


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_template)
        inputFieldTitle = findViewById(R.id.dialog_input_title)

        initView()
        dialogSizeControl()

        if (!isNewItem) {
            Log.d("Open dialog update", "updateExistingItem")
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
        if (isNewItem) {
            okNewItemBeenClicked()
        } else {
            okUpdateItemBeenClicked()
        }

    }

    private fun okNewItemBeenClicked() {
        if (isNewItem) {
            createNewItem()
        } else {
            Log.d("okUpdateee", "update item: $item")
            okUpdateItemBeenClicked()
        }
    }

    private fun okUpdateItemBeenClicked() {
        val inputTitleResult = inputFieldTitle.text.toString()
        val inputDescriptionResult = inputFieldDescription.text.toString()
        val inputNumberResult = inputFieldNumber.text.toString().toInt()

        Log.d("okUpdateItemBeenClickedUpdateee", "update item: $item")
        //itemAddListener
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
        dismiss()
        Log.d("createNewItemClickeddd", "createNewItem")

    }

    // Метод для передачи интерфейса в диалог
    fun setItemAddListener(listener: ItemAddListener) {
        itemAddListener = listener
    }
}