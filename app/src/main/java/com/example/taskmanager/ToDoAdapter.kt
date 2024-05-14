package com.example.taskmanager

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.taskmanager.database.ToDo
import com.example.taskmanager.database.TodoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ToDoAdapter(items:List<ToDo>, repository: TodoRepository,
                  viewModel:MainActivityData): RecyclerView.Adapter<ToDoViewHolder>() {

    var context: Context? = null
    val items = items
    val repository = repository
    val viewModel = viewModel
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_task, parent, false)
        context = parent.context
        return ToDoViewHolder(view)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val currentTask = items[position]
        // Set task text
        holder.cbTodo.text = currentTask.item
        holder.cbTodo.text = items.get(position).item

        holder.ivDelete.setOnClickListener {
            val isChecked = holder.cbTodo.isChecked
            if (isChecked) {
                CoroutineScope(Dispatchers.IO).launch {
                    repository.delete(items.get(position))
                    val data = repository.getAllTodoItems()
                    withContext(Dispatchers.Main) {
                        viewModel.setData(data)
                    }
                }

            } else {
                Toast.makeText(context, "Select the item to be deleted", Toast.LENGTH_LONG).show()
            }
        }
// Edit button click listener
        holder.ivEdit.setOnClickListener {
            displayEditDialog(currentTask)

        }
    }

    private fun displayEditDialog(task: ToDo) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Edit Task")
        builder.setMessage("Edit the task below")

        val input = EditText(context)
        input.inputType = InputType.TYPE_CLASS_TEXT
        input.setText(task.item) // Pre-fill EditText with existing task text
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, which ->
            val editedItem = input.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                // Update task in the database
                task.item = editedItem
                repository.update(task)

                val data = repository.getAllTodoItems()
                withContext(Dispatchers.Main) {
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