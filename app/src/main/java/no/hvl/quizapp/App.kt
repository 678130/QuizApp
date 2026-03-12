package no.hvl.quizapp

import android.app.Application
import androidx.room.Room

class App : Application() {
    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "quiz-db"
        ).allowMainThreadQueries().build()
    }
    lateinit var imageManager: ImageManager

    override fun onCreate() {
        super.onCreate()
        imageManager = ImageManager(this)
    }
}