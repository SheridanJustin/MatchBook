package week11.st221773.matchbook.ui.screens

import HomeViewModel
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import week11.st221773.matchbook.navigation.BottomNavigationBar
import week11.st221773.matchbook.model.Book
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import week11.st221773.matchbook.R
import week11.st221773.matchbook.navigation.Screen
import week11.st221773.matchbook.viewmodel.LibraryViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    vm: HomeViewModel,
    lvm: LibraryViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Matchbook", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier.background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF8C1EFF), Color(0xFFFF6F4E))
                    )
                )
            )
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->

        HomeScreenContent(
            modifier = Modifier.padding(padding),
            navController = navController,
            recommendations = vm.recommendations,
            isLoading = vm.isLoading.value,
            onGetRecommendations = { prompt -> vm.fetchBooks(prompt) },
            lvm = lvm
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    navController: NavHostController,
    recommendations: List<Book>,
    isLoading: Boolean,
    onGetRecommendations: (String) -> Unit,
    modifier: Modifier,
    lvm: LibraryViewModel,
) {
    var userPrompt by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF080018),
                        Color(0xFF2B004C)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = modifier
                .padding(24.dp)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {

            item {
                Spacer(Modifier.height(16.dp))

                Text(
                    "What kind of book are you looking for?",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(Modifier.height(24.dp))

                OutlinedTextField(
                    value = userPrompt,
                    onValueChange = { userPrompt = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.White,
                        unfocusedBorderColor = Color.White,
                        cursorColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    )
                )

                Spacer(Modifier.height(24.dp))

                val isPromptValid = userPrompt.isNotBlank()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFFE5398D), Color(0xFFFFA54C))
                            ),
                            shape = RoundedCornerShape(30.dp)
                        )
                        .clickable(enabled = isPromptValid) {
                            onGetRecommendations(userPrompt)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Get Recommendations",
                        color = if (isPromptValid) Color.White else Color.Gray,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                Spacer(Modifier.height(32.dp))

                if (recommendations.isNotEmpty()) {
                    Text(
                        "Recommendations",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )

                    Spacer(Modifier.height(16.dp))
                }
            }

            // BookCard items from recommendations
            items(recommendations) { book ->

                // check if fetchd books already liked/ttoberead
                val isFavorite = lvm.isFavorite(book.title)
                val isToRead = lvm.isToRead(book.title)


                BookCard(
                    book = book,
                    onAddFavorite = {
                        if (isFavorite) lvm.removeFavorite(book)
                        else lvm.addToFavorites(book)
                    },

                    onAddToRead = {
                        if (isToRead) lvm.removeToBeRead(book)
                        else lvm.addToToBeRead(book)
                    },
                    isFavorite = lvm.isFavorite(book.title),
                    isToRead = lvm.isToRead(book.title),
                    navController = navController,

                )
            }

            if (isLoading) {
                item {
                    Spacer(Modifier.height(20.dp))
                    CircularProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }

}

@Composable
fun BookCard(
    book: Book,
    onAddFavorite: () -> Unit,
    onAddToRead: () -> Unit,
    navController: NavHostController,
    isFavorite: Boolean,
    isToRead: Boolean
) {

    // local states following viewmodel values
    var favState by remember { mutableStateOf(isFavorite) }
    var toReadState by remember { mutableStateOf(isToRead) }

    // re-sync when parent updates (e.g., when navigating back)
    LaunchedEffect(isFavorite, isToRead) {
        favState = isFavorite
        toReadState = isToRead
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                // Navigate to the book details screen
                navController.navigate(Screen.BookDetails.createRoute(book.title, "home"))
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(start = 12.dp, end = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Book cover image (loaded from Google Books API via Coil)
            // The image is clipped to rounded corners and scaled properly.
            AsyncImage(
                model = book.coverUrl ?: R.drawable.default_cover,
                contentDescription = "Book Cover",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(65.dp)
                    .height(100.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Title, author, and short reason text
            // The weight modifier pushes icons to the right.
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(book.title, style = MaterialTheme.typography.titleMedium)
                Text("by ${book.author}", style = MaterialTheme.typography.bodyMedium)

                Spacer(Modifier.height(8.dp))

                Text(
                    book.reason,
                    style = MaterialTheme.typography.bodySmall
//                    modifier = Modifier.padding(horizontal = 8.dp),
//                    maxLines = 2,                                // LIMIT TO 2 LINES
//                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(12.dp))
            }

            // Column containing the To-Read and Favorite action buttons
            // Each button uses simple scale + color animations to give visual feedback.
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                val scope = rememberCoroutineScope()

                // ----- TO READ BUTTON -----
                var toReadPressed by remember { mutableStateOf(false) }

                val toReadScale by animateFloatAsState(
                    targetValue = if (toReadPressed) 1.2f else 1f,
                    animationSpec = tween(150),
                    label = ""
                )

                val toReadColor by animateColorAsState(
                    targetValue = if (toReadState || toReadPressed) Color(0xFF0A7AFF) else Color.Black,
                    animationSpec = tween(200),
                    label = ""
                )

                IconButton(
                    onClick = {
                        scope.launch {
                            toReadPressed = true

                            // 👉 Update ViewModel
                            onAddToRead()

                            // 👉 Update local state immediately
                            toReadState = !toReadState

                            delay(150)
                            toReadPressed = false
                        }
                    },
                    modifier = Modifier.scale(toReadScale)
                ) {
                    Icon(
                        imageVector = if (toReadState) Icons.Filled.Bookmark else Icons.Outlined.BookmarkAdd,
                        contentDescription = "To Read",
                        tint = toReadColor
                    )
                }


                // ----- FAVORITE BUTTON -----
                var favPressed by remember { mutableStateOf(false) }

                val favScale by animateFloatAsState(
                    targetValue = if (favPressed) 1.2f else 1f,
                    animationSpec = tween(150),
                    label = ""
                )

                val favColor by animateColorAsState(
                    targetValue = if (favState || favPressed) Color(0xFFE5398D) else Color.Black,
                    animationSpec = tween(200),
                    label = ""
                )

                IconButton(
                    onClick = {
                        scope.launch {
                            favPressed = true

                            // 👉 Update ViewModel
                            onAddFavorite()

                            // 👉 Update local state immediately
                            favState = !favState

                            delay(150)
                            favPressed = false
                        }
                    },
                    modifier = Modifier.scale(favScale)
                ) {
                    Icon(
                        imageVector = if (favState) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = favColor
                    )
                }
            }
        }
    }
}


// PREVIEW HOME SCREEN
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    val navController = rememberNavController()

    val sampleBooks = listOf(
        Book(
            title = "The Midnight Library",
            author = "Matt Haig",
            reason = "A heartfelt novel about choices and alternate lives."
        ),
        Book(
            title = "Project Hail Mary",
            author = "Andy Weir",
            reason = "A stranded astronaut must save humanity."
        ),
        Book(
            title = "Lonesome Dove",
            author = "Larry McMurty",
            reason = "A love story, an adventure, and an epic of the frontier."
        )
    )

    HomeScreenContent(
        navController = navController,
        recommendations = sampleBooks,
        isLoading = false,
        onGetRecommendations = {},
        modifier = Modifier,
        lvm = LibraryViewModel()
    )
}