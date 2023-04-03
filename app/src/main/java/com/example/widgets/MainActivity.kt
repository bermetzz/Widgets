package com.example.widgets

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.widgets.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.time,
            android.R.layout.simple_spinner_dropdown_item
        )
        binding.newDesc.adapter = adapter
        initClickers()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun initClickers() {
        binding.createBtn.setOnClickListener {
            val notification = binding.newTaskEt.text.toString()
            val spinner = binding.newDesc.selectedItem.toString()
            if (notification.isEmpty() && spinner.isEmpty()) {
                Toast.makeText(
                    applicationContext,
                    "please fill all of the above fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
            // PendingIntent to send to TaskAppWidget
            val intent = Intent(this, TaskAppWidget::class.java)
            intent.action = "ADD_TASK"
            intent.putExtra("task", notification)
            intent.putExtra("description", spinner)
            val pendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            // Send the PendingIntent to TaskAppWidget
            try {
                pendingIntent.send()
                finish()
            } catch (e: PendingIntent.CanceledException) {
                Log.e("bzz", "Failed to send PendingIntent", e.cause)
            }
        }
    }
}