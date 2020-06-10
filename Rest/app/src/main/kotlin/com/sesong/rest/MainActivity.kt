package com.sesong.rest.ui

import kotlin.jvm.java

class MainActivity : androidx.appcompat.app.AppCompatActivity() {

    companion object {
        const val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = -4161
    }

    private val handler = android.os.Handler()
    private var countServiceFlag = false
    private var filterServiceFlag = true
    private var mainTextFlag = false
    private val vibrator by lazy { getSystemService(android.content.Context.VIBRATOR_SERVICE) as android.os.Vibrator }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.sesong.rest.R.layout.activity_main)
        rightSeat.setOnClickListener { android.widget.Toast.makeText(this@MainActivity, "기능 준비중 입니다.", android.widget.Toast.LENGTH_SHORT).show() }
        phoneNoti.setOnClickListener { v -> setTimeNoti() }
        eyeStretch.setOnClickListener {
            if (mainTextFlag) {
                android.widget.Toast.makeText(this@MainActivity, "이미 기능이 실행 중 입니다.", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                mainTextFlag = true
                eyeStretchFun()
            }
        }
        blueScreen.setOnClickListener {
            if (filterServiceFlag) {
                checkPermission()
            } else {
                val intent = android.content.Intent(this@MainActivity, com.sesong.rest.Service.FilterService::class.java)
                stopService(intent)
                android.widget.Toast.makeText(this@MainActivity, "블루스크린 차단 필터가 종료되었습니다.", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun eyeStretchFun() {
        Thread(Runnable {
            val stretching = java.util.ArrayList<String>()
            stretching.add("초점을 맞추지 않은 채로 가볍게 위를 본다")
            stretching.add("눈을 감는다")
            stretching.add("눈을 최대한 부릅뜬다")
            stretching.add("양쪽 시선을 우측으로 고정한다")
            stretching.add("양쪽 시선을 좌측으로 고정한다")
            stretching.add("양쪽 시선을 위쪽으로 고정한다")
            stretching.add("양쪽 시선을 아래쪽으로 고정한다")
            stretching.add("눈 주위를 누르며\n\n안쪽에서 바깥쪽으로 문질러준다")
            stretching.add("눈꺼풀 위를 가볍게 누른다")
            stretching.add(":)")
            for (i in 0..9) {
                try {
                    Thread.sleep(3000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                handler.post {
                    mainText.textSize = 12f
                    mainText.text = stretching[i]
                    vibrator.vibrate(200)
                }
            }
            mainTextFlag = false
        }).start()
    }

    private fun setTimeNoti() {
        countServiceFlag = !countServiceFlag
        if (countServiceFlag) {
            android.widget.Toast.makeText(this@MainActivity, "한번 더 누르면 기능을 종료하실 수 있습니다.", android.widget.Toast.LENGTH_SHORT).show()
            val intent = android.content.Intent(this, com.sesong.rest.Service.MyService::class.java)
            startService(intent)
        } else {
            android.widget.Toast.makeText(this@MainActivity, "한번 더 누르면 기능을 시작하실 수 있습니다.", android.widget.Toast.LENGTH_SHORT).show()
            val intent = android.content.Intent(this, com.sesong.rest.Service.MyService::class.java)
            stopService(intent)
        }
    }

    fun showCountText(mCount: Int) {
        if (!mainTextFlag) {
            mainText!!.textSize = 64f
            mainText!!.text = mCount.toString()
        }
        progressBar!!.progress = mCount
    }

    private fun checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (!android.provider.Settings.canDrawOverlays(this)) {
                val intent = android.content.Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        android.net.Uri.parse("package:$packageName"))
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            } else {
                filterServiceFlag = !filterServiceFlag
                android.widget.Toast.makeText(this@MainActivity, "블루스크린 차단 필터가 실행되었습니다.", android.widget.Toast.LENGTH_SHORT).show()
                val intent = android.content.Intent(this@MainActivity, com.sesong.rest.Service.FilterService::class.java)
                startService(intent)
            }
        } else {
            filterServiceFlag = !filterServiceFlag
            android.widget.Toast.makeText(this@MainActivity, "블루스크린 차단 필터가 실행되었습니다.", android.widget.Toast.LENGTH_SHORT).show()
            val intent = android.content.Intent(this@MainActivity, com.sesong.rest.Service.FilterService::class.java)
            startService(intent)
        }
    }

    @android.annotation.TargetApi(android.os.Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (!android.provider.Settings.canDrawOverlays(this)) {
                android.widget.Toast.makeText(this@MainActivity, "블루스크린 차단 필터를 사용하려면 권한 승인을 해주세요.", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                startService(android.content.Intent(this@MainActivity, com.sesong.rest.Service.MyService::class.java))
            }
        }
    }

    public override fun onResume() {
        super.onResume()
//        BroadcastReceiver.getInstance(this).registerReceiver(mMessageReceiver,
//                IntentFilter("CountingValue"))
    }

    private val mMessageReceiver: android.content.BroadcastReceiver = object : android.content.BroadcastReceiver() {
        override fun onReceive(context: android.content.Context, intent: android.content.Intent) {
            val message = intent.getIntExtra("Counting", 0)
            android.util.Log.d("message ", message.toString())
            showCountText(message)
        }
    }

    override fun onPause() {
//        Local.getInstance(this).unregisterReceiver(mMessageReceiver)
        super.onPause()
    }
}