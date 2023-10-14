package com.comunidadedevspace.taskbeats.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.REPLACE
import androidx.room.Query
import androidx.room.Update


@Dao
interface TaskDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(task: Task)

    @Query("Select * from task")
    fun getAll(): LiveData<List<Task>>

    @Update(onConflict = REPLACE)
    suspend fun update(task: Task)

    @Query("Delete from task")
    suspend fun deleteAll()

    @Query("Delete from task WHERE id=:id")
    suspend fun deleteById(id: Int)





}