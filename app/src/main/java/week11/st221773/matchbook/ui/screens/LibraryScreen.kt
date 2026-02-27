package week11.st221773.matchbook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Bookmark
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import week11.st221773.matchbook.R
import week11.st221773.matchbook.model.Book
import week11.st221773.matchbook.navigation.BottomNavigationBar
import week11.st221773.matchbook.navigation.Screen
import week11.st221773.matchbook.viewmodel.LibraryViewModel

// LibraryScreen ViewModel + Nav
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(
    navController: NavHostController,
    viewModel: LibraryViewModel
) {
    LaunchedEffect(Unit) {
        viewModel.loadLibrary()
    }

    LibraryScreenContent(
        navController = navController,
        selectedTab = 0,
        onTabSelected = { /* ViewModel doesn’t need this */ },
        favorites = viewModel.favorites,
        toBeRead = viewModel.toBeRead,
        isLoading = viewModel.isLoading.value,
        onRemoveFavorite = { viewModel.removeFavorite(it) },
        onRemoveToBeRead = { viewModel.removeToBeRead(it) }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreenContent(
    navController: NavHostController,
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    favorites: List<Book>,
    toBeRead: List<Book>,
    isLoading: Boolean,
    onRemoveFavorite: (Book) -> Unit,
    onRemoveToBeRead: (Book) -> Unit
) {
    var tabIndex by remember { mutableIntStateOf(selectedTab) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Matchbook",
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF8C1EFF),
                            Color(0xFFFF6F4E)
                        )
                    )
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding) // ensures TabRow is pushed down
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF080018), Color(0xFF2B004C))
                    )
                )
        ) {

            // --- TABS ---
            TabRow(
                selectedTabIndex = tabIndex,
                containerColor = Color.Transparent,
                contentColor = Color.White,
                indicator = { positions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(positions[tabIndex]),
                        color = Color.White
                    )
                }
            ) {
                Tab(
                    selected = tabIndex == 0,
                    onClick = {
                        tabIndex = 0
                        onTabSelected(0)
                    },
                    text = { Text("Favourites") },
                    icon = { Icon(Icons.Outlined.FavoriteBorder, null) }
                )

                Tab(
                    selected = tabIndex == 1,
                    onClick = {
                        tabIndex = 1
                        onTabSelected(1)
                    },
                    text = { Text("Want to Read") },
                    icon = { Icon(Icons.Outlined.Bookmark, null) }
                )
            }


            Spacer(Modifier.height(12.dp))

            if (isLoading) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else {
                val books = if (tabIndex == 0) favorites else toBeRead

                LazyColumn(
                    Modifier.padding(horizontal = 16.dp,
                    )) {
                    items(books) { book ->
                        LibraryBookCard(
                            book = book,
                            isFavorite = tabIndex == 0,
                            isToRead = tabIndex == 1,
                            onRemoveFavorite = { onRemoveFavorite(book) },
                            onRemoveToBeRead = { onRemoveToBeRead(book) },
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun LibraryBookCard(
    book: Book,
    isFavorite: Boolean,
    isToRead: Boolean,
    onRemoveFavorite: () -> Unit,
    onRemoveToBeRead: () -> Unit,
    navController: NavHostController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                navController.navigate(Screen.BookDetails.createRoute(book.title, "library"))
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {

        Row(
            modifier = Modifier
                .padding(start = 12.dp, end = 12.dp)   // match BookCard
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ----- Book Cover (real or default) -----
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

            // ----- Text Section -----
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    book.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    "by ${book.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    book.reason,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // ----- Favorite Button -----
            if (isFavorite) {
                IconButton(onClick = onRemoveFavorite) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = "Remove Favorite",
                        tint = Color(0xFFE5398D),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            // ----- To Read Button -----
            if (isToRead) {
                IconButton(onClick = onRemoveToBeRead) {
                    Icon(
                        imageVector = Icons.Filled.Bookmark,
                        contentDescription = "Remove To Read",
                        tint = Color(0xFF0A7AFF),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LibraryScreenPreview() {
    val navController = rememberNavController()

    val sampleBooks = listOf(
        Book(
            title = "Lonesome Dove",
            author = "Larry McMurty",
            reason = "A love story, an adventure, and an epic of the frontier, " +
                    "Larry McMurtry’s Pulitzer Prize-winning classic, Lonesome Dove, " +
                    "the third book in the Lonesome Dove tetralogy, is the grandest " +
                    "novel ever written about the last defiant wilderness of America.\n" +
                    "\n" +
                    "Journey to the dusty little Texas town of Lonesome Dove and meet an unforgettable " +
                    "assortment of heroes and outlaws, whores and ladies, Indians and settlers. " +
                    "Richly authentic, beautifully written, always dramatic, " +
                    "Lonesome Dove is a book to make us laugh, weep, dream, and remember."
        ),
        Book(
            title = "Project Hail Mary",
            author = "Andy Weir",
            reason = "Ryland Grace is the sole survivor on a desperate, last-chance mission—and if he fails, " +
                    "humanity and Earth itself will perish.\n" +
                    "\n" +
                    "Except that right now, he doesn’t know that. He can’t even remember his own name, " +
                    "let alone the nature of his assignment or how to complete it.\n" +
                    "\n" +
                    "All he knows is that he’s been asleep for a very, very long time. And he’s just been " +
                    "awakened to find himself millions of miles from home, with nothing but two corpses for " +
                    "company.\n" +
                    "\n" +
                    "His crewmates dead, his memories fuzzily returning, Ryland realizes that an impossible " +
                    "task now confronts him. Hurtling through space on this tiny ship, it’s up to him " +
                    "to puzzle out an impossible scientific mystery—and conquer an extinction-level threat " +
                    "to our species.\n" +
                    "\n" +
                    "And with the clock ticking down and the nearest human being light-years away, " +
                    "he’s got to do it all alone.\n" +
                    "\n" +
                    "Or does he?"
        )
    )

    LibraryScreenContent(
        navController = navController,
        selectedTab = 0,
        onTabSelected = {},
        favorites = sampleBooks,
        toBeRead = sampleBooks,
        isLoading = false,
        onRemoveFavorite = {},
        onRemoveToBeRead = {}
    )
}
