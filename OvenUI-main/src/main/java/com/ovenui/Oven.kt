package com.ovenui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.ovenui.ovenNavigation.OvenOptions
import com.ovenui.ovenNavigation.PremadeOptions
import com.ovenui.ovenNavigation.ViewPagerAdapter

class Oven : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oven)

        val toolbar = findViewById(R.id.ovenToolbar) as Toolbar?

        toolbar?.setTitle(R.string.mainButOven)
        setSupportActionBar(toolbar)

        setUpTabs()

        toolbar?.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar?.setNavigationOnClickListener {
            this.finish()
        }

    }


    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(OvenOptions(), "Oven options")
        adapter.addFragment(PremadeOptions(), "Premade options")

        val oven_wrapper = findViewById<ViewPager>(R.id.oven_wrapper)
        oven_wrapper.adapter = adapter
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(oven_wrapper)

    }

    fun onOvenOptionClick(view: View) {
        when(view.id) {
            R.id.grill_but -> ovenOptionToHeatOption("Grill")
            R.id.defrost_but -> ovenOptionToHeatOption("Defrost")
            R.id.warmer_but -> ovenOptionToHeatOption("Warmer")
            R.id.up_low_but -> ovenOptionToHeatOption("Up-low")
        }
    }

    fun onPremadeOptionClick(view: View) {
        when(view.id){
            R.id.roast_meat -> premadeOptionToStatus("Roast meat", 6)
            R.id.fish_but -> premadeOptionToStatus("Fish", 3)
            R.id.pizza_but -> premadeOptionToStatus("Pizza", 4)
            R.id.veg_but -> premadeOptionToStatus("Vegetable", 12)
            R.id.cake_but -> premadeOptionToStatus("Cake", 5)
            R.id.sweet_but -> premadeOptionToStatus("Sweet", 1)
        }
    }

    private fun ovenOptionToHeatOption(option: String){
        val ovenIntent = Intent(this, HeatOptions::class.java)
        ovenIntent.putExtra("heatMode", option)
        ovenIntent.putExtra("oven", "oven")
        startActivity(ovenIntent)
    }

    private fun premadeOptionToStatus(option: String, minutes : Int) {
        val ovenIntent = Intent(this, Status::class.java)
        ovenIntent.putExtra("program", option)
        ovenIntent.putExtra("minutes", minutes.toString())
        ovenIntent.putExtra("oven", "oven")
        ovenIntent.putExtra("hasTimer", "hasTimer")
        ovenIntent.putExtra("cookTop", "-1")
        State.getInstance().oven.mealState.setTimer(minutes, 0)
        State.getInstance().oven.mealState.activate()

        startActivity(ovenIntent)
        finish()
    }


}