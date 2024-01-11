package com.enesas.coroutines

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface CommentsAPI {

    @GET("/comments")
    fun getComments() : Call<List<Comments>>

    @GET("/comments")
    suspend fun getSuspendComments() : Response<List<Comments>>
}