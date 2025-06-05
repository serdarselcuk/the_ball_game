package com.allfreeapps.theballgame.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.allfreeapps.theballgame.utils.Constants
import java.io.Serializable
import java.util.Date

@Entity(tableName = Constants.DATABASE_NAME)
data class Score (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "fist_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String?,
    @ColumnInfo(name = "score")
    val score: Int,
    @ColumnInfo(name = "date")
    val date: Date?
):Comparable<Score>, Serializable {
    override fun compareTo(other: Score): Int {
        return score.compareTo(other.score)
    }
}


