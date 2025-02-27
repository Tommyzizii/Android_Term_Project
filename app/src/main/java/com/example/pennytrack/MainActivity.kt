package com.example.pennytrack

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.AppComponentFactory
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pennytrack.data.models.getDarkMode
import com.example.pennytrack.data.models.saveDarkMode
import com.example.pennytrack.view.AddExpenseScreen
import com.example.pennytrack.view.BankLocations
import com.example.pennytrack.view.ChartScreen
import com.example.pennytrack.view.HomeScreen
import com.example.pennytrack.view.MonthlyExpenseScreen
import com.example.pennytrack.view.ProfileScreen
import com.example.pennytrack.ui.theme.PennyTrackTheme
import com.example.pennytrack.view.EditProfileScreen
import com.example.pennytrack.view.MonthlyDetailScreen
import com.example.pennytrack.viewmodels.ExpenseViewModel
import kotlinx.coroutines.launch

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            var isDarkTheme by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                getDarkMode(context).collect { savedDarkMode ->
                    isDarkTheme = savedDarkMode
                }
            }

            PennyTrackTheme(darkTheme = isDarkTheme) {
                MyApp(
                    currentDarkMode = isDarkTheme,
                    onThemeChange = {
                    newDarkMode ->
                    isDarkTheme = newDarkMode

                    scope.launch {
                        saveDarkMode(context, newDarkMode)
                    }
                })  // This is where MyApp is called
            }
        }
    }
}

@Composable
fun MyApp(currentDarkMode: Boolean, onThemeChange: (Boolean) -> Unit) {
    val navController = rememberNavController()  // Creating a navigation controller
    val expenseViewModel: ExpenseViewModel = viewModel()  // Initializing ViewModel

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, expenseViewModel)  // Pass ViewModel to HomeScreen
        }
        composable("chart"){
            ChartScreen(navController, expenseViewModel)
        }
        composable("addExpense") {
            AddExpenseScreen(navController, expenseViewModel)  // Pass ViewModel to AddExpenseScreen
        }
        composable("history"){
            MonthlyExpenseScreen(navController, expenseViewModel)
        }
        composable(
            route = "month_detail/{monthYear}",
            arguments = listOf(navArgument("monthYear") { type = NavType.StringType })
        ) { backStackEntry ->
            val encodedMonthYear = backStackEntry.arguments?.getString("monthYear") ?: ""
            val monthYear = Uri.decode(encodedMonthYear)
            MonthlyDetailScreen(navController, expenseViewModel, monthYear)
        }
        composable("profile"){
            ProfileScreen(navController,currentDarkMode ,onThemeChange)
        }
        composable("editProfile") {
            EditProfileScreen(navController = navController)
        }
        composable("bankLocations"){
            BankLocations(navController)
        }
    }
}