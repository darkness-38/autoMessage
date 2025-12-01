package com.hamza.automessage

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MessageManager(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auto_message_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveMessage(message: ScheduledMessage) {
        val messages = getMessages().toMutableList()
        messages.add(message)
        saveList(messages)
    }

    fun getMessages(): List<ScheduledMessage> {
        val json = prefs.getString("scheduled_messages", null) ?: return emptyList()
        val type = object : TypeToken<List<ScheduledMessage>>() {}.type
        return gson.fromJson(json, type)
    }

    fun deleteMessage(id: Long) {
        val messages = getMessages().toMutableList()
        val iterator = messages.iterator()
        while (iterator.hasNext()) {
            val message = iterator.next()
            if (message.id == id) {
                cancelAlarm(message.requestCode)
                iterator.remove()
                break
            }
        }
        saveList(messages)
    }

    private fun saveList(messages: List<ScheduledMessage>) {
        val json = gson.toJson(messages)
        prefs.edit().putString("scheduled_messages", json).apply()
    }

    private fun cancelAlarm(requestCode: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}
