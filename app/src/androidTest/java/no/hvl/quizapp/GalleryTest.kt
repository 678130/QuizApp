package no.hvl.quizapp

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GalleryTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<GalleryActivity>()

    @Before
    fun setup() {
        Intents.init()
    }

    @After
    fun tearDown() {
        Intents.release()
    }

    @Test
    fun addImage_increasesImageCount() {

        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val imageUri = Uri.parse(
            "android.resource://${context.packageName}/${R.drawable.test_image}"
        )

        val resultData = Intent().apply {
            data = imageUri
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        val result = Instrumentation.ActivityResult(
            Activity.RESULT_OK,
            resultData
        )

        intending(hasAction(Intent.ACTION_OPEN_DOCUMENT))
            .respondWith(result)

        composeTestRule
            .onNodeWithTag("add_image_button")
            .performClick()

        composeTestRule
            .onNodeWithTag("label_field")
            .performTextInput("Cat")

        composeTestRule
            .onNodeWithTag("save_label_button")
            .performClick()

        composeTestRule
            .onNodeWithText("Cat")
            .assertExists()
    }
    @Test
    fun deleteImage_removesImage() {

        composeTestRule
            .onNodeWithTag("add_image_button")
            .performClick()

        composeTestRule
            .onNodeWithTag("label_field")
            .performTextInput("Cat")

        composeTestRule
            .onNodeWithTag("save_label_button")
            .performClick()

        composeTestRule
            .onNodeWithText("Cat")
            .assertExists()

        composeTestRule
            .onNodeWithTag("delete_image_button")
            .performClick()

        composeTestRule
            .onNodeWithText("Cat")
            .assertDoesNotExist()
    }
}