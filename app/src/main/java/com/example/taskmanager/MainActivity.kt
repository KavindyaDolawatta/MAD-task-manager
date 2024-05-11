package com.example.taskmanager

import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.database.ToDo
import com.example.taskmanager.database.TodoDatabase
import com.example.taskmanager.database.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: ToDoAdapter
    private lateinit var viewModel: MainActivityData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView:RecyclerView = findViewById(R.id.rvTodoList)
        val repository =TodoRepository(TodoDatabase.getInstance(this))

        val viewModel = ViewModelProvider(this)[MainActivityData::class.java]


        viewModel.data.observe(this){
            adapter = ToDoAdapter(it, repository, viewModel)
            recyclerView.adapter=adapter
            recyclerView.layoutManager = LinearLayoutManager(this)
        }

        CoroutineScope(Dispatchers.IO).launch {
            val data = repository.getAllTodoItems()

            runOnUiThread{
                viewModel.setData(data)
            }
        }
    val addItem: Button = findViewById(R.id.btnAddItem)

        addItem.setOnClickListener{
            displayAlert(repository)
        }
    }

    private fun displayAlert(repository:TodoRepository) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(getText(R.string.alertTitle))
        builder.setMessage("Enter the new task below")

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)


        builder.setPositiveButton("OK") { dialog, which ->
// Get the input text and display a Toast message
            val item = input.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                repository.insert(ToDo(item))
                val data = repository.getAllTodoItems()
                runOnUiThread{
                    viewModel.setData(data)
                }
            }
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

}
