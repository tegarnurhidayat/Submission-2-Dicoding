package com.datte.githubprofile.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.datte.githubprofile.User

@Database(entities = [User::class], version = 1)
abstract class FavRoomDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object {
        @Volatile
        private var INSTANCE: FavRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FavRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                    FavRoomDatabase::class.java, "fav_database").fallbackToDestructiveMigration()
                    .build()
                }
            }
            return INSTANCE as FavRoomDatabase
        }
    }
}