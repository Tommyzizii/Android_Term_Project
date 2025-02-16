package com.example.pennytrack.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pennytrack.ui.theme.md_theme_light_onPrimary
import com.example.pennytrack.ui.theme.md_theme_light_onSurfaceVariant
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_surface
import com.example.pennytrack.ui.theme.md_theme_light_surfaceVariant
import com.example.pennytrack.viewmodels.ExpenseViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
//import com.patrykandpatrick.vico.compose.chart.line.LinChart
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryModelOf
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel = viewModel()
){

    val expenses by expenseViewModel.expenses.collectAsState()

    // Group expenses by type and calculate total amount for each type
    val expensesByType = remember(expenses) {
        expenses.groupBy { it.description }
            .mapValues { (_, expenses) ->
                expenses.sumOf { it.amount.toDouble() }.toFloat()
            }
    }

    val chartEntries = remember(expensesByType) {
        expensesByType.entries.mapIndexed { index, (type, amount) ->
            ExpenseChartEntry(index.toFloat(), amount, type)
        }
    }

    val xAxisFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ ->
        chartEntries.getOrNull(x.toInt())?.type ?: ""
    }

    val yAxisFormatter = AxisValueFormatter<AxisPosition.Vertical.Start> { y, _ ->
        NumberFormat.getCurrencyInstance(Locale.US).format(y)
    }

    val chartEntryModelProducer = remember { ChartEntryModelProducer() }
    chartEntryModelProducer.setEntries(chartEntries)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("History",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = md_theme_light_onPrimary) },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = md_theme_light_primary,
                    scrolledContainerColor = md_theme_light_onPrimary
                )
            )
        },

        bottomBar = {
            BottomAppBar(
                modifier = Modifier,
                containerColor = md_theme_light_surface,
                contentColor = md_theme_light_primary

            ) {
                IconButton(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.Home, contentDescription = "Home")
                }

                // History Icon
                IconButton(
                    onClick = { navController.navigate("history") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = "History")
                }

                // Add Expense (Floating Action Button)
                FloatingActionButton(
                    onClick = { navController.navigate("addExpense") },
                    modifier = Modifier
                        .size(56.dp)
                        .align(Alignment.CenterVertically)
                        .padding(0.dp),
                    containerColor = md_theme_light_primary,
                    contentColor = md_theme_light_onPrimary
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Expense")
                }

                // Map Icon
                IconButton(
                    onClick = { navController.navigate("map") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.LocationOn, contentDescription = "Map")
                }

                // Profile Icon
                IconButton(
                    onClick = { navController.navigate("profile") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                }
            }
        },
        containerColor = md_theme_light_surface
    ){innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Total Expenses Card
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = md_theme_light_surfaceVariant
                )
            ) {
                Text(
                    text = "Total Expenses: $${expenses.sumOf { it.amount.toDouble() }}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                )
            }

            // Expense Chart
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = md_theme_light_surfaceVariant
                )
            ) {
                Chart(
                    chart = lineChart(),
                    model = chartEntryModelProducer.getModel(),
                    modifier = Modifier.fillMaxSize().padding(16.dp) ,
                    startAxis = rememberStartAxis(valueFormatter = yAxisFormatter),
                    bottomAxis = rememberBottomAxis(valueFormatter = xAxisFormatter)
                )
            }

            // Expense Type Legend
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                items(expensesByType.entries.toList()) { (type, amount) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = type,
                            color = md_theme_light_onSurfaceVariant
                        )
                        Text(
                            text = "$${String.format("%.2f", amount)}",
                            color = md_theme_light_primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}