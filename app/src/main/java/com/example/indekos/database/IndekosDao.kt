package com.example.indekos.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.indekos.model.Indekos
import kotlinx.coroutines.flow.Flow

@Dao
interface IndekosDao {

    @Query("SELECT * FROM indekos")
    fun getAllIndekos(): Flow<List<Indekos>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIndekos(indekos: Indekos)

    @Query("SELECT * FROM indekos WHERE indekosId = :indekosId")
    fun getIndekosById(indekosId: Int): LiveData<Indekos>

}