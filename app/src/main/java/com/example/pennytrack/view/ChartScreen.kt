package com.example.pennytrack.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pennytrack.data.models.ExpenseChartEntry
import com.example.pennytrack.ui.theme.md_theme_light_onPrimary
import com.example.pennytrack.ui.theme.md_theme_light_onSurfaceVariant
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_primaryContainer
import com.example.pennytrack.ui.theme.md_theme_light_surface
import com.example.pennytrack.ui.theme.md_theme_light_surfaceVariant
import com.example.pennytrack.viewmodels.ExpenseViewModel
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen(
    navController: NavController,
    expenseViewModel: ExpenseViewModel
) {
    // Observe today's expenses
    val expenses by expenseViewModel.expenses.collectAsState()

    // Get current date to display
    val currentDate by expenseViewModel.currentDate.collectAsState()

    // Observe total expense for today
    val totalDailyExpense by expenseViewModel.getTotalExpenseForDay(currentDate).observeAsState(0f)

    // Group expenses by type and calculate total amount for each type
    val expensesByType = remember(expenses) {
        expenses.groupBy { it.description }
            .mapValues { (_, expenses) ->
                expenses.sumOf { it.amount.toDouble() }.toFloat()
            }
    }

    //Data Entries
    val chartEntries = remember(expensesByType) {
        expensesByType.entries.mapIndexed { index, (type, amount) ->
            ExpenseChartEntry(index.toFloat(), amount, type)
        }
    }

    //XLabel
    val xAxisFormatter = AxisValueFormatter<AxisPosition.Horizontal.Bottom> { x, _ ->
        chartEntries.getOrNull(x.toInt())?.type ?: ""
    }

    //YLabel
    val yAxisFormatter = AxisValueFormatter<AxisPosition.Vertical.Start> { y, _ ->
        NumberFormat.getCurrencyInstance(Locale.US).format(y)
    }

    val chartEntryModelProducer = remember { ChartEntryModelProducer() }
    chartEntryModelProducer.setEntries(chartEntries)

    val chartColors = remember {
        listOf(
            md_theme_light_primary.toArgb(),
            md_theme_light_primaryContainer.toArgb(),
            Color(0xFF5B9A5C).toArgb()  // A midtone green that fits with your palette
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Expense Chart",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = md_theme_light_onPrimary
                        )

                        Text(
                            currentDate, // Show formatted current date
                            fontSize = 14.sp,
                            color = md_theme_light_onPrimary.copy(alpha = 0.8f)
                        )
                    }

                },
                actions = {
                    // Add refresh button to ensure today's data is shown
                    IconButton(onClick = { expenseViewModel.refreshTodayExpenses() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Today's Data",
                            tint = md_theme_light_onPrimary
                        )
                    }
                },
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

                IconButton(
                    onClick = { navController.navigate("chart") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ShowChart,
                        contentDescription = "Chart",
                    )
                }

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

                IconButton(
                    onClick = { navController.navigate("history") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.DateRange, contentDescription = "History")
                }

                IconButton(
                    onClick = { navController.navigate("profile") },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                }
            }
        },
        containerColor = md_theme_light_surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Total Expenses Card - Now using the LiveData from ViewModel
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = md_theme_light_surfaceVariant
                )
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Today's Expenses",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = md_theme_light_onSurfaceVariant.copy(alpha = 0.7f),
                        modifier =  Modifier.padding(end = 32.dp)
                    )

                    Text(
                        text = "$${String.format("%.2f", totalDailyExpense)}",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall,
                        color = md_theme_light_primary,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
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
                if (expenses.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No expenses recorded today",
                            fontSize = 16.sp,
                            color = md_theme_light_onSurfaceVariant
                        )
                    }
                } else {
                    Column (
                        modifier = Modifier.fillMaxSize().padding(16.dp)
                    ){
                        Text(
                            text = "Expense Breakdown",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = md_theme_light_onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Chart(
                            chart = lineChart(),
                            model = chartEntryModelProducer.getModel(),
                            modifier = Modifier.fillMaxSize().padding(16.dp),
                            startAxis = rememberStartAxis(
                                valueFormatter = yAxisFormatter,
                                label = textComponent(
                                    color = md_theme_light_onSurfaceVariant,
                                    textSize = 12.sp
                                )
                            ),
                            bottomAxis = rememberBottomAxis(
                                valueFormatter = xAxisFormatter,
                                label = textComponent(
                                    color = md_theme_light_onSurfaceVariant,
                                    textSize = 12.sp
                                )
                            )
                        )
                    }
                }
            }

            // Expense Type Legend
            if (expenses.isNotEmpty()) {
                LazyColumn {
                    items(expensesByType.entries.toList()) { (type, amount) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier.padding(start = 8.dp, end = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Circle color indicator
                                androidx.compose.foundation.Canvas(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .padding(end = 4.dp)
                                ) {
                                    // Use primary or primaryContainer color based on index
                                    val colorIndex = expensesByType.entries.toList()
                                        .indexOf(expensesByType.entries.find { it.key == type })
                                    val dotColor = when (colorIndex % 3) {
                                        0 -> md_theme_light_primary
                                        1 -> md_theme_light_primaryContainer
                                        else -> Color(0xFF5B9A5C)
                                    }
                                    drawCircle(color = dotColor)
                                }
                                Text(
                                    text = type,
                                    color = md_theme_light_onSurfaceVariant
                                )
                            }
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
}