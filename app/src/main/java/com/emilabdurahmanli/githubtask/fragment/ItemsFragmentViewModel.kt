package com.emilabdurahmanli.githubtask.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.emilabdurahmanli.githubtask.network.Response
import com.emilabdurahmanli.githubtask.network.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback

class ItemsFragmentViewModel : ViewModel() {
    private var responseModel = MutableLiveData<Response>()
    fun getResponse(data : String){
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.api().getRepositories("created:${data}", "stars", "desc").enqueue(object : Callback<Response>{
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    responseModel.postValue(response.body())
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {

                }

            })
        }
    }

    fun observeResponse():LiveData<Response>{
        return responseModel
    }


}