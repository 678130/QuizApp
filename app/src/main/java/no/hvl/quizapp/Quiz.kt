package no.hvl.quizapp

import android.util.Log
/**
 * Quiz logic class.
 *
 * Responsible for:
 * - Generating questions from images
 * - Tracking score and progress
 * - Evaluating answers
 */
class Quiz(private val imageManager: ImageManager){

    // List of all quiz questions
    lateinit var questions: MutableList<Question>
    var score:Int = 0
    var maxScore: Int = 0
    var currentIndex:Int = 0

    init {
        createQuestions()
        maxScore = questions.size
    }

    /**
     * Builds the list of questions from the images in ImageManager.
     * Each image becomes one question.
     */
    fun createQuestions() {
        val list = mutableListOf<Question>()
        for (image in imageManager.getImages())
        {
            list.add(Question(image.label, image.uri, createAlternatives(image.label)))
        }
        questions = list
    }

    /**
     * Creates a list of 4 answer options:
     * - 3 random incorrect labels
     * - 1 correct label
     * Then shuffles them.
     */
    fun createAlternatives(correct:String) : List<String> {
        var alternatives = imageManager.getImages()
            .filter{ it.label != correct}
            .shuffled()
            .take(3)
            .map { it.label } as MutableList<String>

        alternatives.add(correct)
        alternatives = alternatives.shuffled() as MutableList<String>
        return alternatives
    }

    /**
     * Checks if the selected answer is correct.
     * Returns true if correct, false otherwise.
     */
    fun evalAnswer(answer:String): Boolean {
        val eval = (questions[currentIndex].correct == answer)
        if (eval){
            Log.d("Quiz", "Correct answer: $score / $maxScore")
        }
        return eval
    }

    /**
     * Moves to the next question.
     *
     * Returns:
     * - The next Question if available
     * - null if the quiz is finished
     */
    fun nextQuestion(): Question?{
        currentIndex++
        if (currentIndex < questions.size){
            Log.d("Quiz", "Next Question, index: $currentIndex")
            Log.d("Quiz", questions[currentIndex].imageUri ?: "null")
            return questions[currentIndex]
        }
        Log.d("Quiz", "Should be finished, index: $currentIndex")
        return null
    }

    /**
     * Resets the quiz to the beginning.
     * Score and index are reset.
     */
    fun restartQuiz() {
        currentIndex = 0
        score = 0
        Log.d("Quiz", "Restarted Quiz")

    }
}

/**
 * Data class representing a single quiz question.
 *
 * @param correct The correct label for the image
 * @param imageUri URI of the image to display
 * @param alternatives List of 4 possible answers
 */
data class Question(
    val correct: String,
    val imageUri: String,
    val alternatives: List<String>
)