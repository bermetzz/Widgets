package com.example.widgets

import java.io.Serializable

data class Task(
    var task: String ?= "",
    var description: String ?= "",
    var isChecked: Boolean = false
) : Serializable
