package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {
    private var downloadID: Long = 0
    private var downloadName = ""
    private var downloadStatus = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_title)
        )

        download_button.setOnClickListener {
            when {
                glide_radio_button.isChecked -> {
                    URL = GLIDE_URL
                    downloadName = getString(R.string.glide_radio_btn_label)
                }
                udacity_radio_button.isChecked -> {
                    URL = GITHUB_URL
                    downloadName = getString(R.string.udacity_radio_btn_label)

                }
                retrofit_radio_button.isChecked -> {
                    URL = RETROFIT_URL
                    downloadName = getString(R.string.retrofit_radio_btn_label)

                }
                else -> {
                    URL = ""
                    Toast.makeText(
                        this, resources.getString(R.string.error_message),
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
            if (!URL.isBlank())
                download()
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            downloadStatus = if (id == downloadID) resources.getString(R.string.status_successful)
            else resources.getString(R.string.status_failure)

            download_button.changedButtonState(ButtonState.Completed)
            createNotification(downloadName, downloadStatus, context!!)
        }
    }

    private fun download() {
        download_button.changedButtonState(ButtonState.Clicked)
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)
    }


    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
                .apply {
                    setShowBadge(true)
                }

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)

            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun createNotification(name: String, status: String, context: Context) {
        val notificationManager = ContextCompat.getSystemService(
            this, NotificationManager::class.java
        ) as NotificationManager
        notificationManager.sendNotification(name, status, context)
    }

    companion object {
        private var URL = ""
        private const val GITHUB_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"

        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/v4.11.0.zip"

        private const val RETROFIT_URL =
            "https://search.maven.org/remote_content?g=com.squareup.retrofit2&a=retrofit&v=LATEST"
    }


}

