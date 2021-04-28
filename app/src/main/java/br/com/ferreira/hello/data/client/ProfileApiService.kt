package br.com.ferreira.hello.data.client

import br.com.ferreira.hello.data.model.Todo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApiService {
    @GET("todos/{id}")
    fun getTodoById(@Path("id") todoId: Long): Call<Todo?>?
}