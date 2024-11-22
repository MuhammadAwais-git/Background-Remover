package com.bgRemover.backgroundremover

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class EmptyViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empty_view)

        val filePath = intent.getStringExtra("filePath")
        Log.d("TAG", "onCreate: EmptyViewActivity $filePath")

        val file = File(filePath!!)
        val uri = Uri.fromFile(file)

        val flag = Intent.FLAG_ACTIVITY_NEW_TASK
        val intent = Intent()
        //intent.data = Uri.parse(data)
        intent.data = uri
        intent.flags = flag
        setResult(RESULT_OK, intent)
        finish()
    }
}