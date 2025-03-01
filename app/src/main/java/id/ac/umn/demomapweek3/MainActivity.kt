package id.ac.umn.demomapweek3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.*
import id.ac.umn.demomapweek3.ui.theme.DemoMAPWeek3Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoMAPWeek3Theme {
                MainScreen()
            }
        }
    }
}

// Define Screens for Bottom Navigation
sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Home)
    object Details : Screen("details", "Details", Icons.Filled.Info)
    object Profile : Screen("profile", "Profile", Icons.Filled.Person)
}

// Define Screens for Drawer Navigation
sealed class DrawerScreen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Settings : DrawerScreen("settings", "Settings", Icons.Filled.Settings)
    object Help : DrawerScreen("help", "Help", Icons.Filled.Info)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(navController, drawerState)
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { TopAppBarWithDrawer(drawerState, coroutineScope) },
            bottomBar = { BottomNavigationBar(navController) }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(Screen.Home.route) { HomeScreen() }
                composable(Screen.Details.route) { DetailsScreen() }
                composable(Screen.Profile.route) { ProfileScreen() }
                composable(DrawerScreen.Settings.route) { SettingsScreen() }
                composable(DrawerScreen.Help.route) { HelpScreen() }
            }
        }
    }
}

//Drawer content
@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState) {
    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(WindowInsets.navigationBars.asPaddingValues())
            .padding(16.dp)
    ) {
        Text(
            text = "Navigation Drawer",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(16.dp))

        val drawerItems = listOf(DrawerScreen.Settings, DrawerScreen.Help)
        drawerItems.forEach { screen ->
            TextButton(
                onClick = {
                    coroutineScope.launch { drawerState.close() }
                    navController.navigate(screen.route)
                }
            ) {
                Icon(screen.icon, contentDescription = screen.title, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                Spacer(modifier = Modifier.width(8.dp))
                Text(screen.title, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
    }
}


//Drawer
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithDrawer(drawerState: DrawerState, coroutineScope: CoroutineScope) {
    TopAppBar(
        title = { Text("Demo App") },
        navigationIcon = {
            IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                Icon(Icons.Filled.Menu, contentDescription = "Open Drawer")
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(Screen.Home, Screen.Details, Screen.Profile)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = { navController.navigate(screen.route) },
                icon = { Icon(screen.icon, contentDescription = screen.title, tint = MaterialTheme.colorScheme.onPrimaryContainer) },
                label = { Text(screen.title, color = MaterialTheme.colorScheme.onPrimaryContainer) }
            )
        }
    }
}

//Screens
@Composable
fun HomeScreen() {
    Column {
        Text(text = "Home Screen", modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
fun DetailsScreen() {
    Column {
        Text(text = "Details Screen", modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
fun ProfileScreen() {
    Column {
        Text(text = "Profile Screen", modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
fun SettingsScreen() {
    Column {
        Text(text = "Settings Screen", modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
fun HelpScreen() {
    Column {
        Text(text = "Help Screen", modifier = Modifier.padding(start = 16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    DemoMAPWeek3Theme {
        MainScreen()
    }
}