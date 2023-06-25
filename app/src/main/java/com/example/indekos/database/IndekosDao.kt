package com.example.indekos.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.indekos.model.Indekos

@Dao
interface IndekosDao {
//    @Query("SELECT * FROM indekos")
//    fun getAllIndekos(): LiveData<List<Indekos>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIndekos(indekos: Indekos)

//    @Update
//    suspend fun updateIndekos(indekos: Indekos)
//
//    @Delete
//    suspend fun deleteIndekos(indekos: Indekos)

}