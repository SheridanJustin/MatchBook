package week11.st221773.matchbook.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object SignUp : Screen("signup")
    object ForgotPassword : Screen("forgot_password")

    object Home : Screen("home")
    object MyLibrary : Screen("library_screen")   // MUST MATCH BottomNavItem
    object Account : Screen("account")            // MUST MATCH BottomNavItem

    object BookDetails : Screen("book_details/{title}/{source}") {

        fun createRoute(title: String, source: String): String {
            return "book_details/$title/$source"
        }
    }
}

