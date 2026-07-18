package com.example.notepad.data

import androidx.room.TypeConverter
import com.example.notepad.models.Note
import java.util.Date

/**
 * Type converters for Room database
 */
object Converters {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time
}