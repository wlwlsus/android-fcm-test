package com.dontsc.fcm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.app.DownloadManager

import android.content.IntentFilter

import android.content.Intent

import android.content.BroadcastReceiver


import android.net.Uri

import java.io.File

import android.content.Context
import android.util.Log
import com.dontsc.fcm.R
import java.io.IOException

import android.content.IntentSender

import android.app.PendingIntent

import java.io.FileInputStream

import java.io.OutputStream

import android.content.pm.PackageInstaller
import android.content.pm.PackageInstaller.SessionParams

import androidx.annotation.NonNull


class InstallActivity : AppCompatActivity() {

    private var mDownloadReference: Long = 0
    lateinit var mDownloadManager: DownloadManager


    private val TAG = "Install"

    private lateinit var startBtn: Button
    private lateinit var homeBtm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_install)

        startBtn = findViewById(R.id.start_install)
        homeBtm = findViewById(R.id.homeBtn)


//        autoInstall(
//        downloadFiles(
//            context = this,
//            url = "https://kiosk-apk.s3.ap-northeast-2.amazonaws.com/auto_install.apk",
//            filename = "auto.apk"
//        )


        startBtn.setOnClickListener {
            val localApk = File("/storage/emulated/0/Download/auto.apk")
            val commands = arrayOf("pm", "install", "-r", localApk.absolutePath)
            Runtime.getRuntime().exec(commands)
        }
    }

    @Throws(Exception::class)
    fun install(context: Context, path: String) {
        val file = File(path)
        val apkName = path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf(".apk"))
        //Get PackageInstaller
        val packageInstaller = context.packageManager
            .packageInstaller
        val params = SessionParams(SessionParams.MODE_FULL_INSTALL)
        var session: PackageInstaller.Session? = null
        var outputStream: OutputStream? = null
        var inputStream: FileInputStream? = null
        try {
            //Create Session
            val sessionId = packageInstaller.createSession(params)
            //Open Session
            session = packageInstaller.openSession(sessionId)
            //Get the output stream to write apk to session
            outputStream = session.openWrite(apkName, 0, -1)
            inputStream = FileInputStream(file)
            val buffer = ByteArray(4096)
            var n: Int
            //Read apk file and write session
            while (inputStream.read(buffer).also { n = it } > 0) {
                outputStream.write(buffer, 0, n)
            }
            //You need to close the flow after writing, otherwise the exception "files still open" will be thrown
            inputStream.close()
            inputStream = null
            outputStream.flush()
            outputStream.close()
            outputStream = null
            //Configure the intent initiated after the installation is completed, usually to open the activity
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val intentSender = pendingIntent.intentSender
            //Submit to start installation
            session.commit(intentSender)
        } catch (e: IOException) {
            throw RuntimeException("Couldn't install package", e)
        } catch (e: RuntimeException) {
            session?.abandon()
            throw e
        } finally {
//            closeStream(inputStream)
//            closeStream(outputStream)
        }
    }

    private fun autoInstall(
        url: String,
        filename: String
    ) {
        val uri = Uri.parse(url)

        //Delete update file if exists
        val file = File("/storage/emulated/0/$filename")

        if (file.exists()) //file.delete() - test this, I think sometimes it doesn't work
            file.delete()

        //set download manager
        val request = DownloadManager.Request(uri)
            .setDescription("setDescription")
            .setTitle("setTitle")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setDestinationInExternalPublicDir("/Download", filename)

        //set destination
//        request.setDestinationUri(uri)

        // get download service and enqueue file
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadId = manager.enqueue(request)


        //set BroadcastReceiver to install app when .apk is downloaded
        val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(ctxt: Context?, intent: Intent?) {
                Log.e(TAG, "onReceive: 다운 완료!")
                val install = Intent(Intent.ACTION_VIEW)
                install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                install.setDataAndType(
                    uri,
                    manager.getMimeTypeForDownloadedFile(downloadId)
                )
                startActivity(install)
                unregisterReceiver(this)
                finish()
            }
        }

        //register receiver for when .apk download is compete
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }


    private fun downloadFiles(context: Context, url: String, filename: String) {
        try {
            val file = File("/storage/emulated/0/$filename")

            if (file.exists()) {
                return
            }
            mDownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri)
                .setTitle(filename)
                .setDescription("Downloading")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationInExternalPublicDir("/Download", filename)
            mDownloadReference = mDownloadManager.enqueue(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        downloadState(context)
    }

    private fun downloadState(context: Context) {
        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        val receiverDownloadComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val reference = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);

                if (mDownloadReference == reference) {
                    val query = DownloadManager.Query();
                    query.setFilterById(reference);
                    val cursor = mDownloadManager.query(query);

                    cursor.moveToFirst();

                    val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
                    val status = cursor.getInt(columnIndex);

                    cursor.close()
                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            Log.e(TAG, "onReceive: 성공")
                        }

                        DownloadManager.STATUS_FAILED -> {
                            Log.e(TAG, "onReceive: 실패")
                        }

                        DownloadManager.ERROR_FILE_ERROR -> {
                            Log.e(TAG, "onReceive: 실패2")
                        }
                    }
                }
            }
        }
        context.registerReceiver(receiverDownloadComplete, intentFilter)
    }
}