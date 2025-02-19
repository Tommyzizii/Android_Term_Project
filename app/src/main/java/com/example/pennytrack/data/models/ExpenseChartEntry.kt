package com.example.pennytrack.data.models

import com.patrykandpatrick.vico.core.entry.ChartEntry

data class ExpenseChartEntry(
    override val x: Float,
    override val y: Float,
    val type: String
) : ChartEntry {
    override fun withY(y: Float) = ExpenseChartEntry(x, y, type)
}