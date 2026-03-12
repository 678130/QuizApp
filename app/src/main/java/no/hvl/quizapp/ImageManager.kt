package no.hvl.quizapp

import android.content.Context
import androidx.room.Room
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * Manages images and their labels.
 *
 * Responsibilities:
 * - Store image URIs and labels in memory
 * - Persist metadata to a JSON file
 * - Load metadata when the app starts
 *
 * The metadata is stored in the app’s internal storage
 * as a file named "metadata.json".
 */
class ImageManager(private val context: Context) {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "images.db"
    ).allowMainThreadQueries().build()

    private val dao = db.imageDao()
    private var images = mutableListOf<ImageEntry>()

    init {
        images = getImages() as MutableList<ImageEntry>
    }

    /**
     * Adds a new image with a label.
     *
     * @param uri String representation of the image URI
     * @param label The label assigned to the image
     */
    fun addImage(uri: String, label: String) {
        val item = ImageEntry(uri=uri, label=label)
        images.add(item)
        dao.insert(item)
    }

    /**
     * Returns a copy of the image list.
     * A copy is returned to prevent external modification.
     */
    fun getImages(): List<ImageEntry> {
        return dao.getAll()
    }


    /**
     * Removes an image from the list and updates the metadata file.
     *
     * @param image The image item to remove
     */
    fun removeImage(image: ImageEntry) {
        images.remove(image)
        dao.delete(image)
        //saveMetadata()
    }

}
