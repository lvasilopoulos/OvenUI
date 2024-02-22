package com.ovenui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar

class Options : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_options)

        val toolbar = findViewById(R.id.ovenToolbar) as Toolbar?
        setSupportActionBar(toolbar)

        if (toolbar != null) {
            // add a parameter to the bundle (oven or burner)
            toolbar.setTitle(R.string.mainButOven)
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbar.setNavigationOnClickListener(View.OnClickListener {
                this.finish()
            })
        }

    }


}