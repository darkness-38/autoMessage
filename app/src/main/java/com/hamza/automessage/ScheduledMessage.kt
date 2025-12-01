package com.hamza.automessage

data class ScheduledMessage(
    val id: Long,
    val phoneNumber: String,
    val message: String,
    val timestamp: Long,
    val requestCode: Int
)
