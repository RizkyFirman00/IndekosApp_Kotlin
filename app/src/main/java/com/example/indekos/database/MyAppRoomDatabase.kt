package com.example.indekos.database

import android.content.Context
import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.indekos.model.Indekos
import com.example.indekos.model.StringListConverter
import com.example.indekos.model.Users

@Database(entities = [Users::class, Indekos::class], version = 2)
@TypeConverters(StringListConverter::class)
abstract class MyAppRoomDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun indekosDao(): IndekosDao

    companion object {
        @Volatile
        private var INSTANCE: MyAppRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): MyAppRoomDatabase {
            if (INSTANCE == null) {
                synchronized(MyAppRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MyAppRoomDatabase::class.java, "myapp_database"
                    )
                        .build()
                }
            }
            return INSTANCE as MyAppRoomDatabase
        }
    }
}