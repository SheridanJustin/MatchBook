package week11.st221773.matchbook.ui.screens

import HomeViewModel
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import week11.st221773.matchbook.R
import week11.st221773.matchbook.model.Book
import week11.st221773.matchbook.navigation.BottomNavigationBar
import week11.st221773.matchbook.viewmodel.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    navController: NavHostController,
    title: String,
    homeViewModel: HomeViewModel,
    libraryViewModel: LibraryViewModel,
    source: String
) {
    Log.d("BookDetailsScreen", "Title = $title")
    val book =
        homeViewModel.recommendations.find { it.title == title }
            ?: libraryViewModel.favorites.find { it.title == title }
            ?: libraryViewModel.toBeRead.find { it.title == title }

    if (book == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Book not found", color = Color.White)
        }
        return
    }

    // state variables to update automatically
    val isFavorite by derivedStateOf { libraryViewModel.isFavorite(book.title) }
    val isToRead by derivedStateOf { libraryViewModel.isToRead(book.title) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Matchbook", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
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
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->
        BookDetailsContent(
            modifier = Modifier.padding(padding)
                //.verticalScroll(rememberScrollState())
            ,
            book = book,
            isFavorite = isFavorite,
            isToRead = isToRead,
            onToggleFavorite = {
                if (isFavorite) libraryViewModel.removeFavorite(book)
                else libraryViewModel.addToFavorites(book)
            },

            onToggleToRead = {
                if (isToRead) libraryViewModel.removeToBeRead(book)
                else libraryViewModel.addToToBeRead(book)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsContent(
    book: Book,
    isFavorite: Boolean,
    isToRead: Boolean,
    onToggleFavorite: () -> Unit,
    onToggleToRead: () -> Unit,
    modifier: Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF080018), Color(0xFF2B004C))
                )
            )
            .padding(horizontal = 24.dp, vertical = 12.dp)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(50.dp))

        // Cover
        AsyncImage(
            model = book.coverUrl ?: R.drawable.default_cover,
            contentDescription = "Book Cover",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(240.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        Spacer(Modifier.height(24.dp))

        Text(
            book.title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Text(
            "by ${book.author}",
            style = MaterialTheme.typography.titleMedium,
            color = Color(0xFFDCDCDC)
        )

        // --- GENRE / CATEGORY ---
        if (!book.categories.isNullOrEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = book.categories.joinToString(", "),
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF57C5FF),   // diff colour for genre
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(20.dp))

        Text(
            book.description ?: book.reason,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = onToggleFavorite,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFavorite) Color(0xFF803344) else Color(0xFFE5398D)
            )
        ) {
            Text(if (isFavorite) "Remove from Favourites" else "Add to Favourites")
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onToggleToRead,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isToRead) Color(0xFF2A4F60) else Color(0xFF57C5FF)
            )
        ) {
            Text(if (isToRead) "Remove from Want to Read" else "Add to Want to Read")
        }
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookDetailsContentPreview() {

    val sampleBook = Book(
        title = "Lonesome Dove",
        author = "Larry McMurty",
        reason = "A love story, an adventure, and an epic of the frontier."
    )

    BookDetailsContent(
        book = sampleBook,
        isFavorite = true,
        isToRead = false,
        onToggleFavorite = {},
        onToggleToRead = {},
        modifier = Modifier
    )
}

