package com.ovenui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class HeatOptions : AppCompatActivity() {

    lateinit var heatMode : String
    var temperature : Int = 0
    var timeTimer : Int = 0
    var timer : Boolean = false
    var oven : Boolean = false
    var cookTop : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_heat_options)

        heatMode = intent.getStringExtra("heatMode").toString()
        oven = intent.getStringExtra("oven").equals("oven")
        cookTop = intent.getStringExtra("cookTop").toString()

        val toolbar = findViewById(R.id.heat_options_toolbar) as Toolbar?

        toolbar?.setTitle(heatMode + " options")

        setSupportActionBar(toolbar)

        toolbar?.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar?.setNavigationOnClickListener {
            this.finish()
        }

        val seekBar = findViewById<SeekBar>(R.id.temp_seek_bar)
        seekBar.max = 250
        seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            @SuppressLint("SetTextI18n")
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                val tempText = findViewById<TextView>(R.id.set_temperature_text)
                temperature = progress
                tempText.setText(""+temperature)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {

            }
        })

        alterVisibilityTimerOptions(false)

    }

    fun onClickAddTimer(view: View) {
        when(view.id) {
            R.id.plus_five_timer -> {
                plusTime(5)
            }
            R.id.plus_one_timer -> {
                plusTime(1)
            }
            R.id.minus_five_timer -> {
                plusTime(-5)
            }
            R.id.minus_one_timer -> {
                plusTime(-1)
            }
        }
    }

    fun onClickTimerButton(view: View){
        timer = true
        alterVisibilityTimerOptions(true)
    }

    fun onClickTimerButtonF(view: View){
        timer = false
        alterVisibilityTimerOptions(false)
    }

    private fun alterVisibilityTimerOptions(visibility: Boolean) {
        val viewList = ArrayList<TextView>()
        viewList.add(findViewById<TextView>(R.id.plus_five_timer))
        viewList.add(findViewById<TextView>(R.id.plus_one_timer))
        viewList.add(findViewById<TextView>(R.id.minus_five_timer))
        viewList.add(findViewById<TextView>(R.id.minus_one_timer))
        viewList.add(findViewById<TextView>(R.id.set_timer_text))
        viewList.add(findViewById<TextView>(R.id.timer_title))

        if(visibility){
            for(view in viewList){
                view.visibility = View.VISIBLE
            }
            val timerBut = findViewById<Button>(R.id.add_timer)
            timerBut.visibility = View.INVISIBLE
            val timerBut2 = findViewById<Button>(R.id.remove_timer)
            timerBut2.visibility = View.VISIBLE
        } else {
            for (view in viewList) {
                view.visibility = View.INVISIBLE
            }
            val timerBut = findViewById<Button>(R.id.add_timer)
            timerBut.visibility = View.VISIBLE
            val timerBut2 = findViewById<Button>(R.id.remove_timer)
            timerBut2.visibility = View.INVISIBLE

        }

    }

    @SuppressLint("SetTextI18n")
    private fun plusTime(extraTime : Int){
        timeTimer = timeTimer + extraTime;
        if(timeTimer < 0) timeTimer = 0
        if(timeTimer > 59) timeTimer = 59

        val timerText = findViewById<TextView>(R.id.set_timer_text)

        if(timeTimer < 10) timerText.setText("0${timeTimer}:00")
        else timerText.setText("${timeTimer}:00")
    }

    @SuppressLint("SetTextI18n")
    private fun plusTemp(extraTemp : Int) {
        temperature = temperature + extraTemp
        if(temperature < 0) temperature = 0
        if(temperature > 250) temperature = 250

        val tempText = findViewById<TextView>(R.id.set_temperature_text)
        val seekBar = findViewById<SeekBar>(R.id.temp_seek_bar)
        tempText.setText(""+temperature)
        seekBar.setProgress(temperature)
    }

    fun onClickBegin(view: View){
        if(temperature == 0) return

        val mainIntent = Intent(this, MainActivity::class.java)
        val heatIntent = Intent(this, Status::class.java)

        if (timer) {
            heatIntent.putExtra("hasTimer", "hasTimer")
            if(oven) {
                heatIntent.putExtra("oven", "oven")
                State.getInstance().oven.mealState.setTimer(timeTimer, 0)
                State.getInstance().oven.mealState.activate()
            }else {
                heatIntent.putExtra("oven", "not")
                heatIntent.putExtra("cookTop", cookTop)
                val index = Integer.parseInt(cookTop)
                State.getInstance().burners.get(index).mealState.setTimer(timeTimer, 0)
                State.getInstance().burners.get(index).mealState.activate()
            }
        }
        else {
            heatIntent.putExtra("hasTimer", "hasTimer")
            if(oven) {
                heatIntent.putExtra("oven", "oven")
                State.getInstance().oven.mealState.set()
                State.getInstance().oven.mealState.activate()
            }else {
                heatIntent.putExtra("oven", "not")
                heatIntent.putExtra("cookTop", cookTop)
                val index = Integer.parseInt(cookTop)
                State.getInstance().burners.get(index).mealState.set()
                State.getInstance().burners.get(index).mealState.activate()
            }
        }

        startActivity(heatIntent)
        finish()
    }

    fun onClickAddTemp(view: View){
        when(view.id) {
            R.id.plus_temp_but -> {
                plusTemp(1)
            }
            R.id.minus_temp_but -> {
                plusTemp(-1)
            }
        }
    }

    fun onClickAbort(view: View){
        finish()
    }

}