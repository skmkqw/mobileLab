package pl.wsei.pam.lab03

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R
import java.util.Stack

data class MemoryGameEvent(
    val tiles: List<Tile>,
    val state: GameStates) {
}

class MemoryBoardView(
    private val gridLayout: GridLayout,
    private val cols: Int,
    private val rows: Int
) {
    private val tiles: MutableMap<String, Tile> = mutableMapOf()

    private val icons: List<Int> = listOf(
        R.drawable.baseline_rocket_lauch_24,
        R.drawable.baseline_batch_prediction_24,
        R.drawable.outline_account_circle_24,
        R.drawable.outline_account_box_24,
        R.drawable.outline_ad_24,
        R.drawable.outline_3p_24,
        R.drawable.outline_add_location_24,
        R.drawable.outline_adjust_24,
        R.drawable.outline_airplay_24,
        R.drawable.outline_align_flex_center_24
    )

    private val deckResource: Int = R.drawable.outline_archive_24
    private var onGameChangeStateListener: (MemoryGameEvent) -> Unit = {}
    private val matchedPair: Stack<Tile> = Stack()
    private val logic: MemoryGameLogic = MemoryGameLogic(cols * rows / 2)

    init {
        val numPairs = (cols * rows) / 2
        val shuffledIcons: MutableList<Int> = mutableListOf<Int>().also {
            it.addAll(icons.subList(0, numPairs))
            it.addAll(icons.subList(0, numPairs))
            it.shuffle()
        }

        for (row in 0 until rows) {
            for (col in 0 until cols) {
                val btn = ImageButton(gridLayout.context).also {
                    val tag = "${row}x${col}"
                    it.tag = tag

                    val layoutParams = GridLayout.LayoutParams()
                    layoutParams.width = 0
                    layoutParams.height = 0
                    layoutParams.setGravity(Gravity.CENTER)
                    layoutParams.columnSpec = GridLayout.spec(col, 1, 1f)
                    layoutParams.rowSpec = GridLayout.spec(row, 1, 1f)
                    it.layoutParams = layoutParams

                    gridLayout.addView(it)
                }

                val icon = shuffledIcons.removeAt(0)
                addTile(btn, icon)
            }
        }
    }

    private fun onClickTile(v: View) {
        val tile = tiles[v.tag.toString()]
        if (tile == null || tile.revealed) return

        matchedPair.push(tile)
        val matchResult = logic.process {
            tile.tileResource
        }

        onGameChangeStateListener(MemoryGameEvent(matchedPair.toList(), matchResult))

        if (matchResult != GameStates.Matching) {
            matchedPair.clear()
        }
    }

    fun setOnGameChangeListener(listener: (event: MemoryGameEvent) -> Unit) {
        onGameChangeStateListener = listener
    }

    private fun addTile(button: ImageButton, resourceImage: Int) {
        button.setOnClickListener(::onClickTile)
        val tile = Tile(button, resourceImage, deckResource)
        tiles[button.tag.toString()] = tile
    }
}