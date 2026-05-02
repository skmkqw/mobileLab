package pl.wsei.pam.lab03

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.animation.DecelerateInterpolator
import android.widget.GridLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.R
import java.util.Random

class Lab03Activity : AppCompatActivity() {
    private lateinit var mBoard: GridLayout
    private lateinit var mBoardModel: MemoryBoardView
    private var rows: Int = 3
    private var cols: Int = 3

    lateinit var completionPlayer: MediaPlayer
    lateinit var negativePLayer: MediaPlayer
    private var isSound: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lab03)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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
            when (event.state) {
                GameStates.Match -> {
                    if (isSound) completionPlayer.start()
                    mBoardModel.setEnabled(false)
                    var animationsFinished = 0
                    event.tiles.forEach { tile ->
                        animatePairedButton(tile.button) {
                            animationsFinished++
                            if (animationsFinished == event.tiles.size) {
                                mBoardModel.setEnabled(true)
                            }
                        }
                    }
                }
                GameStates.NoMatch -> {
                    if (isSound) negativePLayer.start()
                    mBoardModel.setEnabled(false)
                    var animationsFinished = 0
                    event.tiles.forEach { tile ->
                        animateMismatch(tile.button) {
                            tile.revealed = false
                            animationsFinished++
                            if (animationsFinished == event.tiles.size) {
                                mBoardModel.setEnabled(true)
                            }
                        }
                    }
                }
                GameStates.Finished -> {
                    if (isSound) completionPlayer.start()
                    mBoardModel.setEnabled(false)
                    var animationsFinished = 0
                    event.tiles.forEach { tile ->
                        animatePairedButton(tile.button) {
                            animationsFinished++
                            if (animationsFinished == event.tiles.size) {
                                mBoardModel.setEnabled(true)
                                Toast.makeText(this, "Gratulacje! Wygrana!", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
                else -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        completionPlayer = MediaPlayer.create(applicationContext, R.raw.completion)
        negativePLayer = MediaPlayer.create(applicationContext, R.raw.negative_guitar)
    }

    override fun onPause() {
        super.onPause()
        completionPlayer.release()
        negativePLayer.release()
    }

    private fun animatePairedButton(button: ImageButton, action: Runnable) {
        val set = AnimatorSet()
        val random = Random()
        button.pivotX = random.nextFloat() * 200f
        button.pivotY = random.nextFloat() * 200f

        val rotation = ObjectAnimator.ofFloat(button, "rotation", 1080f)
        val scallingX = ObjectAnimator.ofFloat(button, "scaleX", 1f, 4f)
        val scallingY = ObjectAnimator.ofFloat(button, "scaleY", 1f, 4f)
        val fade = ObjectAnimator.ofFloat(button, "alpha", 1f, 0f)
        set.startDelay = 500
        set.duration = 2000
        set.interpolator = DecelerateInterpolator()
        set.playTogether(rotation, scallingX, scallingY, fade)
        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {}
            override fun onAnimationEnd(animator: Animator) {
                button.scaleX = 1f
                button.scaleY = 1f
                button.alpha = 0.0f
                action.run()
            }

            override fun onAnimationCancel(animator: Animator) {}
            override fun onAnimationRepeat(animator: Animator) {}
        })
        set.start()
    }

    private fun animateMismatch(button: ImageButton, action: Runnable) {
        val rotation = ObjectAnimator.ofFloat(button, "rotation", 0f, 20f, -20f, 10f, -10f, 0f)
        rotation.duration = 500
        rotation.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                action.run()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        rotation.start()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val currentState = mBoardModel.getState()
        outState.putIntArray("board_state", currentState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.board_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.board_activity_sound -> {
                isSound = !isSound
                if (isSound) {
                    Toast.makeText(this, "Sound turn on", Toast.LENGTH_SHORT).show()
                    item.setIcon(R.drawable.outline_alarm_on_24)
                } else {
                    Toast.makeText(this, "Sound turn off", Toast.LENGTH_SHORT).show()
                    item.setIcon(R.drawable.outline_alarm_off_24)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
