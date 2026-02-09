package no.hvl.quizapp

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class ImageManager(private val context: Context) {

    private val metadataFile: File =
        File(context.filesDir, "metadata.json")

    private val images = mutableListOf<ImageItem>()

    init {
        loadMetadata()
    }

    fun addImage(uri: String, label: String) {
        val item = ImageItem(uri, label)
        images.add(item)
        saveMetadata()
    }

    fun getImages(): List<ImageItem> {
        return images.toList()
    }

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

    fun removeImage(image: ImageItem) {
        images.remove(image)
        saveMetadata()
    }

}
