package com.emilabdurahmanli.githubtask.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.emilabdurahmanli.githubtask.network.model.Item
import com.emilabdurahmanli.githubtask.network.model.ItemRoom

@Dao
interface ItemDao {

    @Query("SELECT * FROM Item_Favorites")
    fun getAll(): List<ItemRoom>

    @Insert
    fun insert(vararg item: ItemRoom)

    @Delete
    fun delete(item: ItemRoom)
}