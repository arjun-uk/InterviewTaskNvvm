package com.education.interviewtask.ui.activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.education.interviewtask.R

open class SuperActivity : AppCompatActivity() {
    private lateinit var loadingDialog: Dialog

    @SuppressLint("ObsoleteSdkInt")
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected == true
        }
    }
    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun showLoadingDialog() {
        if (!::loadingDialog.isInitialized) {
            loadingDialog = Dialog(this)
            loadingDialog.setContentView(R.layout.dialog_loading)
            loadingDialog.setCancelable(false)
            loadingDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        if (!loadingDialog.isShowing) {
            loadingDialog.show()
        }
    }

    fun hideLoadingDialog() {
        if (::loadingDialog.isInitialized && loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }

}