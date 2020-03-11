package br.com.angelorobson.www.workkerrequestsample

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class UploadWorker(private val appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {

        // Get the input
        val idInput = inputData.getInt(MainActivity.ID, 0)

        // Create the output of the work
        val outputData = workDataOf(MainActivity.ID to idInput)

        val notificationManager =
            appContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification =
            getNotification(
                idInput.toString(),
                appContext,
                notificationManager,
                id.toString()
            )

        notificationManager.notify(idInput, notification)

        // Return the output
        return Result.success(outputData)

    }

    private fun getNotification(
        content: String,
        context: Context?,
        manager: NotificationManager,
        id: String
    ): Notification {
        val builder = NotificationCompat.Builder(context?.applicationContext)
            .setContentText(content)
            .setTicker("Alerta")
            .setAutoCancel(false)
            .setSmallIcon(R.mipmap.ic_launcher)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chanel =
                NotificationChannel(id, id, NotificationManager.IMPORTANCE_DEFAULT)
            manager.createNotificationChannel(chanel)
            builder.setChannelId(id)
        }

        return builder.build()
    }
}