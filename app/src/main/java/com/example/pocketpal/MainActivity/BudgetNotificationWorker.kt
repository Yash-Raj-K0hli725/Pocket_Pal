package com.example.pocketpal.MainActivity

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.pocketpal.R

class BudgetNotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val expense = inputData.getLong("expense", 0)
        val budget = inputData.getInt("budget", 0)
        val threshold = inputData.getInt("threshold", 0)

        createNotificationChannel()
        Log.d("Debugging", "Expense: $expense, Budget: $budget, Threshold: $threshold")


        if (expense >= budget) {
            val notification = createNotification2()
            with(NotificationManagerCompat.from(applicationContext)) {
                if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(1, notification)
                }
            }
        } else if (expense >= threshold) {
            val notification = createNotification1()
            with(NotificationManagerCompat.from(applicationContext)) {
                if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    notify(1, notification)
                }
            }
        }
        return Result.success()
    }

    private fun createNotification1(): Notification {
        return NotificationCompat.Builder(applicationContext, "notification_channel")
            .setContentTitle("Pocket Pal")
            .setContentText("Your Budget is about to exceed!")
            .setSmallIcon(R.drawable.analysis)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createNotification2(): Notification {
        return NotificationCompat.Builder(applicationContext, "notification_channel")
            .setContentTitle("Pocket Pal")
            .setContentText("Your Budget is exceeded!")
            .setSmallIcon(R.drawable.analysis)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "notification_channel",
            "Budget Notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        val manager = applicationContext.getSystemService(NotificationManager::class.java)
        manager?.createNotificationChannel(channel)
    }
}