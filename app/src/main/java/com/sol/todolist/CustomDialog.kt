import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.sol.todolist.R

class CustomDialog(context: Context, private val onConfirmClick: (String) -> Unit) : Dialog(context) {

    private lateinit var editText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)

        val titleTextView: TextView = findViewById(R.id.dialogTitle)
        titleTextView.text = "Add new task"

        editText = findViewById(R.id.editText)

        val btnConfirm: Button = findViewById(R.id.btnConfirm)
        btnConfirm.setOnClickListener {
            val enteredText = editText.text.toString()
            Toast.makeText(context, "Work add", Toast.LENGTH_LONG).show()
            onConfirmClick.invoke(enteredText)
            dismiss()
        }

        val btnCancel: Button = findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener {
            Toast.makeText(context, "You clicked cancel", Toast.LENGTH_LONG).show()
            dismiss()
        }
    }
}