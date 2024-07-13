import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.smartpanel.R

class ChoosePict(
    private val activity: Activity,
    private val profileImage: ImageView,
    private val choosePhotoButton: ImageButton,
    private val editPhotoButton: ImageButton
) {

    private val PICK_IMAGE_REQUEST = 1

    init {
        choosePhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activity.startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        editPhotoButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activity.startActivityForResult(intent, PICK_IMAGE_REQUEST)
            profileImage.setImageURI(null)
            editPhotoButton.visibility = View.VISIBLE
            choosePhotoButton.setImageResource(R.drawable.ic_edit)
            choosePhotoButton.visibility = View.GONE
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            val selectedImageUri: Uri = data.data!!

            Glide.with(activity)
                .load(selectedImageUri)
                .apply(RequestOptions.circleCropTransform())
                .into(profileImage)

            editPhotoButton.visibility = View.VISIBLE
            choosePhotoButton.setImageResource(R.drawable.ic_edit)
            choosePhotoButton.visibility = View.GONE
        }
    }
}