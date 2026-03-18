package pl.wsei.pam.lab02

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pl.wsei.pam.lab01.databinding.ActivityLab02Binding
import pl.wsei.pam.lab03.Lab03Activity

class Lab02Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLab02Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLab02Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.main66Board.setOnClickListener {
            launchLab03(6, 6)
        }

        binding.main44Board.setOnClickListener {
            launchLab03(4, 4)
        }

        binding.main32Board.setOnClickListener {
            launchLab03(3, 2)
        }

        binding.main43Board.setOnClickListener {
            launchLab03(4, 3)
        }
    }

    private fun launchLab03(rows: Int, cols: Int) {
        val intent = Intent(this, Lab03Activity::class.java)

        val size = intArrayOf(rows, cols)
        intent.putExtra("size", size)

        startActivity(intent)
    }
}