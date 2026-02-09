package no.hvl.quizapp

import android.app.Application

class App : Application() {
    lateinit var imageManager: ImageManager

    override fun onCreate() {
        super.onCreate()
        imageManager = ImageManager(this)
    }
}