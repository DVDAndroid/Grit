package com.shub39.grit.core.habits.domain

import grit.shared.generated.resources.Res
import grit.shared.generated.resources.daily
import grit.shared.generated.resources.monthly
import grit.shared.generated.resources.weekly
import grit.shared.generated.resources.yearly
import org.jetbrains.compose.resources.StringResource

enum class NumericListType {
    ByDay,
    ByWeek,
    ByMonth,
    ByYear;

    companion object {
        fun NumericListType.toStringRes(): StringResource = when (this) {
            ByDay -> Res.string.daily
            ByWeek -> Res.string.weekly
            ByMonth -> Res.string.monthly
            ByYear -> Res.string.yearly
        }
    }
}