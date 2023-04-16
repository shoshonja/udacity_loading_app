package com.udacity.ui.details

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.R
import com.udacity.ui.main.INTENT_CONTENT
import com.udacity.ui.main.INTENT_STATUS
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val extras = intent?.extras
        if (extras != null) {
            val successFromExtra = extras.get(INTENT_STATUS)
            detail_status_content.text = successFromExtra.toString()

            val uriFromExtra = extras.get(INTENT_CONTENT)
            detail_filename_content.text = uriFromExtra.toString()
        }

        detail_button.setOnClickListener { onBackPressed() }
    }

}
