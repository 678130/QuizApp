package no.hvl.quizapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    lateinit var imageManager: ImageManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        imageManager = ImageManager(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val galleryButton = findViewById<Button>(R.id.gallery_button)
        galleryButton.setOnClickListener {
            startActivity(Intent(this, GalleryActivity::class.java))
        }

        val quizButton = findViewById<Button>(R.id.quiz_button)
        quizButton.setOnClickListener {
            startActivity(Intent(this, QuizActivity::class.java))
        }

    }
}