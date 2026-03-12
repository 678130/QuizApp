package no.hvl.quizapp

import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class QuizTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<QuizActivity>()

    @Test
    fun correctAnswerIncreasesScore() {

            composeTestRule
                .onNodeWithTag("correct_option")
                .performClick()

            composeTestRule
                .onNodeWithText("Score 1/1")
                .assertExists()
    }

    @Test
    fun wrongAnswerIncreasesScore() {

        composeTestRule
            .onAllNodesWithTag("wrong_option")[0]
            .performClick()

        composeTestRule
            .onNodeWithText("Score 0/1")
            .assertExists()
    }
}


