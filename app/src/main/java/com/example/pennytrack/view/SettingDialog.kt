package com.example.pennytrack.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.pennytrack.ui.theme.md_theme_light_onPrimary
import com.example.pennytrack.ui.theme.md_theme_light_onSurface
import com.example.pennytrack.ui.theme.md_theme_light_onSurfaceVariant
import com.example.pennytrack.ui.theme.md_theme_light_primary
import com.example.pennytrack.ui.theme.md_theme_light_surface
import com.example.pennytrack.ui.theme.md_theme_light_surfaceVariant

@Composable
fun SettingsDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onLanguageChange: (String) -> Unit,
    onThemeChange: (Boolean) -> Unit
) {
    var selectedLanguage by remember { mutableStateOf("English") }
    var isDarkMode by remember { mutableStateOf(false) }


    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(dismissOnClickOutside = true)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 8.dp,
                color = md_theme_light_surface
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.titleLarge,
                        color = md_theme_light_onSurface
                    )

                    Divider(color = md_theme_light_surfaceVariant)

                    Text(
                        text = "Select Language:",
                        style = MaterialTheme.typography.bodyLarge,
                        color = md_theme_light_onSurfaceVariant
                    )
                    Column {
                        listOf("English", "Myanmar").forEach { language ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = language,
                                    color = md_theme_light_onSurface
                                )
                                RadioButton(
                                    selected = (selectedLanguage == language),
                                    onClick = {
                                        selectedLanguage = language
                                        onLanguageChange(language)
                                    },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = md_theme_light_primary,
                                        unselectedColor = md_theme_light_onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }

                    Divider(color = md_theme_light_surfaceVariant)

                    Text(
                        text = "Theme Mode:",
                        style = MaterialTheme.typography.bodyLarge,
                        color = md_theme_light_onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isDarkMode) "Dark Mode 🌙" else "Light Mode ☀️",
                            color = md_theme_light_onSurface
                        )
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = {
                                isDarkMode = it
                                onThemeChange(it)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = md_theme_light_onPrimary,
                                checkedTrackColor = md_theme_light_primary,
                                uncheckedThumbColor = md_theme_light_onSurfaceVariant,
                                uncheckedTrackColor = md_theme_light_surfaceVariant
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = md_theme_light_primary,
                            contentColor = md_theme_light_onPrimary
                        )
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }
}