package com.ident.main.mngtapp

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.ident.main.mngtapp.AppConstant.GlobalAppConstant
import com.ident.main.mngtapp.auth.AuthActivity
import com.ident.main.mngtapp.dashboard.DashBoardActivity
import com.ident.main.mngtapp.utils.SharedPrefClass

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var intent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val isLogin = SharedPrefClass().getPrefValue(
                this@SplashActivity, GlobalAppConstant.IS_LOGIN
            ).toString()

            intent = if (isLogin == "true") {
                Intent(this@SplashActivity, DashBoardActivity()::class.java)
            } else {
                Intent(this@SplashActivity, AuthActivity()::class.java)
            }
            startActivity(intent)
            finish()

        }, 3000) // 3000 is the delayed time in milliseconds.


    }
}
