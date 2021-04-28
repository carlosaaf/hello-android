package br.com.ferreira.hello.data.client

import br.com.ferreira.hello.data.model.Profile
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApiService {
    @GET("profile/{id}")
    fun getTodoById(@Path("id") todoId: Long): Call<Profile?>?
}