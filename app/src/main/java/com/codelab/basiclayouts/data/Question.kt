package com.codelab.basiclayouts.data

import androidx.compose.material.icons.filled.AccountCircle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.DisabledByDefault
import androidx.compose.material.icons.filled.PhotoAlbum
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.ui.graphics.vector.ImageVector
import com.codelab.basiclayouts.R

/**
 * A data class to represent the information presented in the dog card
 */

// This data class contains a Question object containing the relevant parameters for displaying FAQ questions
// The list of questions contains all the 10 FAQ questions, including the question, answer and icon


data class Question(
    val icon: ImageVector,
    @DrawableRes val imageResourceId: Int,
    @StringRes val questionNum: Int,
    val answer: String,
    @StringRes val question: Int
)

// Each question represents a new Question object declared with the proper parameters
// The questions are stored in a list of Question objects which can be used later to display the FAQs to the user

val questions = listOf(
    Question(
        Icons.Filled.AccountCircle,
        R.drawable.jojiuser,
        R.string.question_name_1,
        "Our initial target audience was people with impaired vision, however anyone can use Global Vision!",
        R.string.question_description_1
    ),
    Question(
        Icons.Filled.Timer,
        R.drawable.clock,
        R.string.question_name_2,
        "Global Vision was created in 2023",
        R.string.question_description_2
    ),
    Question(
        Icons.Default.Biotech,
        R.drawable.tech,
        R.string.question_name_3,
        "Global Vision uses an Image API that was trained via TensorFlow",
        R.string.question_description_3
    ),
    Question(
        Icons.Filled.VerifiedUser,
        R.drawable.add,
        R.string.question_name_4,
        "Its simple, when opening the app, navigate to the sign up page and enter your details to create a new user",
        R.string.question_description_4
    ),
    Question(
        Icons.Filled.PhotoAlbum,
        R.drawable.camera,
        R.string.question_name_5,
        "Click the bottom navigation card, and add your photo to the specific directory",
        R.string.question_description_5
    ),
    Question(
        Icons.Filled.TextFields,
        R.drawable.tts,
        R.string.question_name_6,
        "The text to speech feature is on by default",
        R.string.question_description_6
    ),
    Question(
        Icons.Filled.DisabledByDefault,
        R.drawable.offfer,
        R.string.question_name_7,
        "To turn off the text-to-speech feature, open the menu on the home page, click settings and turn the text-to-speech off",
        R.string.question_description_7
    ),
    Question(
        Icons.Filled.AccountCircle,
        R.drawable.cody,
        R.string.question_name_8,
        "Global Vision was made in a span of 4 months",
        R.string.question_description_8
    ),
    Question(
        Icons.Filled.AccountCircle,
        R.drawable.tooly,
        R.string.question_name_9,
        "Global vision uses Kotlin and the Jetpack Compose Android Studio toolkit",
        R.string.question_description_9
    )
)