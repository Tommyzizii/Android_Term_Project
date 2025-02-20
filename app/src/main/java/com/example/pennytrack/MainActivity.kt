package com.example.pennytrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pennytrack.view.AddExpenseScreen
import com.example.pennytrack.view.BankLocations
import com.example.pennytrack.view.ChartScreen
import com.example.pennytrack.view.HomeScreen
import com.example.pennytrack.view.MonthlyExpenseScreen
import com.example.pennytrack.view.ProfileScreen
import com.example.pennytrack.ui.theme.PennyTrackTheme
import com.example.pennytrack.viewmodels.ExpenseViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            PennyTrackTheme {
                MyApp()  // This is where MyApp is called
            }
        }
    }
}

@Composable
fun MyApp() {
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
            MonthlyExpenseScreen(navController)
        }
        composable("profile"){
            ProfileScreen(navController)
        }
        composable("bankLocations"){
            BankLocations(navController)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PennyTrackTheme {
        MyApp()  // Preview of MyApp
    }
}
