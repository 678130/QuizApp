package no.hvl.quizapp

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val manager = ImageManager(application)
    val quiz = Quiz(manager)
    var score by  mutableIntStateOf(quiz.score)
    var question by mutableStateOf(quiz.currentQuestion)
    var isFinished by mutableStateOf(false)


}