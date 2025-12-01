package com.hamza.automessage

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.widget.Toast

class SmsSentReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                Toast.makeText(context, "SMS Başarıyla Gönderildi", Toast.LENGTH_SHORT).show()
            }
            SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                Toast.makeText(context, "SMS Hatası: Genel Arıza", Toast.LENGTH_LONG).show()
            }
            SmsManager.RESULT_ERROR_NO_SERVICE -> {
                Toast.makeText(context, "SMS Hatası: Servis Yok", Toast.LENGTH_LONG).show()
            }
            SmsManager.RESULT_ERROR_NULL_PDU -> {
                Toast.makeText(context, "SMS Hatası: PDU Boş", Toast.LENGTH_LONG).show()
            }
            SmsManager.RESULT_ERROR_RADIO_OFF -> {
                Toast.makeText(context, "SMS Hatası: Radyo Kapalı (Uçak Modu?)", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(context, "SMS Hatası: Bilinmeyen Hata ($resultCode)", Toast.LENGTH_LONG).show()
            }
        }
    }
}
