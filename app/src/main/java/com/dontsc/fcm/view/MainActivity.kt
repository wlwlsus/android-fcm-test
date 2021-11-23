package com.dontsc.fcm.view

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.dontsc.fcm.R
import com.dontsc.fcm.update.AdminReceiver
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    companion object {
        var liveTextView: MutableLiveData<String> = MutableLiveData("Default Value")
    }

    private val TAG = "Main"

    private var count = 0
    private lateinit var textView: TextView
    private lateinit var btn: Button
    private lateinit var dbBtn: Button
    private lateinit var autoButton: Button


    val DEVICE_ADMIN_ADD_REQUEST = 1001
    private lateinit var mAdminComponentName: ComponentName
    private lateinit var mDevicePolicyManager: DevicePolicyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDevicePolicyManager = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        mAdminComponentName = ComponentName(this, AdminReceiver::class.java)


        if (!isDeviceAdminApp()) {
            enableAdmin()
        }


        textView = findViewById(R.id.textView01)
        btn = findViewById(R.id.button)
        dbBtn = findViewById(R.id.db)
        autoButton = findViewById(R.id.auto_install)

        btn.text = count.toString()

        btn.setOnClickListener {
            count++
            btn.text = count.toString()
        }

        liveTextView.observe(this, { code ->
//      textView.text = code

            val inst = code.split(" ")

            when (inst[0]) {
                "stop" -> {
                    if (count == inst[1].toInt()) {
                        Toast.makeText(this, "$count 번 키오스크 이벤트", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            Log.e(TAG, "onCreate: $token")

            // Log and toast
            val msg = "MSG Token"
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        }

        dbBtn.setOnClickListener {
            startActivity(Intent(this, DatabaseActivity::class.java))
        }
        autoButton.setOnClickListener {
            startActivity(Intent(this, InstallActivity::class.java))
        }

    }

    //    private void setFilePath(Context context) {
    //        filePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/";
    //        Log.e(TAG, "setFilePath: 파일 경로 체크 : " + filePath);
    //    }
    //
    //    private void installPackage(Context context, String packageName, FileInputStream in)
    //            throws IOException {
    //        final PackageInstaller packageInstaller = context.getPackageManager().getPackageInstaller();
    //        final PackageInstaller.SessionParams params = new PackageInstaller.SessionParams(
    //                PackageInstaller.SessionParams.MODE_FULL_INSTALL);
    //
    //        params.setAppPackageName(packageName);
    //
    //        final int sessionId = packageInstaller.createSession(params);
    //        final PackageInstaller.Session session = packageInstaller.openSession(sessionId);
    //        final OutputStream out = session.openWrite(packageName, 0, -1);
    //        final byte[] buffer = new byte[65536];
    //        int bytes_read;
    //        while ((bytes_read = in.read(buffer)) != -1) {
    //            out.write(buffer, 0, bytes_read);
    //        }
    //        session.fsync(out);
    //        in.close();
    //        out.close();
    //        session.commit(createIntentSender(context, sessionId));
    //    }
    //
    //    IntentSender createIntentSender(Context context, int sessionId) {
    //        PendingIntent pendingIntent = PendingIntent.getBroadcast(
    //                context,
    //                sessionId,
    //                new Intent(ACTION_INSTALL_COMPLETE), 0
    //        );
    //        return pendingIntent.getIntentSender();
    //    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "isAdminApp: " + isDeviceAdminApp())
        Log.i(TAG, "isOwnerApp: " + isDeviceOwnerApp())
    }


    private fun isDeviceAdminApp(): Boolean {
        return mDevicePolicyManager.isAdminActive(mAdminComponentName)
    }

    private fun isDeviceOwnerApp(): Boolean {
        return mDevicePolicyManager.isDeviceOwnerApp(packageName)
    }

    private fun enableAdmin() {
        if (isDeviceAdminApp()) {
            return
        }
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminComponentName)
        startActivityForResult(intent, DEVICE_ADMIN_ADD_REQUEST)
    }
}