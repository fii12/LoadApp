package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {
    private var downloadName = ""
    private var downloadStatus = ""


    companion object {
        private const val DOWNLOAD_FILE_NAME = "download_name"
        private const val DOWNLOAD_STATUS_NAME = "download_status"
        fun getDetailIntent(
            context: Context,
            fileDownloaded: String,
            downloadStatus: String
        ): Intent {
            return Intent(context, DetailActivity::class.java).apply {
                putExtra(DOWNLOAD_FILE_NAME, fileDownloaded)
                putExtra(DOWNLOAD_STATUS_NAME, downloadStatus)
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val notificationManager = ContextCompat.getSystemService(
            this, NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelNotifications()

        downloadName = intent.getStringExtra(DOWNLOAD_FILE_NAME).toString()
        file_name_text.text = downloadName

        downloadStatus = intent.getStringExtra(DOWNLOAD_STATUS_NAME).toString()
        status_text.text = downloadStatus

        if (!downloadStatus.isBlank()) {
            status_text.setTextColor(getColor(R.color.colorPrimary))
        } else {
            status_text.setTextColor(Color.RED)
        }

        ok_button.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

}
