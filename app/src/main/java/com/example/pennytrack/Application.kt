package com.example.pennytrack

import android.app.Application
import com.example.pennytrack.dao.ExpenseDatabase


class PennyTrackApplication : Application() {
    val database by lazy { ExpenseDatabase.getDatabase(this) }
}