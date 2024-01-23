import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.sol.todolist.ItemAddListener
import com.sol.todolist.MainActivity
import com.sol.todolist.R
import com.sol.todolist.ToDoItem

class CustomDialog(private var activity: MainActivity) :
    Dialog(activity), View.OnClickListener {

    private lateinit var okButton: Button
    private lateinit var cancelButton: Button

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
    }

    private fun elseBeenClicked() {

    }

    private fun cancelButtonClicked() {
        dismiss()
    }

    private fun okButtonClicker() {
        val inputTitleResult = inputFieldTitle.text.toString()
        val inputDescriptionResult = inputFieldDescription.text.toString()
        val inputNumberResult = inputFieldNumber.text.toString().toInt()

        itemAddListener.addItem(ToDoItem
            (inputTitleResult, inputDescriptionResult, inputNumberResult))
        dismiss()
    }

    // Метод для передачи интерфейса в диалог
    fun setItemAddListener(listener: ItemAddListener) {
        itemAddListener = listener
    }
}