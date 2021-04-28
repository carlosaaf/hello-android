package br.com.ferreira.hello.data.model

data class Todo(
    var id: Long,
    var userId: Long,
    var title: String,
    var completed: Boolean
)