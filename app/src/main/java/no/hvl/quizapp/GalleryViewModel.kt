package no.hvl.quizapp
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class GalleryViewModel (application: Application) : AndroidViewModel(application) {

    private val repo = ImageManager(application)
    private var _images by mutableStateOf(repo.getImages())
    val images: List<ImageEntry>
        get() = _images
    var sortAscending by mutableStateOf(true)
        private set

    fun addImage(uriString: String, label:String) {
        repo.addImage(uriString, label)
        refresh()
    }

    fun deleteImage(entry: ImageEntry) {
        repo.removeImage(entry)
        refresh()
    }

    private fun refresh() {
        _images = repo.getImages()
    }

    fun toggleSort() {
        sortAscending = !sortAscending
    }

    fun sortImages() {
        _images = _images.sortedBy { it.label }
    }

    fun sortImagesDesc(){
        _images = _images.sortedByDescending { it.label }
    }
}