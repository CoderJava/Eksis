package com.ysn.eksis

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callbackManager = CallbackManager.Factory.create()
        login_button_activity_main.setReadPermissions("email")
        login_button_activity_main.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                val accessToken = result?.accessToken.toString()
                val sharedPreferencesManager = SharedPreferencesManager(this@MainActivity)
                sharedPreferencesManager.putDataString(SharedPreferencesManager.ACCESS_TOKEN, accessToken)
            }

            override fun onCancel() {
                /* nothing to do in here */
            }

            override fun onError(error: FacebookException?) {
                Snackbar.make(findViewById(android.R.id.content), error?.message!!, Snackbar.LENGTH_LONG)
                    .show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
