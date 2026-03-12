package no.hvl.quizapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ImageEntry::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}