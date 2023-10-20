package com.comunidadedevspace.taskbeats.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.comunidadedevspace.taskbeats.R
import com.comunidadedevspace.taskbeats.TaskBeatsApplication
import com.comunidadedevspace.taskbeats.data.local.Task
import com.google.android.material.snackbar.Snackbar

class TaskDetailActivity : AppCompatActivity() {

    private var task: Task? = null
    private lateinit var btnDone: Button

    val dataBaseInstance by lazy {
        (application as TaskBeatsApplication).getAppDataBase()
    }
    val dao by lazy {dataBaseInstance.taskDao()}

    private val factory = object : ViewModelProvider.Factory{

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TaskDetailViewModel(dao) as T
        }

    }
    private val viewModel: TaskDetailViewModel by viewModels {
        TaskDetailViewModel.getVMFactory(application)
    }
    companion object{
        private const val TASK_DETAIL_EXTRA = "task.extra.detail"

        fun start(context: Context, task: Task?): Intent {
            val intent = Intent(context, TaskDetailActivity::class.java)
                .apply {
                    putExtra(TASK_DETAIL_EXTRA, task)
                }
            return intent
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        setSupportActionBar(findViewById(R.id.toolbar))



        task = intent.getSerializableExtra(TASK_DETAIL_EXTRA) as Task?

        val edtTitle = findViewById<EditText>(R.id.edt_task_title)
        val edtDescription = findViewById<EditText>(R.id.edt_task_description)
        btnDone = findViewById<Button>(R.id.btn_done)

        if(task!= null){
            edtTitle.setText(task!!.title)
            edtDescription.setText(task!!.description)
        }

        btnDone.setOnClickListener{
            val title = edtTitle.text.toString()
            val desc = edtDescription.text.toString()

            if(title.isNotEmpty() && desc.isNotEmpty()){
                if (task == null) {
                addOrUpdateTask(0, title, desc, ActionType.CREATE)
                }else{
                    addOrUpdateTask(task!!.id, title, desc, ActionType.UPDATE)
                }
            }else{
                showMessage(it, "Fields are required")
            }
        }

        /*tvTitle = findViewById(R.id.tv_task_title_detail)
        tvTitle.text = task?.title ?: "Adicione uma tarefa"*/
    }
    private fun addOrUpdateTask(
        id: Int,
        title: String,
        description: String,
        actionType: ActionType
    ){
        val newTask = Task(id, title, description)
        perfomAction(newTask, actionType)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_task_detail, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.delete_task -> {

                if(task != null){
                    perfomAction(task!!, ActionType.DELETE)
                }else {
                    showMessage(btnDone, "Task not found")

                }

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun perfomAction(task: Task, actionType: ActionType){
        val taskAction = TaskAction(task, actionType.name)
        viewModel.execute(taskAction)
        finish()
    }

    private fun showMessage(view: View, message: String){
        Snackbar.make(view, message,  Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

}