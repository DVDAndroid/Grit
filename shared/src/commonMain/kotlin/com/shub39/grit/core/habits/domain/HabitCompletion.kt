package com.shub39.grit.core.habits.domain

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = HabitCompletionSerializer::class)
enum class HabitCompletion(val ok: Int) {
    Completed(1),
    OnlyNotes(0);

    companion object {
        fun byValue(x: Int) = entries.single { it.ok == x }
    }
}

object HabitCompletionSerializer : KSerializer<HabitCompletion> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(
            "HabitCompletion",
            PrimitiveKind.INT
        )

    override fun serialize(
        encoder: Encoder,
        value: HabitCompletion
    ) {
        encoder.encodeInt(value.ok)
    }

    override fun deserialize(
        decoder: Decoder
    ): HabitCompletion {
        return HabitCompletion.byValue(
            decoder.decodeInt()
        )
    }
}