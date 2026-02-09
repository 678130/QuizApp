package no.hvl.quizapp

import android.util.Log

class Quiz(private val imageManager: ImageManager){

    lateinit var questions: MutableList<Question>
    var score:Int = 0
    var maxScore: Int = 0
    var currentIndex:Int = 0

    init {
        createQuestions()
        maxScore = questions.size
    }

    fun createQuestions() {
        val list = mutableListOf<Question>()
        for (image in imageManager.getImages())
        {
            list.add(Question(image.label, image.uri, createAlternatives(image.label)))
        }
        questions = list
    }

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

    fun evalAnswer(answer:String): Boolean {
        val eval = (questions[currentIndex].correct == answer)
        if (eval){
            Log.d("Quiz", "Correct answer: $score / $maxScore")
        }
        return eval
    }

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

    fun restartQuiz() {
        currentIndex = 0
        score = 0
        Log.d("Quiz", "Restarted Quiz")

    }
}

data class Question(
    val correct: String,
    val imageUri: String,
    val alternatives: List<String>
)