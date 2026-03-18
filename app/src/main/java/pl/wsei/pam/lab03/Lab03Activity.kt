package pl.wsei.pam.lab03

import android.os.Bundle
import android.view.Gravity
import android.widget.GridLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R

class Lab03Activity : AppCompatActivity() {

    private lateinit var mBoard: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)

        mBoard = findViewById(R.id.gridLayoutBoard)

        val size = intent.getIntArrayExtra("size") ?: intArrayOf(3, 3)
        val rows = size[0]
        val cols = size[1]

        mBoard.rowCount = rows
        mBoard.columnCount = cols

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val btn = ImageButton(this).also {
                    it.tag = "${row}x${col}"
                    val layoutParams = GridLayout.LayoutParams()

                    it.setImageResource(R.drawable.baseline_rocket_lauch_24)

                    layoutParams.width = 0
                    layoutParams.height = 0
                    layoutParams.setGravity(Gravity.CENTER)
                    layoutParams.columnSpec = GridLayout.spec(col, 1, 1f)
                    layoutParams.rowSpec = GridLayout.spec(row, 1, 1f)
                    it.layoutParams = layoutParams

                    mBoard.addView(it)
                }
            }
        }
    }
}