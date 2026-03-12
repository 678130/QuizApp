Tested the imageprovider by running the follwing command:
adb shell content query --uri content://no.hvl.quizapp.provider

Response:
Row: 0 name=Frog, URI=content://com.android.providers.media.documents/document/image%3A22
Row: 1 name=Chicken, URI=content://com.android.providers.media.documents/document/image%3A19
Row: 2 name=Cat, URI=content://com.android.providers.media.documents/document/image%3A20
Row: 3 name=Dog, URI=content://com.android.providers.media.documents/document/image%3A21


Test navigation from main to gallery -> openGallery():
The test sets the activity to main activity.
It should press the button with id gallery_button
Then if the activity it changes to has a button with text "Add image", it has successfully changed activity to gallery.

QuizTest class:
Test correct answer and increment score -> correctAnswerIncreasesScore() 
The test sets the activity to quiz activity.
It presses the button which has the correct answer.
It checks if the score label text changes to "Score 1/1"

Test incorrect answer -> wrongAnswerIncreasesScore()
The test sets the activity to quiz activity 
It presses the first wrong option
It checks if the score label text changes to "Score 0/0"

GalleryTest class:
Test add image -> addImage_increasesImageCount()
The tests sets activty to gallery
It makes the image picker pick a preset image.
It writes Cat as label
Saves the label
Then checks if there is a label with text "Cat".
If the image with the label cat was added, it will pass.

Test remove image -> deleteImage_removesImage()
The tests sets activty to gallery
It makes the image picker pick a preset image.
It writes Cat as label
Saves the label
Then checks if there is a label with text "Cat".
If the image with the label cat was added, it will pass.
It finds the button called delete_image_button and deletes Cat
if it checks if the cat is not there, if not, it will pass.
