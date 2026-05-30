package com.shub39.grit.core.habits.presentation.ui.component.stats

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.shub39.grit.core.habits.domain.HabitWithAnalytics
import com.shub39.grit.core.habits.presentation.ui.component.AnalyticsCard
import com.shub39.grit.core.habits.presentation.ui.component.NotEnoughData
import grit.shared.generated.resources.Res
import grit.shared.generated.resources.expand
import grit.shared.generated.resources.pin
import grit.shared.generated.resources.progress
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NumberValueChangeAnalyticsCard(
    habit: HabitWithAnalytics,
    onNavigateToFullNumericList: () -> Unit
) {
    checkNotNull(habit.numericAnalytics)
    AnalyticsCard(
        title = stringResource(Res.string.progress),
        icon = Res.drawable.pin,
        header = {
            if (habit.numericAnalytics.dailyChanges.isNotEmpty()) {
                Row {
                    FilledTonalIconButton(
                        onClick = onNavigateToFullNumericList,
                        shapes = IconButtonDefaults.shapes(),
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.expand),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }
        },
    ) {
        if (habit.numericAnalytics.dailyChanges.isEmpty()) NotEnoughData()
        else NumberValueChangesList(
            dailyChanges = habit.numericAnalytics.dailyChanges.take(10),
            weeklyChanges = habit.numericAnalytics.weeklyChanges.take(10),
            monthlyChanges = habit.numericAnalytics.monthlyChanges.take(10),
            yearlyChanges = habit.numericAnalytics.yearlyChanges,
            lazyList = false,
        )
    }
}