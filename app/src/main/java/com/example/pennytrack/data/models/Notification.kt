package com.example.pennytrack.data.models

data class Notification(
    val id: String = "",
    val message: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false
)