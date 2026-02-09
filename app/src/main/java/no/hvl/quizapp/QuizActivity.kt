package no.hvl.quizapp

import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue


class QuizActivity : AppCompatActivity() {
    private lateinit var content: () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val manager: ImageManager = (application as App).imageManager
        val quiz = Quiz(manager)


        setContent {
            var score by remember { mutableIntStateOf(quiz.score) }
            var question by remember { mutableStateOf(quiz.questions[quiz.currentIndex]) }
            var isFinished by remember { mutableStateOf(false) }

            if (isFinished) {
                ResultScreen(
                    score = score,
                    maxScore = quiz.questions.size,
                    onRestart = {
                        quiz.restartQuiz()
                        score = 0
                        question = quiz.questions[quiz.currentIndex]
                        isFinished = false
                    }
                )
            } else {
                QuizScreen(
                    scoreText = "Score $score/${quiz.currentIndex}",
                    indexText = "Question ${quiz.currentIndex + 1}/${quiz.questions.size}",
                    imageUri = question.imageUri,
                    options = question.alternatives,
                    onOptionClick = { selected ->
                        if (quiz.evalAnswer(selected)) {
                            quiz.score++
                            score = quiz.score
                        }
                        if (quiz.nextQuestion() == null)
                        {
                            isFinished = true
                        }

                        if (quiz.currentIndex < quiz.questions.size) {
                            question = quiz.questions[quiz.currentIndex]
                        }
                    }
                )
            }
        }
    }
    @Composable
    fun QuizScreen(
        scoreText: String,
        indexText: String,
        imageUri: String?,
        options: List<String>,
        onOptionClick: (String) -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = scoreText,
                fontSize = 18.sp
            )
            Text(
                text = indexText,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                if (imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = "Quiz image",
                        modifier = Modifier.size(250.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                options.forEach { option ->
                    Button(
                        onClick = { onOptionClick(option) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(option)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
    @Composable
    fun ResultScreen(
        score: Int,
        maxScore: Int,
        onRestart: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Quiz Finished!",
                fontSize = 28.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Score: $score / $maxScore",
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRestart
            ) {
                Text("Restart")
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}