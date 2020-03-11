package br.com.angelorobson.www.workkerrequestsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    companion object {
        const val ID = "id"
    }

    var time = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start_worker.setOnClickListener {
            executeWorker(time++)
        }
    }

    private fun executeWorker(time: Int) {
        val workManager = WorkManager.getInstance(this)
        // workDataOf (part of KTX) converts a list of pairs to a [Data] object.
        val imageData = workDataOf(ID to time)

        val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(imageData)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .build()

        workManager.enqueue(uploadWorkRequest)
        val status = workManager.getWorkInfoByIdLiveData(uploadWorkRequest.id)
        status.observe(this, Observer {
            if (it.state == WorkInfo.State.SUCCEEDED) {
                Log.d("Times", "time= ${it.outputData.getInt(ID, 0)}")
                Toast.makeText(
                    this@MainActivity,
                    "time= ${it.outputData.getInt(ID, 0)}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
