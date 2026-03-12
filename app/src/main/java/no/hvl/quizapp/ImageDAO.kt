package no.hvl.quizapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {

    @Query("SELECT * FROM ImageEntry")
    fun getAll(): List<ImageEntry>

    @Insert
    fun insert(image: ImageEntry)

    @Delete
    fun delete(image: ImageEntry)
}