package no.hvl.quizapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.runtime.setValue
import androidx.compose.foundation.lazy.items
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import androidx.activity.viewModels
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag

/**
 * Activity for managing the image gallery.
 *
 * Responsibilities:
 * - Display all images and their labels
 * - Allow users to add new images from the device
 * - Let users assign labels to images
 * - Remove images from the gallery
 * - Sort images alphabetically
 */
class GalleryActivity : AppCompatActivity() {
    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val manager = (application as App).imageManager

        setContent {
            GalleryScreen(manager)
        }
    }

    /**
     * Main gallery screen.
     *
     * Shows:
     * - Add image button
     * - Sort button
     * - List of images with labels
     */
    @Composable
    fun GalleryScreen(
        manager: ImageManager,
        testMode: Boolean = true
    ) {
        var showDialog by remember { mutableStateOf(false) }
        var pendingUri by remember { mutableStateOf<Uri?>(null) }
        var labelText by remember { mutableStateOf("") }

        val context = LocalContext.current

        val imagePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            val finalUri = if (testMode) {
                Uri.parse("content://test/image")
            } else {
                uri
            }

            finalUri?.let {
                if (!testMode) {
                    context.contentResolver.takePersistableUriPermission(
                        it,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
                pendingUri = it
                labelText = ""
                showDialog = true
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text("Add Label") },
                    text = {
                        OutlinedTextField(
                            value = labelText,
                            onValueChange = { labelText = it },
                            label = { Text("Label") },
                            modifier = Modifier.testTag("label_field")
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                pendingUri?.let {
                                    //manager.addImage(it.toString(), labelText)
                                    //images = manager.getImages()
                                    viewModel.addImage(it.toString(), labelText)
                                }
                                showDialog = false
                            }, modifier = Modifier.testTag("save_label_button")
                        ) {
                            Text("Save")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }

            Button(
                onClick = {
                    if (testMode) {
                        pendingUri = Uri.parse("content://test/image")
                        labelText = ""
                        showDialog = true
                    } else {
                        imagePicker.launch(arrayOf("image/*"))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("add_image_button")
            ) {
                Text("Add Image")
            }

            Button(
                onClick = {
                    if (viewModel.sortAscending){
                        viewModel.sortImages()
                        !viewModel.sortAscending
                    }
                    else if (!viewModel.sortAscending) {
                        //images = images.sortedByDescending { it.label }
                        viewModel.sortImagesDesc()
                        //sort = true
                        !viewModel.sortAscending
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sort")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(viewModel.images) { image ->
                    ImageRow(
                        image = image,
                        onDelete = {
                            //manager.removeImage(image)
                            //images = manager.getImages()
                            viewModel.deleteImage(image)
                        }
                    )
                }
            }
        }
    }

    /**
     * Row representing a single image entry.
     *
     * Shows:
     * - Image preview
     * - Label
     * - Delete button
     */
    @Composable
    fun ImageRow(
        image: ImageEntry,
        onDelete: () -> Unit
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {

            AsyncImage(
                model = image.uri,
                contentDescription = image.label,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = image.label,
                modifier = Modifier.weight(1f)
            )

            Button(onClick = onDelete,
                modifier = Modifier.testTag("delete_image_button")) {
                Text("Delete")
            }
        }
    }
}
