package com.codelab.basiclayouts

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.codelab.basiclayouts.data.questions
import com.codelab.basiclayouts.ui.theme.MySootheTheme
import androidx.compose.ui.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.height
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Spa
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.codelab.basiclayouts.data.Question
import com.codelab.basiclayouts.data.questions
import com.codelab.basiclayouts.ui.theme.taupe100
import java.util.Locale



private lateinit var textToSpeech: TextToSpeech

class CommonQuestions : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            MySootheTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                   QuestionPage()
                }
            }

            // Create TTS Engine
            textToSpeech = TextToSpeech(this) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    // TextToSpeech engine is successfully initialized
                    textToSpeech.language = Locale.ENGLISH
                    val audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
                    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
                    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
                    val increasedVolume = (maxVolume * 20).toInt() // Increase by 80%
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0)

                } else {
                    // TextToSpeech engine initialization failed
                }
            }


        }


    }


    // This code displays a bottom NAV bar
    // Each element has a specific icon
    // TTS is used to provide auditory feedback after each click
    @Composable
    private fun SootheBottomNavigation(modifier: Modifier = Modifier) {
        BottomNavigation(
            backgroundColor = taupe100, // Set the background color of the BottomNavigation
            modifier = modifier.height(64.dp) // Set the height of the BottomNavigation
        ) {
            BottomNavigationItem(
                icon = {
                    IconButton(onClick = {
                        val text = "Home"
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                        val intent = Intent(this@CommonQuestions, homeScreen::class.java)
                        startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            modifier = Modifier.padding(top = 5.dp),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                },
                label = {
                    Text(
                        stringResource(R.string.bottom_navigation_home), // Set the label text for the Home item
                        color = Color.Black // Set the text color to black
                    )
                },
                selected = true, // Set whether this item is currently selected
                onClick = {} // Define the click action for the item
            )

            BottomNavigationItem(
                icon = {
                    IconButton(onClick = {
                        val text = "Camera"
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                        val intent = Intent(this@CommonQuestions, ImageClassificationActivity::class.java)
                        startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            modifier = Modifier.padding(top = 8.dp),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                },
                label = {
                    Text(
                        stringResource(R.string.maincamera_home), // Set the label text for the Camera item
                        color = Color.Black // Set the text color to black
                    )
                },
                selected = true, // Set whether this item is currently selected
                onClick = {} // Define the click action for the item
            )

            BottomNavigationItem(
                icon = {
                    IconButton(onClick = {
                        val text = "Profile"
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                        val intent = Intent(this@CommonQuestions, sideScreen::class.java)
                        startActivity(intent)
                    }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            modifier = Modifier.padding(top = 9.dp),
                            contentDescription = null,
                            tint = Color.Black
                        )
                    }
                },
                label = {
                    Text(
                        stringResource(R.string.bottom_navigation_profile), // Set the label text for the Profile item
                        color = Color.Black // Set the text color to black
                    )
                },
                selected = false, // Set whether this item is currently selected
                onClick = {} // Define the click action for the item
            )
        }
    }



    // This functions takes all the items from the questions list, and calls QuestionItem with each item
    // The elements are displayed in a LazyColumn
    @Composable
    fun QuestionPage() {

        Scaffold(
            // Creates the topBar
            topBar = {
                QuestionTopAppBar()
            },
            // Creates the bottom NAV bar
            bottomBar = { SootheBottomNavigation() }
        ) { it ->
            Box(modifier = Modifier.fillMaxSize()) {
                // Sets the background of the activity
                Image(
                    painter = painterResource(id = R.drawable.hi), // Set the drawable resource here
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillBounds
                )

                // Creates scrollable list of FAQs
                LazyColumn(contentPadding = it) {
                    items(questions) { question ->
                        QuestionItem(
                            question = question,
                            context = LocalContext.current,
                            modifier = Modifier
                                .padding(dimensionResource(R.dimen.padding_small))
                        )
                    }
                }
            }
        }
    }


    @OptIn(ExperimentalMaterialApi::class)
    // This code creates the individual FAQ element
    // It combines all of the following methods into a large function
    @Composable
    fun QuestionItem(
        question: Question,
        context: Context,
        modifier: Modifier = Modifier,
    ) {
        // Get the current context using LocalContext
        val context = LocalContext.current

        // State variable to track the expansion state of the question
        var expanded by remember { mutableStateOf(false) }

        Card(
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                    .clickable {
                        // When the card is clicked, retrieve the question text and speak it using textToSpeech
                        val questionText = context.getString(question.question)
                        textToSpeech.speak(
                            questionText, TextToSpeech.QUEUE_FLUSH, null, null
                        )
                    }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(dimensionResource(R.dimen.padding_small))
                ) {
                    QuestionIcon(question.imageResourceId)
                    Box(modifier = Modifier.weight(1.2f)) {
                        topQuestion(question.questionNum, question.question)
                    }
                    QuestionItemButton(
                        expanded = expanded,
                        onClick = { expanded = !expanded },
                    )
                }

                if (expanded) {
                    // If the question is expanded, retrieve the answer text and speak it using textToSpeech
                    val text = "Answer" + question.answer
                    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                    questionAnswer(
                        question.answer, modifier = Modifier.padding(
                            start = dimensionResource(R.dimen.padding_medium),
                            top = dimensionResource(R.dimen.padding_small),
                            bottom = dimensionResource(R.dimen.padding_medium),
                            end = dimensionResource(R.dimen.padding_medium),
                        )
                    )
                }
            }
        }
    }

    // This code creates the expand button, which when clicked shows the question answers
    // When the expand icon is clicked, the icon changes to expand less
    @Composable
    private fun QuestionItemButton(
        expanded: Boolean,
        onClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        // A button with an icon to expand or collapse the question item

        IconButton(
            onClick = onClick, // The function to be called when the button is clicked
            modifier = modifier // Modifier for styling and layout customization
        ) {
            Icon(
                imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                // Display different icons based on the expansion state
                // If expanded is true, display the expand-less icon; otherwise, display the expand-more icon
                contentDescription = stringResource(R.string.expand_button_content_description),
                // Text description for accessibility purposes
                tint = Color.Black // Tint color of the icon
            )
        }
    }




    // This function creates the top bar, including a title and a logo
    @Composable
    fun QuestionTopAppBar(modifier: Modifier = Modifier) {
        // The image resource is set to a question mark icon for the FAQ page
        // Text set to "FAQ"
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.image_size))
                            .padding(dimensionResource(R.dimen.padding_small)),
                        painter = painterResource(R.drawable.toplog),

                        // Content Description is not needed here - image is decorative, and setting a
                        // null content description allows accessibility services to skip this element
                        // during navigation.

                        contentDescription = null
                    )
                    Text(
                        text = stringResource(R.string.app_names),
                        style = MaterialTheme.typography.h1
                    )
                }
            },
            modifier = modifier
        )
    }


    // This function creates the icon for each question, which aids the user to identify the question genre or category
    // The function accepts a drawable resource and adjusts the size and shape to fit the FAQ box
    @Composable
    fun QuestionIcon(
        @DrawableRes questionIcon: Int,
        modifier: Modifier = Modifier
    ) {
        Image(
            modifier = modifier
                .size(dimensionResource(R.dimen.image_size))
                .padding(dimensionResource(R.dimen.padding_small))
                .clip(MaterialTheme.shapes.small),
            contentScale = ContentScale.Crop,
            painter = painterResource(questionIcon),

            // Content Description is not needed here - image is decorative, and setting a null content
            // description allows accessibility services to skip this element during navigation.

            contentDescription = null
        )
    }


    // This function displays the question
    // It accepts the number of the question (ex : Q3) and the actual question
    // The font sizes of each parameter are adjust for visual clarity
    @Composable
    fun topQuestion(
        @StringRes questionNum: Int,
        @StringRes questionName: Int,
        modifier: Modifier = Modifier
    ) {
        Column(modifier = modifier) {
            Text(
                text = stringResource(questionNum),
                style = MaterialTheme.typography.h1,
                modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_small))
            )
            Text(
                text = stringResource(questionName),
                style = MaterialTheme.typography.h4
            )
        }
    }


    // This function displays the answer
    // It accepts a string which is the question answer
    // The font sizes of each parameter are adjust for visual clarity
    @Composable
    fun questionAnswer(
        answer: String,
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
        ) {
            Text(
                text = stringResource(R.string.about),
                style = MaterialTheme.typography.h1
            )
            Text(
                text = answer,
                style = MaterialTheme.typography.h4
            )
        }
    }
}