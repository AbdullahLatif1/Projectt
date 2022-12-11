package com.example.tiktokvideodownloader.ui.main

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tiktokvideodownloader.App1
import com.example.tiktokvideodownloader.App1.mInterstitialAd
import com.example.tiktokvideodownloader.R
import com.example.tiktokvideodownloader.di.ServiceLocator
import com.example.tiktokvideodownloader.ui.permission.PermissionRequester
import com.example.tiktokvideodownloader.ui.service.QueueService
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback

class SplashActivity : AppCompatActivity() {
    var progressBar: ProgressBar? = null
    var text: TextView? = null
    var progressStatus = 0
    var intent_id = 0
    var handler = Handler()
    private val permissionRequester: PermissionRequester by lazy {
        ServiceLocator.permissionModule.permissionRequester
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionRequester(this)
        stopService(QueueService.buildIntent(this))
        setContentView(R.layout.activity_splash)
        App1.load_inter(applicationContext)
        progressBar = findViewById<View>(R.id.progressBar1) as ProgressBar
        text = findViewById<TextView>(R.id.getSTartedBtn) as TextView
        Thread {
            while (progressStatus < 100) {
                progressStatus += 1
                handler.post { progressBar!!.progress = progressStatus }
                try {
                    Thread.sleep(50)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            if (progressStatus == 100) {
                progressBar!!.visibility =View.INVISIBLE
            }
        }.start()
        Handler().postDelayed({
            text!!.visibility = View.VISIBLE
        }, 5100)
        text!!.setOnClickListener{
            all_files11()

        }
    }
    fun all_files11(){
        intent_id = 1
        show_full_screen_ad()
    }
    fun show_full_screen_ad() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(this)
            mInterstitialAd.setFullScreenContentCallback(object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    super.onAdFailedToShowFullScreenContent(adError)
                    handle_intent(intent_id)
                }

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    handle_intent(intent_id)
                }
            })
        } else {
            handle_intent(intent_id)
        }
    }
    fun handle_intent(intent_id: Int) {
        when(intent_id) {
            1 ->{
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }

        }
    }
    }