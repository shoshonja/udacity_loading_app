package com.udacity.ui.details

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.R
import com.udacity.ui.main.INTENT_CONTENT
import com.udacity.ui.main.INTENT_STATUS
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val extras = intent?.extras
        if (extras != null) {
            val successFromExtra = extras.get(INTENT_STATUS)
            Toast.makeText(
                this,
                "We have status, and it is $successFromExtra",
                Toast.LENGTH_SHORT
            ).show()

            val uriFromExtra = extras.get(INTENT_CONTENT)
            Toast.makeText(
                this,
                "We also have URI, and it is $uriFromExtra",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
