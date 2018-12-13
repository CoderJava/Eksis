package com.ysn.eksis.ui

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.ysn.eksis.R
import com.ysn.eksis.storage.SharedPreferencesManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    lateinit var callbackManager: CallbackManager
    lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSharedPreferencesManager()
        callbackManager = CallbackManager.Factory.create()
        login_button_activity_main.setReadPermissions(Arrays.asList("email", "user_location", "user_gender", "user_birthday", "user_age_range", "user_friends"))
        login_button_activity_main.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                sharedPreferencesManager.putDataBoolean(SharedPreferencesManager.IS_LOGIN, true)
                val intentDashboardActivity = Intent(this@MainActivity, DashboardActivity::class.java)
                intentDashboardActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intentDashboardActivity)
            }

            override fun onCancel() {
                /* nothing to do in here */
            }

            override fun onError(error: FacebookException?) {
                Snackbar.make(findViewById(android.R.id.content), error?.message!!, Snackbar.LENGTH_LONG)
                    .show()
            }
        })

        if (sharedPreferencesManager.getDataBoolean(SharedPreferencesManager.IS_LOGIN)) {
            val intentDashboardActivity = Intent(this, DashboardActivity::class.java)
            intentDashboardActivity.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentDashboardActivity)
        }
    }

    private fun initSharedPreferencesManager() {
        sharedPreferencesManager = SharedPreferencesManager(this@MainActivity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
