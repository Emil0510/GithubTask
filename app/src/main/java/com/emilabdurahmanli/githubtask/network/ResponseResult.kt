package com.emilabdurahmanli.githubtask.network

import com.emilabdurahmanli.githubtask.network.model.Message

sealed interface ResponseResult<T : Any>{
    class OnSuccess<T : Any>(val data : T) : ResponseResult<T>
    class OnError<T : Any>(var message : Message) : ResponseResult<T>
}