package com.hamza.automessage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScheduledMessagesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MessagesAdapter
    private lateinit var messageManager: MessageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scheduled_messages)

        messageManager = MessageManager(this)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }

        findViewById<ImageButton>(R.id.btnAdd).setOnClickListener {
            finish() // Return to main activity to add new
        }
        
        loadMessages()
    }

    private fun loadMessages() {
        val messages = messageManager.getMessages()
        adapter = MessagesAdapter(messages) { message ->
            messageManager.deleteMessage(message.id)
            loadMessages() // Refresh list
        }
        recyclerView.adapter = adapter
    }

    class MessagesAdapter(
        private val messages: List<ScheduledMessage>,
        private val onDeleteClick: (ScheduledMessage) -> Unit
    ) : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {

        class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvPhoneNumber: TextView = view.findViewById(R.id.tvPhoneNumber)
            val tvMessage: TextView = view.findViewById(R.id.tvMessage)
            val tvDate: TextView = view.findViewById(R.id.tvDate)
            val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_scheduled_message, parent, false)
            return MessageViewHolder(view)
        }

        override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
            val message = messages[position]
            holder.tvPhoneNumber.text = message.phoneNumber
            holder.tvMessage.text = message.message
            
            val timeFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
            holder.tvDate.text = timeFormat.format(java.util.Date(message.timestamp))

            holder.btnDelete.setOnClickListener {
                onDeleteClick(message)
            }
        }

        override fun getItemCount() = messages.size
    }
}
