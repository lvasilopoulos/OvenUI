package com.ovenui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClickCookTop(view: View) {
        val cookTopIntent = Intent(this, CookTop::class.java)
        startActivity(cookTopIntent)
    }

    fun onClickOven(view: View){
        if(State.getInstance().oven.mealState.isActive()) {
            val ovenIntent = Intent(this, Status::class.java)
            ovenIntent.putExtra("oven", "oven")
            startActivity(ovenIntent)
        }else {
            val ovenIntent = Intent(this, Oven::class.java)
            startActivity(ovenIntent)
        }
    }
}