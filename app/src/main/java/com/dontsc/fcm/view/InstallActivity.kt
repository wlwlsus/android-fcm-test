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

import android.app.PendingIntent
import android.content.pm.PackageInstaller

import android.content.pm.PackageInstaller.SessionParams
import android.os.Environment
import android.widget.EditText
import android.widget.Toast


class InstallActivity : AppCompatActivity() {

//    private val ACTION_INSTALL_COMPLETE = "com.dontsc.fcm.INSTALL_COMPLETE"
//    private val ACTION_UNINSTALL_COMPLETE = "com.afwsamples.testdpc.UNINSTALL_COMPLETE"

//    private var mDownloadReference: Long = 0
//    lateinit var mDownloadManager: DownloadManager

    companion object {
        private const val FILE_BASE_PATH = "file://"
        private const val MIME_TYPE = "application/vnd.android.package-archive"
    }

    private val TAG = "Install"

    private lateinit var startBtn: Button
    private lateinit var homeBtm: Button
    private lateinit var apkUrl: EditText
    private var latestFileName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_install)

        startBtn = findViewById(R.id.start_install)
        homeBtm = findViewById(R.id.homeBtn)
        apkUrl = findViewById(R.id.apk_url)

        startBtn.setOnClickListener {
            val url = apkUrl.text.toString()
            val point1 = url.lastIndexOf("/") + 1
            val point2 = url.lastIndex
            latestFileName = url.slice(IntRange(point1, point2))
//            downloadFiles(
//                context = this,
//                url = apkUrl.text.toString(),
//                filename = urlToFileName()
//            )

            enqueueDownload(
                context = applicationContext,
                url = apkUrl.text.toString()
            )
        }
    }

    private fun enqueueDownload(context: Context, url: String) {
        try {
            var destination =
                context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"

            destination += latestFileName

            Log.e(TAG, "enqueueDownload: 파일 경로 : $destination")

            val uri = Uri.parse("$FILE_BASE_PATH$destination")

            val file = File(destination)
            if (file.exists()) file.delete()

            val downloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val downloadUri = Uri.parse(url)
            val request = DownloadManager.Request(downloadUri)
            request.setMimeType(MIME_TYPE)
            request.setTitle("title")
            request.setDescription("des")

            // set destination
            request.setDestinationUri(uri)

            showInstallOption(context, destination)
            // Enqueue a new download and same the referenceId
            downloadManager.enqueue(request)
            Toast.makeText(context, "downloading", Toast.LENGTH_LONG)
                .show()
        } catch (e: Exception) {
            Toast.makeText(context, "올바은 URL이 아니거나 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showInstallOption(context: Context, destination: String) {
        // set BroadcastReceiver to install app when .apk is downloaded
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(
                context: Context,
                intent: Intent
            ) {
                install1(context, "com.dontsc.fcm", destination)
            }
        }
        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    fun install1(context: Context, packageName: String, apkPath: String) {

        // PackageManager provides an instance of PackageInstaller
        val packageInstaller = context.packageManager.packageInstaller

        // Prepare params for installing one APK file with MODE_FULL_INSTALL
        // We could use MODE_INHERIT_EXISTING to install multiple split APKs
        val params =
            PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
        params.setAppPackageName(packageName)

        // Get a PackageInstaller.Session for performing the actual update
        val sessionId = packageInstaller.createSession(params)
        val session = packageInstaller.openSession(sessionId)

        // Copy APK file bytes into OutputStream provided by install Session
        val out = session.openWrite(packageName, 0, -1)
        val fis = File(apkPath).inputStream()
        fis.copyTo(out)
        session.fsync(out)
        out.close()

        // The app gets killed after installation session commit
        session.commit(
            PendingIntent.getBroadcast(
                context, sessionId,
                Intent("android.intent.action.MAIN"), 0
            ).intentSender
        )
    }


    private fun urlToFileName(): String {
        val url = apkUrl.text.toString()
        val point1 = url.lastIndexOf("/") + 1
        val point2 = url.lastIndex
        return url.slice(IntRange(point1, point2))
    }

//    private fun downloadFiles(context: Context, url: String, filename: String) {
//        try {
//            val file = File("/storage/emulated/0/Download/$filename")
//
//            if (file.exists()) {
//                Log.e(TAG, "downloadFiles: 파일 있음")
//                install1(context, "com.dontsc.fcm", "/storage/emulated/0/Download/$filename")
//                return
//            }
//            mDownloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//            val uri = Uri.parse(url)
//            val request = DownloadManager.Request(uri)
//                .setTitle(filename)
//                .setDescription("Downloading")
//                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//                .setDestinationInExternalPublicDir("/Download", filename)
//
//            Log.e(TAG, "화긴~_~: $request")
//            mDownloadReference = mDownloadManager.enqueue(request)
//
//            showInstallOption(context, "/storage/emulated/0/Download/$filename")
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//
//        downloadState(context)
//    }

//    private fun downloadState(context: Context) {
//        val intentFilter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
//        val receiverDownloadComplete = object : BroadcastReceiver() {
//            override fun onReceive(context: Context?, intent: Intent?) {
//                val reference = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0L);
//
//                if (mDownloadReference == reference) {
//                    val query = DownloadManager.Query();
//                    query.setFilterById(reference);
//                    val cursor = mDownloadManager.query(query);
//
//                    cursor.moveToFirst();
//
//                    val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
//                    val status = cursor.getInt(columnIndex);
//
//                    cursor.close()
//                    when (status) {
//                        DownloadManager.STATUS_SUCCESSFUL -> {
//                            Log.e(TAG, "onReceive: 성공")
//                        }
//
//                        DownloadManager.STATUS_FAILED -> {
//                            Log.e(TAG, "onReceive: 실패")
//                        }
//
//                        DownloadManager.ERROR_FILE_ERROR -> {
//                            Log.e(TAG, "onReceive: 실패2")
//                        }
//                    }
//                }
//            }
//        }
//        context.registerReceiver(receiverDownloadComplete, intentFilter)
//    }
//
//
//    private fun showInstallOption(context: Context, destination: String) {
//        val onComplete = object : BroadcastReceiver() {
//            override fun onReceive(
//                context: Context,
//                intent: Intent
//            ) {
//                Log.e(TAG, "onReceive: 다운 끝 !")
////                install1(context, "com.dontsc.fcm", destination)
//            }
//        }
//        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
//    }
//
//    fun install1(context: Context, packageName: String, apkPath: String) {
//
//        val packageInstaller = context.packageManager.packageInstaller
//        val params =
//            SessionParams(SessionParams.MODE_FULL_INSTALL)
//        params.setAppPackageName(packageName)
//
//        val sessionId = packageInstaller.createSession(params)
//        val session = packageInstaller.openSession(sessionId)
//
//        val out = session.openWrite(packageName, 0, -1)
//        val fis = File(apkPath).inputStream()
//        fis.copyTo(out)
//        session.fsync(out)
//        out.close()
//
//        session.commit(
//            PendingIntent.getBroadcast(
//                context, sessionId,
//                Intent("android.intent.action.MAIN"), 0
//            ).intentSender
//        )
//    }


}