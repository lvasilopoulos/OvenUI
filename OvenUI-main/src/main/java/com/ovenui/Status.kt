package com.ovenui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Status : AppCompatActivity() {

    var program : String = ""
    var oven : Boolean = false
    var cookTop : String = ""
    var cookTopIndex : Int = -1
    lateinit var cancelBut : Button
    var hasTimer : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        program = State.getInstance().program
        oven = intent.getStringExtra("oven").equals("oven")
        hasTimer = intent.getStringExtra("hasTimer").toString() == "hasTimer"
        cookTop = intent.getStringExtra("cookTop").toString()
        if(cookTop != "" && !oven) {
            cookTopIndex = Integer.parseInt(cookTop)
            hasTimer = State.getInstance().burners[cookTopIndex].mealState.hasTimer()
        }
        if(oven) {
            hasTimer = State.getInstance().oven.mealState.hasTimer()
        }
        val toolbar = findViewById(R.id.status_toolbar) as Toolbar?
        cancelBut = findViewById(R.id.cancel_button)


        toolbar?.setTitle("Status")

        setSupportActionBar(toolbar)

        toolbar?.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar?.setNavigationOnClickListener {
            val mainIntent = Intent(this, MainActivity::class.java)
            mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(mainIntent)
        }

        val progressBarFull = findViewById<ProgressBar>(R.id.timer_progress_bar_full)
        val progressBarCancel = findViewById<ProgressBar>(R.id.timer_progress_bar_cancel)
        val progressBar = findViewById<ProgressBar>(R.id.timer_progress_bar)
        val elapsed_time = findViewById<TextView>(R.id.elapsed_time)
        val reset_button = findViewById<Button>(R.id.reset_button)

        if(hasTimer) {
            progressBarFull.setProgress(100)
            progressBarCancel.setProgress(100)
            progressBarFull.visibility = View.INVISIBLE
            progressBarCancel.visibility = View.INVISIBLE
            cancelBut.visibility = View.VISIBLE
            elapsed_time.visibility = View.INVISIBLE
            reset_button.visibility = View.INVISIBLE

            cancelBut.setOnClickListener {

                    val timerText = findViewById<TextView>(R.id.status_timer)

                    timerText.setText("--:--")
                    progressBar.visibility = View.INVISIBLE
                    progressBarCancel.visibility = View.VISIBLE

                    if(oven) {
                        State.getInstance().oven.mealState.deactivate()
                    }
                    cancelBut.visibility = View.INVISIBLE

            }

            reset_button.setOnClickListener{
                if(oven) {
                    State.getInstance().oven.mealState.deactivate()
                    val mainIntent = Intent(this, MainActivity::class.java)
                    mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(mainIntent)
                } else {
                    State.getInstance().burners[cookTopIndex].mealState.deactivate()
                    val mainIntent = Intent(this, MainActivity::class.java)
                    mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(mainIntent)
                }
            }
        }else {
            progressBarFull.visibility = View.INVISIBLE
            progressBarCancel.visibility = View.INVISIBLE
            progressBar.visibility = View.INVISIBLE
            cancelBut.visibility = View.INVISIBLE
            elapsed_time.visibility = View.VISIBLE
            reset_button.visibility = View.VISIBLE

            reset_button.setOnClickListener{
                if(oven) {
                    State.getInstance().oven.mealState.deactivate()
                    val mainIntent = Intent(this, MainActivity::class.java)
                    mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(mainIntent)
                } else {
                    State.getInstance().burners[cookTopIndex].mealState.deactivate()
                    val mainIntent = Intent(this, MainActivity::class.java)
                    mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(mainIntent)
                }
            }
        }

        CoroutineScope(Main).launch {
            if(hasTimer) getTime()
            else getElapsedTime()
        }

    }

    override fun onBackPressed() {
        val mainIntent = Intent(this, MainActivity::class.java)
        mainIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(mainIntent)
    }

    private fun getNumText(num: Int) : String{
        if (num < 10) return "0$num"
        return ""+num
    }

    private fun getTimeText(minutes: Int, seconds: Int) : String {
        return getNumText(minutes) + ":" + getNumText(seconds)
    }

    private suspend fun updateProgressBar(totalSeconds: Int, progress: Int){
        withContext(Main){
            val timerText = findViewById<TextView>(R.id.status_timer)
            val progressBar = findViewById<ProgressBar>(R.id.timer_progress_bar)

            timerText.setText(getTimeText(totalSeconds / 60, totalSeconds % 60))

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                progressBar.setProgress(progress, true)
            } else {
                progressBar.setProgress(progress)
            }

            if(progress == 100) {
                val progressBarFull = findViewById<ProgressBar>(R.id.timer_progress_bar_full)
                val reset_button = findViewById<Button>(R.id.reset_button)
                progressBarFull.setProgress(100)
                progressBarFull.visibility = View.VISIBLE
                cancelBut.visibility = View.INVISIBLE
                progressBar.visibility = View.INVISIBLE
                reset_button.visibility = View.VISIBLE
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun getElapsedTime(){
        if(oven) {
            while(true){
                if(!State.getInstance().oven.mealState.isActive()) break;
                val timerText = findViewById<TextView>(R.id.status_timer)
                val secondsPassed = (State.getInstance().oven.mealState.getTimeLapsed()).toInt()
                timerText.setText(getTimeText(secondsPassed / 60, secondsPassed % 60))
                delay(1000)
            }
        } else {
            while(true){
                if(!State.getInstance().burners[cookTopIndex].mealState.isActive()) break;
                val timerText = findViewById<TextView>(R.id.status_timer)
                val secondsPassed = (State.getInstance().burners[cookTopIndex].mealState.getTimeLapsed()).toInt()
                timerText.setText(getTimeText(secondsPassed / 60, secondsPassed % 60))
                delay(1000)
            }
        }
    }

    private suspend fun getTime(){
        if(oven) {
            while(true){
                if(!State.getInstance().oven.mealState.isActive()) break;
                State.getInstance().oven.mealState.tick()
                // set the progress bar
                val progress = State.getInstance().oven.mealState.getProgress()
                updateProgressBar(
                    State.getInstance().oven.mealState.getRemainingTime().toInt(),
                    progress
                )
                if(State.getInstance().oven.mealState.getFinished()) break;
                delay(1000)
            }
        } else {
            while(true){
                if(!State.getInstance().burners[cookTopIndex].mealState.isActive()) break;
                State.getInstance().burners[cookTopIndex].mealState.tick()
                // set the progress bar
                val progress = State.getInstance().burners[cookTopIndex].mealState.getProgress()
                updateProgressBar(
                    State.getInstance().burners[cookTopIndex].mealState.getRemainingTime().toInt(),
                    progress
                )
                if(State.getInstance().burners[cookTopIndex].mealState.getFinished()) break;
                delay(1000)
            }
        }
    }

}