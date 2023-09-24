package com.emilabdurahmanli.githubtask.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emilabdurahmanli.githubtask.network.ResponseResult
import com.emilabdurahmanli.githubtask.network.model.Response
import com.emilabdurahmanli.githubtask.network.RetrofitClient
import com.emilabdurahmanli.githubtask.network.model.ItemRoom
import com.emilabdurahmanli.githubtask.room.ItemDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback



class ItemsFragmentViewModel() : ViewModel() {
    private var responseModel = MutableLiveData<Response>()
    private var favoriteList = MutableLiveData<List<ItemRoom>>()
    private var responseResult = MutableLiveData<ResponseResult<Any>>()

    fun getFavoriteList( itemDao: ItemDao){
        CoroutineScope(Dispatchers.IO).launch {
            favoriteList.postValue(itemDao.getAll())
        }
    }
     fun getResponse(data : String, page : Int){
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.api().getRepositories("created:${data}", "stars", "desc", page).enqueue(object : Callback<Response>{
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if(response.isSuccessful){
                        responseModel.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                }
            })
        }
    }

    fun addToFavorites(itemDao: ItemDao, itemRoom: ItemRoom){
        CoroutineScope(Dispatchers.IO).launch {
            itemDao.insert(itemRoom)
        }
    }

    fun deleteFromFavorites(itemDao: ItemDao, itemRoom: ItemRoom){
        CoroutineScope(Dispatchers.IO).launch {
            itemDao.delete(itemRoom)
            getFavoriteList(itemDao)
        }
    }

    fun observeResponse():LiveData<Response>{
        return responseModel
    }

    fun observeFavoriteList():LiveData<List<ItemRoom>>{
        return favoriteList
    }

    fun observeResponseResult () :LiveData<ResponseResult<Any>>{
        return responseResult
    }


}