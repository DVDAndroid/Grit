package com.shub39.grit.core.habits.presentation.ui.sections

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.shub39.grit.core.LocalWindowSizeClass
import com.shub39.grit.core.habits.presentation.HabitState
import com.shub39.grit.core.habits.presentation.ui.component.stats.NumberValueChangesList
import com.shub39.grit.core.theme.flexFontEmphasis
import grit.shared.generated.resources.Res
import grit.shared.generated.resources.nav_arrow_back
import grit.shared.generated.resources.progress
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun NumberValuesFullList(
    state: HabitState,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentHabit =
        state.habitsWithAnalytics.find { it.habit.id == state.analyticsHabitId } ?: return
    checkNotNull(currentHabit.numericAnalytics)
    val windowSizeClass = LocalWindowSizeClass.current

    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = stringResource(Res.string.progress), fontFamily = flexFontEmphasis())
            },
            navigationIcon = {
                FilledTonalIconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = vectorResource(Res.drawable.nav_arrow_back),
                        contentDescription = "Navigate Back",
                    )
                }
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    scrolledContainerColor = Color.Transparent,
                    containerColor = Color.Transparent,
                ),
            windowInsets =
                if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
                    WindowInsets(0)
                } else {
                    TopAppBarDefaults.windowInsets
                },
        )

        NumberValueChangesList(
            dailyChanges = currentHabit.numericAnalytics.dailyChanges,
            weeklyChanges = currentHabit.numericAnalytics.weeklyChanges,
            monthlyChanges = currentHabit.numericAnalytics.monthlyChanges,
            yearlyChanges = currentHabit.numericAnalytics.yearlyChanges,
            lazyList = true,
        )
    }
}
