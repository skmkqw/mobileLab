package pl.wsei.pam.lab03

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import pl.wsei.pam.lab01.R

class Lab03Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)

        val size = intent.getIntArrayExtra("size") ?: intArrayOf(3, 3)
        val rows = size[0]
        val cols = size[1]
    }
}