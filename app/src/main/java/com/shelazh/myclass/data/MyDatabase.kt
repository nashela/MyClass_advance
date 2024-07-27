package com.shelazh.myclass.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shelazh.myclass.data.local.User
import com.shelazh.myclass.data.local.UserDao

@Database(
    entities = [User::class],
    version = 2,
    exportSchema = true
)
abstract class MyDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    companion object {

        @Volatile
        private var INSTANCE: MyDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): MyDatabase {
            val tmpInstance = INSTANCE
            if(tmpInstance == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, MyDatabase::class.java, "my_database")
                    .fallbackToDestructiveMigration()
                    .build()
            }

            return INSTANCE!!
        }
    }
}