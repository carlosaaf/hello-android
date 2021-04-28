package br.com.ferreira.hello.data.model

interface ApiCallback {
    fun onSuccess(text: String)
    fun onError(text: String)
}