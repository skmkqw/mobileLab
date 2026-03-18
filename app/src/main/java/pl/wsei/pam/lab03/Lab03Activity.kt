package pl.wsei.pam.lab03

import android.os.Bundle
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R

class Lab03Activity : AppCompatActivity() {

    private lateinit var mBoard: GridLayout
    private lateinit var mBoardModel: MemoryBoardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)

        mBoard = findViewById(R.id.gridLayoutBoard)

        val size = intent.getIntArrayExtra("size") ?: intArrayOf(3, 3)
        val rows = size[0]
        val cols = size[1]

        mBoard.rowCount = rows
        mBoard.columnCount = cols

        mBoardModel = MemoryBoardView(mBoard, cols, rows)

        mBoardModel.setOnGameChangeListener { event ->
            // Logika reagowania na odkrycie karty, np. toast lub punkty
        }
    }
}