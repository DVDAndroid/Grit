/*
 * Copyright (C) 2026  Shubham Gorai
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.shub39.grit.core.habits.presentation.ui.component.stats

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.shub39.grit.core.habits.domain.HabitNumberChange
import com.shub39.grit.core.habits.domain.NumericListType
import com.shub39.grit.core.habits.domain.NumericListType.Companion.toStringRes
import com.shub39.grit.core.theme.flexFontRounded
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import java.util.Locale

@Composable
fun NumberValueChangesList(
    dailyChanges: List<HabitNumberChange>,
    weeklyChanges: List<HabitNumberChange>,
    monthlyChanges: List<HabitNumberChange>,
    yearlyChanges: List<HabitNumberChange>,
    lazyList: Boolean,
) {
    var listType by rememberSaveable { mutableStateOf(NumericListType.ByDay) }
    Row(
        modifier = Modifier.padding(all = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween),
    ) {
        NumericListType.entries.forEach { entry ->
            ToggleButton(
                checked = entry == listType,
                onCheckedChange = { listType = entry },
                modifier = Modifier.weight(1f),
            ) {
                Text(text = stringResource(entry.toStringRes()))
            }
        }
    }

    AnimatedContent(targetState = listType, modifier = Modifier.fillMaxSize()) { currentType ->
        val changes = when (currentType) {
            NumericListType.ByDay -> dailyChanges
            NumericListType.ByWeek -> weeklyChanges
            NumericListType.ByMonth -> monthlyChanges
            NumericListType.ByYear -> yearlyChanges
        }

        if (lazyList) {
            LazyColumn {
                items(changes) {
                    ChangeProgressItem(
                        change = it,
                        modifier = Modifier
                            .padding(horizontal = 8.dp),
                    )
                }
            }
        } else {
            Column {
                for (ch in changes) {
                    ChangeProgressItem(ch)
                }
            }
        }
    }
}

@Composable
private fun ChangeProgressItem(
    change: HabitNumberChange,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth(),
    ) {
        Text(
            text = change.period.toComposeString(),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = String.format(Locale.ENGLISH, "%.2f", change.value)
            ,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.secondary,
                    fontFamily = flexFontRounded(),
                ),
            )
            Icon(
                imageVector = vectorResource(change.change.icon()),
                contentDescription = null,
            )
        }
    }

}