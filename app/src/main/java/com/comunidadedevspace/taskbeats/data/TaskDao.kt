package com.comunidadedevspace.taskbeats.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update


@Dao
interface TaskDao {

    @Insert(onConflict = REPLACE)
    fun insert(task: Task)

    @Query("Select * from task")
    fun getAll(): List<Task>

    @Update(onConflict = REPLACE)
    fun update(task: Task)

    @Query("Delete from task")
    fun deleteAll()

    @Query("Delete from task WHERE id=:id")
    fun deleteById(id: Int)





}