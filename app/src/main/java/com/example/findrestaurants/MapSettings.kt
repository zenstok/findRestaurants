package com.example.findrestaurants

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.findrestaurants.placesAPI.Common


class MapSettings : AppCompatActivity() {

    lateinit var slider: SeekBar
    lateinit var value: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_settings)

        slider = findViewById(R.id.seekBar)
        value = findViewById(R.id.tv_result)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            slider.min = 1000
        }
        value.text = Common.radius.toString()
        slider.max = 10000
        slider.setProgress(Common.radius)

        slider.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                value.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                Toast.makeText(baseContext, "Radius is changing", Toast.LENGTH_SHORT).show()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    Common.radius = seekBar.progress


                    val intent = Intent(this@MapSettings, RandomWidget::class.java)
                    intent.action = "android.appwidget.action.APPWIDGET_UPDATE"
                    val ids =
                        AppWidgetManager.getInstance(application).getAppWidgetIds(
                            ComponentName(
                                application,
                                RandomWidget::class.java
                            )
                        )
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                    sendBroadcast(intent)
                }
            }
        })
    }
}
