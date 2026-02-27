package week11.st221773.matchbook.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home",
        label = "Home",
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )

    object MyLibrary : BottomNavItem(
        route = "library_screen",
        label = "My Library",
        icon = Icons.Outlined.Book,
        selectedIcon = Icons.Filled.Book
    )

    object Account : BottomNavItem(
        route = "account",
        label = "Account",
        icon = Icons.Outlined.Person,
        selectedIcon = Icons.Filled.Person
    )

    companion object {
        val navItems = listOf(
            Home,
            MyLibrary,
            Account
        )
    }
}
