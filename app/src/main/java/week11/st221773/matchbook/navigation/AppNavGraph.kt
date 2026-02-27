package week11.st221773.matchbook.navigation

import HomeViewModel
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import week11.st221773.matchbook.ui.screens.AccountScreen
import week11.st221773.matchbook.ui.screens.BookDetailsScreen
import week11.st221773.matchbook.ui.screens.ForgotPasswordScreen
import week11.st221773.matchbook.ui.screens.HomeScreen
import week11.st221773.matchbook.ui.screens.LibraryScreen
import week11.st221773.matchbook.ui.screens.LoginScreen
import week11.st221773.matchbook.ui.screens.SignUpScreen
import week11.st221773.matchbook.ui.screens.SplashScreen
import week11.st221773.matchbook.viewmodel.LibraryViewModel
import week11.st221773.matchbook.viewmodel.MainViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel,
    libraryViewModel: LibraryViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        // AUTH SCREENS
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(
                navController = navController,
                viewModel = mainViewModel
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(
                navController = navController,
                viewModel = mainViewModel
            )
        }

        composable(Screen.ForgotPassword.route) {
            ForgotPasswordScreen(navController)
        }


        // BOTTOM NAV SCREENS
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                vm = homeViewModel,
                lvm = libraryViewModel
            )
        }

        composable(Screen.MyLibrary.route) {
            LibraryScreen(
                navController = navController,
                viewModel = libraryViewModel
            )
        }

        composable(Screen.Account.route) {
            AccountScreen(navController)
        }

        // BOOK DETAILS SCREEN
        composable(
            route = Screen.BookDetails.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("source") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val source = backStackEntry.arguments?.getString("source")!!  // "home" or "library"

            BookDetailsScreen(
                navController = navController,
                title = title,
                source = source,
                homeViewModel = homeViewModel,
                libraryViewModel = libraryViewModel
            )
        }
    }
}
