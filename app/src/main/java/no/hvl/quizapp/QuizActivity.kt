package no.hvl.quizapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import kotlin.getValue


/**
 * Activity responsible for running the quiz.
 *
 * It:
 * - Loads quiz data from ImageManager
 * - Displays questions and answer buttons
 * - Tracks score and progress
 * - Shows a result screen when finished
 */
class QuizActivity : AppCompatActivity() {
    private val viewModel: QuizViewModel by viewModels()
    private lateinit var content: () -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //val manager: ImageManager = (application as App).imageManager
        //val quiz = Quiz(manager)


        setContent {
            //var score by remember { mutableIntStateOf(quiz.score) }
            //var question by remember { mutableStateOf(quiz.currentQuestion) }
            //var isFinished by remember { mutableStateOf(false) }
            val vm = viewModel
            val quiz = vm.quiz
            val score = vm.score
            val currentQuestion = vm.question
            val isFinished = vm.isFinished

            if (isFinished) {
                ResultScreen(
                    score = score,
                    maxScore = quiz.questions.size,
                    onRestart = {
                        quiz.restartQuiz()
                        vm.score = 0
                        vm.question = quiz.questions[quiz.currentIndex]
                        vm.isFinished = false
                    }
                )
            } else if (currentQuestion != null) {
                QuizScreen(
                    scoreText = "Score ${quiz.score}/${quiz.currentIndex}",
                    indexText = "Question ${quiz.currentIndex + 1}/${quiz.questions.size}",
                    imageUri = currentQuestion!!.imageUri,
                    options = currentQuestion!!.alternatives,
                    onOptionClick = { selected ->
                        if (quiz.evalAnswer(selected)) {
                            quiz.score++
                            vm.score = quiz.score
                        }
                        if (quiz.nextQuestion() == null) {
                            vm.isFinished = true
                        }

                        if (quiz.currentIndex < quiz.questions.size) {
                            vm.question = quiz.questions[quiz.currentIndex]
                        }
                    }
                )
            } else {
                NotEnoughQuestionsScreen()
            }


        }
    }
    /**
     * Main quiz screen.
     *
     * Displays:
     * - Score and question index
     * - Current image
     * - Four answer buttons
     */
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

    /**
     * Result screen shown when the quiz is finished.
     *
     * Displays:
     * - Final score
     * - Restart button
     */
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

    @Composable
    fun NotEnoughQuestionsScreen() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Not enough questions.",
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

        }
    }

}


