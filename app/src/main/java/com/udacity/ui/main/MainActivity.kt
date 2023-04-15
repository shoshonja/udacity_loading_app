package com.udacity.ui.main

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.R
import com.udacity.ui.details.DetailActivity
import com.udacity.utils.createChannel
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


const val INTENT_CONTENT = "intent-content"
const val INTENT_STATUS = "intent-status"

class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private var notificationManager: NotificationManager? = null
    private var downloadManager: DownloadManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        setupNotifications()

        custom_button.setOnClickListener {
            actOnSelectedRadioOption()
        }
    }

    private fun setupNotifications() {
        getNotificationManager().createChannel(
            getString(R.string.channel_id),
            getString(R.string.channel_name)
        )
    }

    private fun getNotificationManager(): NotificationManager {
        return if (notificationManager == null) {
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        } else {
            notificationManager!!
        }
    }

    private fun getDownloadManager(): DownloadManager {
        return if (downloadManager == null) {
            getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        } else {
            downloadManager!!
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id == downloadID) {
                getNotificationManager().sendNotification(
                    this@MainActivity,
                    prepareIntentForDetails(id),
                    "We downloaded!"
                )
            }
        }
    }

    fun prepareIntentForDetails(downloadId: Long): Intent {
        val contentIntent = Intent(applicationContext, DetailActivity::class.java)

        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = getDownloadManager().query(query)
        if (cursor.moveToFirst()) {
            val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            val success = (status == DownloadManager.STATUS_SUCCESSFUL)
            val uri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_URI))

            contentIntent.putExtra(INTENT_STATUS, success)
            contentIntent.putExtra(INTENT_CONTENT, uri)
        }
        cursor.close()
        return contentIntent
    }


private fun download(radioOption: RadioOption) {
    val url = when (radioOption) {
        RadioOption.GLIDE -> resources.getString(R.string.glide_url)
        RadioOption.RETROFIT -> resources.getString(R.string.retrofit_url)
        RadioOption.UDACITY -> resources.getString(R.string.udacity_url)
    }

    val request =
        DownloadManager.Request(Uri.parse(url))
            .setTitle(getString(R.string.app_name))
            .setDescription(getString(R.string.app_description))
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(true)

    downloadID = getDownloadManager().enqueue(request)
    Toast.makeText(applicationContext, "Download started", Toast.LENGTH_LONG).show()

}

private fun actOnSelectedRadioOption() {
    when (main_radio_group.checkedRadioButtonId) {
        R.id.main_radio_group_glide -> download(RadioOption.GLIDE)
        R.id.main_radio_group_retrofit -> download(RadioOption.RETROFIT)
        R.id.main_radio_group_udacity -> download(RadioOption.UDACITY)
        else -> Toast.makeText(this, "Please select radio option", Toast.LENGTH_SHORT).show()
    }
}

private enum class RadioOption {
    GLIDE,
    RETROFIT,
    UDACITY
}

}
