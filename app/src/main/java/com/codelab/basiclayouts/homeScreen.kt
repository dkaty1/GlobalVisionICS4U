package com.codelab.basiclayouts


import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.outlined.BikeScooter
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.codelab.basiclayouts.ui.theme.LightBlue
import com.codelab.basiclayouts.ui.theme.MySootheTheme
import com.codelab.basiclayouts.ui.theme.stats
import java.util.Locale


class homeScreen : AppCompatActivity() {

    // Instance Variables
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var myButton: Button
    private lateinit var myTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Runs the homeScreen
            MySootheApp()

            // Text to speech engine is declared
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

    // This function destroys the Text To Speech Engine
    // This helps to conserve resources and reduce CPU load
    override fun onDestroy() {
        super.onDestroy()
        textToSpeech.shutdown() // Shutdown the TextToSpeech engine to release resources
    }

    // This function displays tips to the user
    // It includes an informative icon, title of the tip, and the info for the user
    @Composable
    fun TipCard(
        onClick: () -> Unit,
        tipTitle: String,
        @DrawableRes imageResourceId: Int,
        tipDescription: String,
        modifier: Modifier = Modifier
    ) {
        // Container box for the tip card
        Box(
            modifier = modifier
                .clickable {
                    onClick()
                    val tempString = tipTitle + tipDescription
                    textToSpeech.speak(tempString, TextToSpeech.QUEUE_FLUSH, null, null)
                }
                .background(Color.White, MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp, vertical = 8.dp) // Adjust the padding as per requirement
        ) {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                // Box for the top portion of the card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = LightBlue)
                        .padding(horizontal = 16.dp, vertical = 8.dp) // Adjust the padding as per requirement
                ) {
                    // Row containing the tip title and image
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                    ) {
                        // Tip title
                        Text(
                            text = tipTitle,
                            style = MaterialTheme.typography.h6,
                            color = Color.Black
                        )
                        // Tip image
                        Image(
                            painter = painterResource(id = imageResourceId),
                            contentDescription = "Image",
                            modifier = Modifier
                                .size(74.dp)
                                .padding(start = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp)) // Add space between the blue box and the description
                // Box for the description portion of the card
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .height(80.dp)
                        .fillMaxWidth()
                        .background(color = Color.White)
                        .padding(horizontal = 16.dp, vertical = 8.dp) // Adjust the padding as per requirement
                ) {
                    // Tip description
                    Text(
                        text = tipDescription,
                        style = MaterialTheme.typography.h4,
                        color = Color.Black
                    )
                }
            }
        }
    }

    // This function displays stats to the user
    // It includes an informative icon, title of the stat, and relevant info for the user
    @Composable
    fun statCard(
        onClick: () -> Unit,
        tipTitle: String,
        @DrawableRes imageResourceId: Int,
        tipDescription: String,
        modifier: Modifier = Modifier
    ) {
        // Container box for the stat card
        Box(
            modifier = modifier
                .clickable {
                    onClick()
                    val tempString = tipTitle + tipDescription
                    textToSpeech.speak(tempString, TextToSpeech.QUEUE_FLUSH, null, null)
                }
                .background(Color.White, MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp, vertical = 8.dp) // Adjust the padding as per requirement
        ) {
            Column(modifier = Modifier.padding(bottom = 8.dp)) {
                // Box for the top portion of the card
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Decrease the width to 80% of the available width
                        .background(color = stats)
                        .padding(horizontal = 16.dp, vertical = 8.dp) // Adjust the padding as per requirement
                ) {
                    // Row containing the stat title and image
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                    ) {
                        // Stat title
                        Text(
                            text = tipTitle,
                            style = MaterialTheme.typography.h6,
                            color = Color.Black
                        )
                        // Stat image
                        Image(
                            painter = painterResource(id = imageResourceId),
                            contentDescription = "Image",
                            modifier = Modifier
                                .size(74.dp)
                                .padding(start = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp)) // Add space between the blue box and the description
                // Box for the description portion of the card
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f) // Decrease the width to 80% of the available width
                        .background(color = Color.White)
                        .padding(horizontal = 16.dp, vertical = 8.dp) // Adjust the padding as per requirement
                ) {
                    // Stat description
                    Text(
                        text = tipDescription,
                        style = MaterialTheme.typography.h4,
                        color = Color.Black
                    )
                }
            }
        }
    }

    // This functions puts all the tipCards into a list
    // Each tipCard is called from the list called tipData
    // Each tipCard is then formatted into lazyRow for scrolling
    @Composable
    fun TipCardList(
        modifier: Modifier = Modifier
    ) {
        // Define the data for the tip cards
        val tipData = listOf(
            TipCardData(
                title = "Tip : Backtracking",
                imageResourceId = R.drawable.swiping,
                description = "To return to the previous screen, simply swipe from the left side"
            ),
            TipCardData(
                title = "Tip : Camera",
                imageResourceId = R.drawable.perm,
                description = "Ensure your device has the proper permissions enabled to use the camera"
            )
        )
        LazyRow(modifier = modifier) {
            items(tipData) { tip ->
                // Render a TipCard for each item in the tipData list
                TipCard(
                    onClick = {},
                    tipTitle = tip.title,
                    imageResourceId = tip.imageResourceId,
                    tipDescription = tip.description,
                    modifier = Modifier.width(350.dp) // Adjust the width as per your requirement
                )
                Spacer(modifier = Modifier.width(8.dp)) // Add space between TipCards
            }
        }
    }

    // Data class representing the information for a tip card
    data class TipCardData(
        val title: String,
        @DrawableRes val imageResourceId: Int,
        val description: String
    )

    // This function creates a topBar which displays the app name and logo
    // The TTS feature is activated when the app name is clicked
    @Composable
    fun betterBar(modifier: Modifier = Modifier, onClick: () -> Unit) {
        // State to track whether the dropdown menu is expanded or not
        var expanded by remember { mutableStateOf(false) }

        // List of items for the dropdown menu
        val dropdownItems = listOf("FAQs")

        // TopAppBar composable
        TopAppBar(
            title = {
                ClickableText(
                    text = AnnotatedString("Global Vision"),
                    onClick = { offset ->
                        onClick()
                        val text = "Global Vision"
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 20.sp
                    )
                )
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "Localized description"
                    )
                }
            },
            actions = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = {
                        // Toggle the value of 'expanded' when the menu icon is clicked
                        expanded = !expanded
                        textToSpeech.speak("Menu Bar", TextToSpeech.QUEUE_FLUSH, null, null)
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Localized description"
                        )
                    }
                    if (expanded) {
                        // Render the dropdown menu only if 'expanded' is true
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            // Iterate through each item in the 'dropdownItems' list
                            dropdownItems.forEach { item ->
                                DropdownMenuItem(onClick = {
                                    // Invoke the callback and set 'expanded' to false when an item is clicked
                                    onItemClick(item)
                                    expanded = false
                                }) {
                                    Text(
                                        text = item,
                                        style = TextStyle(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }


    // This code is called when the user toggles the dropdown menu
    // If the FAQ option is selected, a new intent is created
    private fun onItemClick(item: String) {
        if (item == "FAQs"){
            // intent for commonQuestions activity created
            val intent = Intent(this@homeScreen, CommonQuestions::class.java)
            // Activity changed
            startActivity(intent)
            // TTS to state action
            textToSpeech.speak("FAQs", TextToSpeech.QUEUE_FLUSH, null, null)
        }

    }

    // This function is part of the app UI, and displays a scrollable grid of images
    @Composable
    fun FunnyEvenBetterAlignYourBodyElement(
        @DrawableRes drawable: Int,
        @StringRes text: Int,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        val context = LocalContext.current

        Surface(
            shape = MaterialTheme.shapes.medium,
            color = Color.White,
            modifier = modifier.clickable {
                onClick()
                // Speak "RECENT" using text-to-speech when clicked
                textToSpeech.speak("RECENT", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        ) {
            Column(modifier = Modifier.clickable {
                onClick()
                val spokenText = context.getString(text)
                // Speak the text retrieved from resources using text-to-speech when clicked
                textToSpeech.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null, null)
            }) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    // Display the image with the specified drawable resource
                    Image(
                        painter = painterResource(drawable),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = context.getString(text), // Retrieve the text from resources using context.getString()
                        style = MaterialTheme.typography.h4,
                        color = Color.Black,
                        modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp)
                    )
                }
            }
        }
    }



    // This function is another part of the app UI, and displays images in a row
    @Composable
    fun FavoriteCollectionCard(
        @DrawableRes drawable: Int,
        @StringRes text: Int,
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        val context = LocalContext.current

        // Surface composable represents a rectangular piece of material
        // with elevation that can display content and react to user input
        Surface(
            shape = MaterialTheme.shapes.small,
            modifier = modifier.clickable {
                onClick()
                val spokenText = context.getString(text)
                // Use text-to-speech to speak the text retrieved from resources when clicked
                textToSpeech.speak(spokenText, TextToSpeech.QUEUE_FLUSH, null, null)
            }
        ) {
            // Row composable arranges its children in a horizontal line
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.width(250.dp)
            ) {
                // Image composable displays the specified drawable resource
                Image(
                    painter = painterResource(drawable),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(100.dp)
                )
                // Text composable displays the text retrieved from resources
                Text(
                    text = stringResource(text),
                    style = MaterialTheme.typography.h3,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Black
                )
            }
        }
    }


    // This function uses the elements declared above, and formats them in a LazyRow
    // The data is from the alignYourBodyData list
    @Composable
    fun AlignYourBodyRow(
        modifier: Modifier = Modifier,
        onClick: () -> Unit
    ) {
        // LazyRow composable displays its children in a horizontal scrolling list lazily
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = modifier.clickable {
                onClick()
                //val spokenText = context.getString(text)
                // Use text-to-speech to speak "Recent" when clicked
                textToSpeech.speak("Recent", TextToSpeech.QUEUE_FLUSH, null, null)
            }
        ) {
            // items is a function that iterates over the alignYourBodyData list and creates composable elements for each item
            items(alignYourBodyData) { item ->
                // Call FunnyEvenBetterAlignYourBodyElement composable to display each item in the list
                FunnyEvenBetterAlignYourBodyElement(item.drawable, item.text, onClick = {})
            }
        }
    }

    // This function uses the elements declared above, and formats them in a LazyGrid
    // The data is from the favoriteCollectionsData list
    @Composable
    fun FavoriteCollectionsGrid(
        modifier: Modifier = Modifier
    ) {
        // LazyHorizontalGrid composable arranges its children in a grid horizontally
        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = modifier.height(120.dp)
        ) {
            // items is a function that iterates over the favoriteCollectionsData list and creates composable elements for each item
            items(favoriteCollectionsData) { item ->
                // Call FavoriteCollectionCard composable to display each item in the list
                FavoriteCollectionCard(item.drawable, item.text, Modifier.height(56.dp), onClick = {})
            }
        }
    }

    // Helps style all the UI elements
    // Keeps a consistent font size, color, and padding for all UI elements
    // HomeSection is a reusable composable used to create sections in the home screen
    @Composable
    fun HomeSection(
        @StringRes title: Int, // The resource ID of the section title string
        modifier: Modifier = Modifier,
        onClick: () -> Unit, // Callback function triggered when the section is clicked
        content: @Composable () -> Unit // The content composable that defines the items within the section
    ) {
        // Column composable arranges its children in a vertical layout
        Column(modifier = modifier.clickable { onClick() }) {
            // Text composable displays the section title
            Text(
                text = stringResource(title).uppercase(Locale.getDefault()), // Retrieve the title string from resources and convert it to uppercase
                color = Color.Black,
                style = MaterialTheme.typography.h4,
                modifier = Modifier
                    .paddingFromBaseline(top = 40.dp, bottom = 8.dp)
                    .padding(horizontal = 16.dp)
            )
            // Execute the content composable passed as a lambda parameter to display the items within the section
            content()
        }
    }





    // Up until this point, all the UI elements have been created, but not yet displayed
    // All the different UI elements are called in the HomeScreen function which displays them to the user
    @Composable
    fun HomeScreen(modifier: Modifier = Modifier) {
        Column(
            modifier
                .verticalScroll(rememberScrollState()) // Enables vertical scrolling for the column
                .fillMaxSize() // Column occupies the entire available space
        ) {
            Spacer(Modifier.height(16.dp)) // Adds a vertical space of 16.dp
            betterBar(Modifier.padding(horizontal = 10.dp), onClick = {}) // Displays the betterBar composable with padding and onClick callback
            Spacer(Modifier.height(8.dp)) // Adds a vertical space of 8.dp
            HomeSection(title = R.string.align_your_body, onClick = {textToSpeech.speak("Recent", TextToSpeech.QUEUE_FLUSH, null, null)}) {
                AlignYourBodyRow(onClick = {}) // Displays the AlignYourBodyRow composable within the HomeSection
            }
            HomeSection(title = R.string.favorite_collections, onClick = {textToSpeech.speak("Favorite Collections", TextToSpeech.QUEUE_FLUSH, null, null)}) {
                FavoriteCollectionsGrid() // Displays the FavoriteCollectionsGrid composable within the HomeSection
            }
            HomeSection(title = R.string.bottomOther, onClick = {textToSpeech.speak("Other Info", TextToSpeech.QUEUE_FLUSH, null, null)}) {
                TipCardList(Modifier.padding(4.dp)) // Displays the TipCardList composable within the HomeSection
            }
            HomeSection(title = R.string.staty, onClick = {textToSpeech.speak("Stats", TextToSpeech.QUEUE_FLUSH, null, null)}) {
                statCard(
                    onClick = {},
                    tipTitle = "Your Stats : ",
                    R.drawable.stat,
                    tipDescription = "Pictures Taken: " + getImageCounter() + "\nTotal Usage Time : 5 Hours \nDate Joined : June 2, 2023 \nConsecutive Daily Log Ins : 5 Days",
                    modifier = Modifier.padding(4.dp)
                ) // Displays the statCard composable within the HomeSection
            }
            Spacer(Modifier.height(16.dp)) // Adds a vertical space of 16.dp
        }
    }

    // Retrieves the image count from the ImageClassificationActivity
    private fun getImageCounter(): Int {
        val image = ImageClassificationActivity()
        return image.getImageCounter()
    }


    // This function displays the bottom NAV bar which holds all the user options to change screens
    // The user has options to change to the Camera, Home Screen or Profile
    @Composable
    private fun SootheBottomNavigation(modifier: Modifier = Modifier) {
        BottomNavigation(
            backgroundColor = Color.White, // Set the background color of the BottomNavigation to white
            modifier = modifier.height(64.dp) // Apply the given height to the BottomNavigation
        ) {
            // Home BottomNavigationItem
            BottomNavigationItem(
                icon = {
                    IconButton(onClick = {
                        val text = "Home"
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) // Speak the text using TextToSpeech
                        val intent = Intent(this@homeScreen, homeScreen::class.java) // Create an intent to navigate to the home screen
                        startActivity(intent) // Start the activity with the intent
                    }) {
                        Icon(
                            imageVector = Icons.Default.Spa,
                            modifier = Modifier.padding(top = 5.dp), // Apply top padding to the icon
                            contentDescription = null,
                            tint = Color.Black // Set the tint color of the icon to black
                        )
                    }
                },
                label = {
                    Text(
                        stringResource(R.string.bottom_navigation_home), // Retrieve the text from resources
                        color = Color.Black // Set the text color to black
                    )
                },
                selected = true, // Set this BottomNavigationItem as selected
                onClick = {}
            )

            // Camera BottomNavigationItem
            BottomNavigationItem(
                icon = {
                    IconButton(onClick = {
                        val text = "Camera"
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) // Speak the text using TextToSpeech
                        val intent = Intent(this@homeScreen, ImageClassificationActivity::class.java) // Create an intent to navigate to the ImageClassificationActivity
                        startActivity(intent) // Start the activity with the intent
                    }) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            modifier = Modifier.padding(top = 8.dp), // Apply top padding to the icon
                            contentDescription = null,
                            tint = Color.Black // Set the tint color of the icon to black
                        )
                    }
                },
                label = {
                    Text(
                        stringResource(R.string.maincamera_home), // Retrieve the text from resources
                        color = Color.Black // Set the text color to black
                    )
                },
                selected = true, // Set this BottomNavigationItem as selected
                onClick = {}
            )

            // Profile BottomNavigationItem
            BottomNavigationItem(
                icon = {
                    IconButton(onClick = {
                        val text = "Profile"
                        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null) // Speak the text using TextToSpeech
                        val intent = Intent(this@homeScreen, sideScreen::class.java) // Create an intent to navigate to the side screen
                        startActivity(intent) // Start the activity with the intent
                    }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            modifier = Modifier.padding(top = 9.dp), // Apply top padding to the icon
                            contentDescription = null,
                            tint = Color.Black // Set the tint color of the icon to black
                        )
                    }
                },
                label = {
                    Text(
                        stringResource(R.string.bottom_navigation_profile), // Retrieve the text from resources
                        color = Color.Black // Set the text color to black
                    )
                },
                selected = false, // Set this BottomNavigationItem as not selected
                onClick = {}
            )
        }
    }


    // This function is the final function - and combines the homeScreen and NAV bar together
// The homeScreen is created by this function
    @Composable
    fun MySootheApp() {
        MySootheTheme { // Apply the MySootheTheme to the content of MySootheApp
            Scaffold(
                bottomBar = { SootheBottomNavigation() } // Set the bottomBar of the Scaffold to the SootheBottomNavigation composable
            ) { padding ->
                HomeScreen(Modifier.padding(padding)) // Create the HomeScreen composable with padding modifier applied
            }
        }
        val rootLayout = window.decorView.rootView
        rootLayout.background = ContextCompat.getDrawable(this, R.drawable.lol6) // Set the background of the root layout to the specified drawable
    }

    // Define data for alignYourBodyData
    private val alignYourBodyData = mutableListOf(
        Pair(R.drawable.homeimage1, R.string.ab1_inversions),
        Pair(R.drawable.lol2, R.string.ab2_quick_yoga),
        Pair(R.drawable.lol3, R.string.ab3_stretching),
        Pair(R.drawable.lol4, R.string.ab4_tabata)
    ).map { DrawableStringPair(it.first, it.second) }

    // Define data for favoriteCollectionsData
    private val favoriteCollectionsData = listOf(
        R.drawable.fc1_short_mantras to R.string.fc1_short_mantras,
        R.drawable.fc2_nature_meditations to R.string.fc2_nature_meditations,
        R.drawable.fc3_stress_and_anxiety to R.string.fc3_stress_and_anxiety,
        R.drawable.fc4_self_massage to R.string.fc4_self_massage,
        R.drawable.fc5_overwhelmed to R.string.fc5_overwhelmed,
        R.drawable.fc6_nightly_wind_down to R.string.fc6_nightly_wind_down
    ).map { DrawableStringPair(it.first, it.second) }

    // Define data class for pairs of drawable and string resources
    data class DrawableStringPair(
        @DrawableRes val drawable: Int,
        @StringRes val text: Int
    )
}