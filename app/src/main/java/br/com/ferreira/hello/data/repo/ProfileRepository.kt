package br.com.ferreira.hello.data.repo

import br.com.ferreira.hello.data.client.ProfileApiService
import br.com.ferreira.hello.data.model.ApiCallback
import br.com.ferreira.hello.data.model.Todo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class ProfileRepository @Inject constructor(
    private val apiService: ProfileApiService
) {
    fun getMessage(id: Long, callback: ApiCallback) =
        apiService.getTodoById(id)?.enqueue(object : Callback<Todo?> {
            override fun onResponse(call: Call<Todo?>, response: Response<Todo?>) {
                if (response.isSuccessful) {
                    response.body()?.let { todo ->
                        callback.onSuccess(todo.title)
                    }
                } else {
                    if (response.code() == 404) {
                        callback.onError("Not found")
                    } else {
                        callback.onError("Unknown error ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<Todo?>, t: Throwable) {
                t.message?.let {
                    callback.onError(it)
                }
            }
        })
}