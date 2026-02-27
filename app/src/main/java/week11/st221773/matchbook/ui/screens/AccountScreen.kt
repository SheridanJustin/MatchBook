package week11.st221773.matchbook.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import week11.st221773.matchbook.navigation.BottomNavigationBar
import week11.st221773.matchbook.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavHostController) {

    val auth = FirebaseAuth.getInstance()
    val email = auth.currentUser?.email ?: "Not logged in"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
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

        Box(modifier = Modifier.padding(padding)) {
            AccountContent(
                email = email,
                onLogout = {
                    auth.signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountContent(
    modifier: Modifier = Modifier,
    email: String,
    onLogout: () -> Unit,
    navController: NavHostController? = null
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF080018), Color(0xFF2B004C))
                )
            )
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(40.dp))

        Text(email, color = Color.White, style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(80.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF8C1EFF), Color(0xFFFF6F4E))
                    ),
                    shape = RoundedCornerShape(30.dp)
                )
                .clickable { onLogout() },
            contentAlignment = Alignment.Center
        ) {
            Text("Logout", color = Color.White)
        }
    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AccountPreview() {
    AccountContent(
        email = "user@example.com",
        onLogout = {}
    )
}

