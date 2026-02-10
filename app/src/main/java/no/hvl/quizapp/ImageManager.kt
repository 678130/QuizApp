package no.hvl.quizapp

import android.content.Context
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

    private val metadataFile: File =
        File(context.filesDir, "metadata.json")

    private val images = mutableListOf<ImageItem>()

    init {
        loadMetadata()
    }

    /**
     * Adds a new image with a label.
     *
     * @param uri String representation of the image URI
     * @param label The label assigned to the image
     */
    fun addImage(uri: String, label: String) {
        val item = ImageItem(uri, label)
        images.add(item)
        saveMetadata()
    }

    /**
     * Returns a copy of the image list.
     * A copy is returned to prevent external modification.
     */
    fun getImages(): List<ImageItem> {
        return images.toList()
    }

    /**
     * Saves the current image metadata to the JSON file.
     *
     * Structure:
     * [
     *   { "uri": "...", "label": "cat" },
     *   { "uri": "...", "label": "dog" }
     * ]
     */
    private fun saveMetadata() {
        val jsonArray = JSONArray()

        for (image in images) {
            val obj = JSONObject()
            obj.put("uri", image.uri)
            obj.put("label", image.label)
            jsonArray.put(obj)
        }

        metadataFile.writeText(jsonArray.toString())
    }

    /**
     * Loads metadata from the JSON file into memory.
     * If the file does not exist, nothing happens.
     */
    private fun loadMetadata() {
        if (!metadataFile.exists()) return

        val text = metadataFile.readText()
        val jsonArray = JSONArray(text)

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            val item = ImageItem(
                obj.getString("uri"),
                obj.getString("label")
            )
            images.add(item)
        }
    }

    /**
     * Removes an image from the list and updates the metadata file.
     *
     * @param image The image item to remove
     */
    fun removeImage(image: ImageItem) {
        images.remove(image)
        saveMetadata()
    }

}
