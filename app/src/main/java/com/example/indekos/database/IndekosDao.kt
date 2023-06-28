package com.example.indekos.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.indekos.model.Indekos
import kotlinx.coroutines.flow.Flow

@Dao
interface IndekosDao {

    // Fungsi RV di Home Activity
    @Query("SELECT * FROM indekos")
    fun getAllIndekos(): Flow<List<Indekos>>

    // Fungsi tambah data di AddDataActivity
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertIndekos(indekos: Indekos)

    // Fungsi ambil data indekos dari homePage, buat nampilin data di detail
    @Query("SELECT * FROM indekos WHERE indekosId = :indekosId")
    fun getIndekosById(indekosId: Int): LiveData<Indekos>

    // Fungsi RV di History Activity
    @Query("SELECT * FROM indekos WHERE userId = :userId")
    fun getIndekosByUserId(userId: Int): Flow<List<Indekos>>

    // Update data indekos di DetailHistoryActivity
    @Update
    fun updateIndekos(indekos: Indekos)

    // Delete data indekos di DetailHistoryActivity
    @Query("DELETE FROM indekos WHERE indekosId = :indekosId")
    fun deleteIndekos(indekosId: Int)

}