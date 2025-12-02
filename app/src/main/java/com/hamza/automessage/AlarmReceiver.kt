package com.hamza.automessage

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.widget.Toast
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val phoneNumber = intent.getStringExtra("PHONE_NUMBER")
        val message = intent.getStringExtra("MESSAGE")
        val messageId = intent.getLongExtra("MESSAGE_ID", -1)

        if (!phoneNumber.isNullOrEmpty() && !message.isNullOrEmpty()) {
            try {
                val smsManager = context.getSystemService(SmsManager::class.java)
                
                val sentIntent = PendingIntent.getBroadcast(
                    context, 
                    0, 
                    Intent(context, SmsSentReceiver::class.java).apply { action = "SMS_SENT" }, 
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                smsManager.sendTextMessage(phoneNumber, null, message, sentIntent, null)
                Log.d("AlarmReceiver", "SMS sending triggered to $phoneNumber")

                // Mesaj gönderildikten sonra listeden sil
                if (messageId != -1L) {
                    MessageManager(context).deleteMessage(messageId)
                    Log.d("AlarmReceiver", "Message $messageId deleted from list")
                }

            } catch (e: Exception) {
                Log.e("AlarmReceiver", "Error sending SMS", e)
                Toast.makeText(context, "SMS Gönderme Hatası: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
