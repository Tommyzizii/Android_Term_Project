package com.example.pennytrack.view

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pennytrack.ui.theme.md_theme_light_onSurfaceVariant
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_surfaceVariant
import com.example.pennytrack.viewmodels.ExpenseViewModel

@Composable
fun MonthlyExpenseSummaryScreen(
    navController: NavController,
    viewModel: ExpenseViewModel,
    modifier: Modifier = Modifier
) {
    val monthlyTotals by viewModel.getMonthlyTotals().observeAsState(emptyList())

    LazyColumn(modifier = modifier) {
        items(monthlyTotals) { monthlyTotal ->
            val displayName = viewModel.formatMonthYear(monthlyTotal.monthYear)

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .clickable {
                        viewModel.setMonth(monthlyTotal.monthYear)
                        navController.navigate("month_detail/${Uri.encode(monthlyTotal.monthYear)}")
                    },
                colors = CardDefaults.cardColors(
                    containerColor = md_theme_light_surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = md_theme_light_primary
                    )
                    Text(
                        text = "$${String.format("%.2f", monthlyTotal.total)}",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = md_theme_light_onSurfaceVariant
                    )
                }
            }
        }
    }
}