package com.allfreeapps.theballgame.ui.model.entities

import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Score (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "fist_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "score")
    val score: Int,
    @ColumnInfo(name = "date")
    val date: Date?
)

