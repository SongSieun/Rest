package com.sesong.rest

class LoadingActivity : androidx.appcompat.app.AppCompatActivity() {
    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(com.sesong.rest.R.style.AppTheme_loading)
        setContentView(com.sesong.rest.R.layout.activity_loading)
        android.os.Handler().postDelayed({
            val intent = android.content.Intent(baseContext, MainActivity::class.java) // Intent 선언
            startActivity(intent) // Intent 시작
            finish()
        }, 2000) // 로딩화면 시간
    }
}