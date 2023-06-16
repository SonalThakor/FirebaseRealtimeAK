package com.example.firebaserealtimeak.activity

import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaserealtimeak.databinding.ActivityImageUploadBinding
import com.example.firebaserealtimeak.db.ImageUpload
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

class ImageUploadActivity : AppCompatActivity() {

    private val PICK_IMAGE_REQUEST = 1

    private lateinit var binding: ActivityImageUploadBinding

    private var mImageUri: Uri? = null

    private lateinit var mStorageRef: StorageReference
    private lateinit var mDatabaseRef: DatabaseReference

    private var mUploadTask: StorageTask<UploadTask.TaskSnapshot>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImageUploadBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        mStorageRef = FirebaseStorage.getInstance().reference.child("uploads")
        mDatabaseRef = FirebaseDatabase.getInstance().reference.child("uploads")

        binding.buttonChooseImage.setOnClickListener {
            openFileChooser()
        }

        binding.buttonUpload.setOnClickListener {
            if (mUploadTask != null && mUploadTask!!.isInProgress) {
                Toast.makeText(this, "Upload in progress", Toast.LENGTH_SHORT).show()
            } else {
                uploadFile()
            }
        }

        binding.textViewShowUploads.setOnClickListener {

        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
            && data != null && data.data != null) {
            mImageUri = data.data

            Picasso.get().load(mImageUri).into(binding.imageView)
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver: ContentResolver = contentResolver
        val mime: MimeTypeMap = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun uploadFile() {
        mImageUri?.let { uri ->
            val fileReference: StorageReference = mStorageRef.child(System.currentTimeMillis().toString()
                    + "." + getFileExtension(uri))

            mUploadTask = fileReference.putFile(uri)
                .addOnSuccessListener(OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    val handler = Handler()
                    handler.postDelayed({
                        binding.progressBar.progress = 0
                    }, 500)

                    Toast.makeText(this, "Upload successful", Toast.LENGTH_LONG).show()
                    val upload = ImageUpload(binding.editTextFileName.text.toString().trim(),
                        taskSnapshot.uploadSessionUri.toString())
                    val uploadId: String = mDatabaseRef.push().key.toString()
                    mDatabaseRef.child(uploadId).setValue(upload)
                })
                .addOnFailureListener(OnFailureListener { e ->
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                })
                .addOnProgressListener(OnProgressListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    binding.progressBar.progress = progress.toInt()
                })
        } ?: run {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show()
        }
    }
}
