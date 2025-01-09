package com.bangnv.pushnotificationv2

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.bangnv.pushnotificationv2.databinding.ActivityMainBinding
import com.bangnv.pushnotificationv2.utils.applyWindowInsets
import com.bangnv.pushnotificationv2.utils.showToastLong
import com.bangnv.pushnotificationv2.utils.showToastShort
import java.util.Date

class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_POST_NOTIFICATION = 1001
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        initializeBinding()
        applyWindowInsets()
        setupClickListener()
    }

    private fun initializeBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun applyWindowInsets() {
        binding.root.applyWindowInsets()
    }

    private fun setupClickListener() {
        binding.btnGoToListProduct.setOnClickListener {
            navigateToListProduct()
        }
        binding.btnSendNotification.setOnClickListener {
            sendNotificationIfPermitted()
        }
    }

    private fun navigateToListProduct() {
        startActivity(Intent(this, ListProductActivity::class.java))
    }

    private fun sendNotificationIfPermitted() {
        if (hasNotificationPermission()) {
            sendNotification()
        } else {
            requestNotificationPermission()
        }
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            // If the version is lower than TIRAMISU, permission is not required
            true
        }
    }

    private fun sendNotification() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val notificationManagerCompat = NotificationManagerCompat.from(this)
            val notification = buildNotification()
            notificationManagerCompat.notify(getNotificationId(), notification)
        }
    }

    private fun buildNotification(): Notification {
        val bitmapIcon = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
        // Default sound
        val defaultSound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val pendingIntentRegularActivity: PendingIntent? = createIntentRegularActivity()
        val pendingIntentSpecialActivity: PendingIntent? = createIntentSpecialActivity()

        // Case of using different intents
//        val myExecuteIntent = pendingIntentSpecialActivity
        val myExecuteIntent = pendingIntentRegularActivity

        return NotificationCompat.Builder(this, MyApplication.CHANNEL_ID_1)
            .setContentTitle("Title push notification")
            .setContentText("Message push notification")
            .setSmallIcon(R.drawable.ic_notification)
            .setLargeIcon(bitmapIcon)
            .setSound(defaultSound)
            .setAutoCancel(true)
            .setContentIntent(myExecuteIntent)
            .setColor(resources.getColor(R.color.colorAccent, theme))
            .build()
    }

    // Regular activity
    private fun createIntentRegularActivity(): PendingIntent? {
        // Create an Intent for the activity you want to start.
        val resultIntent = Intent(this, DetailActivity::class.java)
        // Create the TaskStackBuilder.
        val resultPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            // Add the intent, which inflates the back stack.
            addNextIntentWithParentStack(resultIntent)
            // Get the PendingIntent containing the entire back stack.
            getPendingIntent(
                getNotificationId(),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        return resultPendingIntent
    }

    // Special activity
    private fun createIntentSpecialActivity(): PendingIntent? {
        val notifyIntent = Intent(this, DetailActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notifyPendingIntent = PendingIntent.getActivity(
            this, getNotificationId(), notifyIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return notifyPendingIntent
    }

    private fun getNotificationId() = Date().time.toInt()

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_POST_NOTIFICATION
            )
        }
    }

    // Handle notification permissions
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_POST_NOTIFICATION) {
            when {
                grantResults.isEmpty() -> {
                    showPermissionExplanationDialog()
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    showToastShort(getString(R.string.str_permission_granted_click_again))
                }
                else -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val shouldShow = shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)
                        if (shouldShow) {
                            showPermissionExplanationDialog()
                        } else {
                            navigateToAppSettings()
                        }
                    }
                }
            }
        }
    }

    private fun showPermissionExplanationDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.str_request_noti_permission_title))
            .setMessage(getString(R.string.str_request_noti_permission_message))
            .setPositiveButton(getString(R.string.str_retry)) { _, _ ->
                requestNotificationPermission()
            }
            .setNegativeButton(getString(R.string.str_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun navigateToAppSettings() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.str_noti_permission_denied_title))
            .setMessage(getString(R.string.str_noti_permission_denied_message))
            .setPositiveButton(getString(R.string.str_go_to_setting)) { _, _ ->
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts(getString(R.string.str_schema_package), packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton(getString(R.string.str_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}