package com.hamza.automessage

import android.Manifest
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var etPhoneNumber: EditText
    private lateinit var etMessage: EditText
    private lateinit var tvSelectedDateTime: TextView
    private lateinit var btnDate: Button
    private lateinit var btnTime: Button
    private lateinit var btnSchedule: Button
    private lateinit var btnPickContact: ImageButton

    private var selectedCalendar: Calendar = Calendar.getInstance()

    companion object {
        private const val PERMISSION_REQUEST_CODE = 101
    }

    private val contactPickerLauncher = registerForActivityResult(androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val contactUri = result.data?.data ?: return@registerForActivityResult
            val cursor = contentResolver.query(contactUri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val idIndex = it.getColumnIndex(android.provider.ContactsContract.Contacts._ID)
                    val hasPhoneNumberIndex = it.getColumnIndex(android.provider.ContactsContract.Contacts.HAS_PHONE_NUMBER)
                    
                    if (idIndex != -1 && hasPhoneNumberIndex != -1) {
                        val id = it.getString(idIndex)
                        val hasPhoneNumber = it.getString(hasPhoneNumberIndex)

                        if (hasPhoneNumber.equals("1", ignoreCase = true)) {
                            val phones = contentResolver.query(
                                android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                "${android.provider.ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                                arrayOf(id),
                                null
                            )
                            phones?.use { phoneCursor ->
                                if (phoneCursor.moveToFirst()) {
                                    val numberIndex = phoneCursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    if (numberIndex != -1) {
                                        var number = phoneCursor.getString(numberIndex)
                                        // Boşlukları ve parantezleri temizle
                                        number = number.replace(" ", "").replace("-", "").replace("(", "").replace(")", "")
                                        etPhoneNumber.setText(number)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private lateinit var btnListMessages: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etMessage = findViewById(R.id.etMessage)
        tvSelectedDateTime = findViewById(R.id.tvSelectedDateTime)
        btnDate = findViewById(R.id.btnDate)
        btnSchedule = findViewById(R.id.btnSchedule)
        btnPickContact = findViewById(R.id.btnPickContact)
        btnListMessages = findViewById(R.id.btnListMessages)

        // Giriş Animasyonları
        val title = findViewById<TextView>(R.id.tvTitle)
        val cardForm = findViewById<androidx.cardview.widget.CardView>(R.id.cardForm)
        val cardDate = findViewById<androidx.cardview.widget.CardView>(R.id.cardDate)

        title.alpha = 0f
        title.translationY = -50f
        title.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(100).start()
        
        btnListMessages.setOnClickListener {
            startActivity(Intent(this, ScheduledMessagesActivity::class.java))
        }

        cardForm.alpha = 0f
        cardForm.translationY = 100f
        cardForm.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(300).start()

        cardDate.alpha = 0f
        cardDate.translationY = 100f
        cardDate.animate().alpha(1f).translationY(0f).setDuration(800).setStartDelay(500).start()

        btnSchedule.alpha = 0f
        btnSchedule.scaleX = 0.5f
        btnSchedule.scaleY = 0.5f
        btnSchedule.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(600).setStartDelay(700).setInterpolator(android.view.animation.OvershootInterpolator()).start()

        updateDateTimeDisplay()

        btnDate.setOnClickListener { 
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction { 
                it.animate().scaleX(1f).scaleY(1f).setDuration(100).start() 
            }.start()
            showDatePicker() 
        }
        btnTime.setOnClickListener { 
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction { 
                it.animate().scaleX(1f).scaleY(1f).setDuration(100).start() 
            }.start()
            showTimePicker() 
        }
        btnSchedule.setOnClickListener { 
            it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction { 
                it.animate().scaleX(1f).scaleY(1f).setDuration(100).start() 
            }.start()
            scheduleMessage() 
        }
        btnPickContact.setOnClickListener { 
            it.animate().rotation(360f).setDuration(500).start()
            pickContact() 
        }

        checkPermissions()
    }

    // ... (pickContact, showDatePicker, showTimePicker, updateDateTimeDisplay, checkPermissions aynı kalacak)

    private fun pickContact() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            checkPermissions()
            return
        }
        val intent = Intent(Intent.ACTION_PICK, android.provider.ContactsContract.Contacts.CONTENT_URI)
        contactPickerLauncher.launch(intent)
    }

    private fun showDatePicker() {
        val year = selectedCalendar.get(Calendar.YEAR)
        val month = selectedCalendar.get(Calendar.MONTH)
        val day = selectedCalendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedCalendar.set(Calendar.YEAR, selectedYear)
            selectedCalendar.set(Calendar.MONTH, selectedMonth)
            selectedCalendar.set(Calendar.DAY_OF_MONTH, selectedDay)
            updateDateTimeDisplay()
        }, year, month, day).show()
    }

    private fun showTimePicker() {
        val hour = selectedCalendar.get(Calendar.HOUR_OF_DAY)
        val minute = selectedCalendar.get(Calendar.MINUTE)

        TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            selectedCalendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            selectedCalendar.set(Calendar.MINUTE, selectedMinute)
            selectedCalendar.set(Calendar.SECOND, 0)
            updateDateTimeDisplay()
        }, hour, minute, true).show()
    }

    private fun updateDateTimeDisplay() {
        val format = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm", java.util.Locale.getDefault())
        tvSelectedDateTime.text = "Seçilen: ${format.format(selectedCalendar.time)}"
    }

    private fun checkPermissions() {
        val permissions = arrayOf(
            Manifest.permission.SEND_SMS, 
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CONTACTS
        )
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE)
        }
    }

    private fun scheduleMessage() {
        var phoneNumber = etPhoneNumber.text.toString().trim()
        val message = etMessage.text.toString()

        // Otomatik numara formatlama (+90 ekleme)
        if (phoneNumber.isNotEmpty() && !phoneNumber.startsWith("+")) {
            if (phoneNumber.startsWith("5")) {
                phoneNumber = "+90$phoneNumber"
            } else if (phoneNumber.startsWith("05")) {
                phoneNumber = "+90${phoneNumber.substring(1)}"
            }
        }

        if (phoneNumber.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show()
            return
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_SHORT).show()
            checkPermissions()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
                Toast.makeText(this, "Lütfen hassas alarm izni verin", Toast.LENGTH_LONG).show()
                return
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                    data = android.net.Uri.parse("package:$packageName")
                }
                startActivity(intent)
                Toast.makeText(this, "Lütfen pil kısıtlamasını kaldırın", Toast.LENGTH_LONG).show()
            }
        }

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        
        // Benzersiz ID oluştur
        val requestCode = System.currentTimeMillis().toInt()
        
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("PHONE_NUMBER", phoneNumber)
            putExtra("MESSAGE", message)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            requestCode, // Benzersiz RequestCode
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, selectedCalendar.timeInMillis, pendingIntent)
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, selectedCalendar.timeInMillis, pendingIntent)
            }
            
            // Mesajı kaydet
            val scheduledMessage = ScheduledMessage(
                id = requestCode.toLong(),
                phoneNumber = phoneNumber,
                message = message,
                timestamp = selectedCalendar.timeInMillis,
                requestCode = requestCode
            )
            MessageManager(this).saveMessage(scheduledMessage)
            
            Toast.makeText(this, getString(R.string.scheduled_success), Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            Toast.makeText(this, "Alarm izni hatası: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
