package week11.st221773.matchbook

import HomeViewModel
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import week11.st221773.matchbook.navigation.AppNavGraph
import week11.st221773.matchbook.ui.theme.MatchbookTheme
import week11.st221773.matchbook.viewmodel.LibraryViewModel
import week11.st221773.matchbook.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val homeViewModel: HomeViewModel = viewModel()
            val libraryViewModel: LibraryViewModel = viewModel()

            AppNavGraph(
                navController = navController,
                mainViewModel = MainViewModel(),
                homeViewModel = homeViewModel,
                libraryViewModel = libraryViewModel
            )
        }
    }
}

