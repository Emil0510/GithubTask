package com.emilabdurahmanli.githubtask.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.emilabdurahmanli.githubtask.network.model.Item
import com.emilabdurahmanli.githubtask.network.model.ItemRoom

@Database(entities = [ItemRoom::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}