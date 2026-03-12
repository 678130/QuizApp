package no.hvl.quizapp
import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import androidx.room.Room

class ImageContentProvider : ContentProvider() {

    private lateinit var db: AppDatabase

    override fun onCreate(): Boolean {
        db = Room.databaseBuilder(
            context!!,
            AppDatabase::class.java,
            "images.db"
        ).allowMainThreadQueries()
            .build()

        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {

        val cursor = MatrixCursor(arrayOf("name", "URI"))

        val images = db.imageDao().getAll()

        for (img in images) {
            cursor.addRow(arrayOf(img.label, img.uri))
        }

        return cursor
    }

    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int = 0
}