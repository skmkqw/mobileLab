package pl.wsei.pam.lab03

import android.os.Bundle
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R

class Lab03Activity : AppCompatActivity() {
    private lateinit var mBoard: GridLayout
    private lateinit var mBoardModel: MemoryBoardView
    private var rows: Int = 3
    private var cols: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)

        mBoard = findViewById(R.id.gridLayoutBoard)

        val size = intent.getIntArrayExtra("size") ?: intArrayOf(3, 3)
        rows = size[0]
        cols = size[1]
        mBoard.rowCount = rows
        mBoard.columnCount = cols

        mBoardModel = MemoryBoardView(mBoard, cols, rows)

        if (savedInstanceState != null) {
            val savedState = savedInstanceState.getIntArray("board_state")
            if (savedState != null) {
                mBoardModel.setState(savedState)
            }
        }

        mBoardModel.setOnGameChangeListener { event ->
            if (event.state == GameStates.Finished) {
                // komunikat o wygranej
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val currentState = mBoardModel.getState()
        outState.putIntArray("board_state", currentState)
    }
}