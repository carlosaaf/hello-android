package br.com.ferreira.hello.data.model

interface ApiCallback<T> {
    fun onSuccess(value: T)
    fun onError(text: String)
}