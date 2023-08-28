package com.comunidadedevspace.taskbeats

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toolbar
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    /*private var taskList = arrayListOf(
        Task(0, "Title0", "Desc0"),
        Task(1, "Title1", "Desc1"),
        Task(2, "Title2", "Desc2"),
        Task(3, "Title3", "Desc3"),
        Task(4, "Title4", "Desc4"),
    )*/
    private lateinit var ctnContent: LinearLayout

    private val adapter: TaskListAdapter = TaskListAdapter(::onListItemClicked)

    private val dataBase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "taskbeats-database"
        ).build()

    }
    private val dao by lazy {
        dataBase.taskDao()
    }

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val taskAction = data?.getSerializableExtra(TASK_ACTION_RESULT) as TaskAction
            val task: Task = taskAction.task

            if (taskAction.actionType == ActionType.DELETE.name) {
                /*val newList = arrayListOf<Task>()
                    .apply {
                        addAll(taskList)
                    }
                newList.remove(task)
                showMessage(ctnContent, "Task deleted")

                if (newList.size == 0) {
                    ctnContent.visibility = View.VISIBLE
                }

                adapter.submitList(newList)
                taskList = newList */

            } else if (taskAction.actionType == ActionType.CREATE.name) {
               insertIntoDataBase(task)
            }else if (taskAction.actionType == ActionType.UPDATE.name) {

                /*val tempEmptyList = arrayListOf<Task>()
                taskList.forEach{
                    if(it.id == task.id){
                        val newItem = Task(it.id, task.title, task.description)
                        tempEmptyList.add(newItem)
                    }else{
                        tempEmptyList.add(it)
                    }
                }
                showMessage(ctnContent, "Task updated")
                adapter.submitList(tempEmptyList)
                taskList = tempEmptyList    */
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)


        listFromDataBase()

        ctnContent = findViewById(R.id.ctn_content)


        val rvTasks: RecyclerView = findViewById(R.id.rv_task_list)
        rvTasks.adapter = adapter

        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            openTaskListDetail(null)
        }

    }
    private fun insertIntoDataBase(task:Task) {
        CoroutineScope(IO).launch {
            dao.insert(task)
            listFromDataBase()
        }
    }
    private fun listFromDataBase(){
        CoroutineScope(IO).launch {
            val myDataBaseList: List<Task> = dao.getAll()
            adapter.submitList(myDataBaseList)
        }
    }

    private fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
            .show()
    }

    private fun onListItemClicked(task: Task) {
        openTaskListDetail(task)
    }

    private fun openTaskListDetail(task: Task?) {
        val intent = TaskDetailActivity.start(this, task)
        startForResult.launch(intent)
    }
}

enum class ActionType {
    DELETE,
    UPDATE,
    CREATE

}

data class TaskAction(
    val task: Task,
    var actionType: String
) : Serializable

const val TASK_ACTION_RESULT = "TASK_ACTION_RESULT"