package com.ovenui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CookTop : AppCompatActivity() {

    private lateinit var cookTopAdapter: CookTopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cook_top)

        val toolbar = findViewById(R.id.cookTopToolbar) as Toolbar?
            setSupportActionBar(toolbar)

        if (toolbar != null) {
            toolbar.setTitle(R.string.mainButCookTop)
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
            toolbar.setNavigationOnClickListener(View.OnClickListener {
                this.finish()
            })
        }

        initRecyclerView(this)
        cookTopAdapter.submitList(State.getInstance().burners)

        CoroutineScope(Dispatchers.Main).launch {

        }

    }

    private fun initRecyclerView(context: Context) {
        val cook_top_recyclerView = findViewById<RecyclerView>(R.id.cook_top_recyclerView)
        val status = Intent(this, Status::class.java)
        val heatIntent = Intent(this, HeatOptions::class.java)
        cook_top_recyclerView.apply {
            layoutManager = LinearLayoutManager(this@CookTop)
            cookTopAdapter = CookTopAdapter { item ->
                if (item.mealState.isActive()) {
                    status.putExtra("oven", "not")
                    status.putExtra("cookTop", ""+item.index)
                    startActivity(status)
                } else {
                    heatIntent.putExtra("oven", "not")
                    heatIntent.putExtra("cookTop", ""+item.index)
                    heatIntent.putExtra("heatMode", item.name)
                    startActivity(heatIntent)
                }
                item.mealState.activate()
            }
            adapter = cookTopAdapter
        }
    }

    fun cookTopItemClick(view : View) {

    }
}