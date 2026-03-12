package no.hvl.quizapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uri: String,
    val label: String
)

