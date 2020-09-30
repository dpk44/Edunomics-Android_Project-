package com.dpk.hammoq

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dpk.hammoq.SelectedItemHolder.listOfSelectedPhotos
import com.mediapicker.gallery.Gallery
import com.mediapicker.gallery.GalleryConfig
import com.mediapicker.gallery.domain.contract.IGalleryCommunicator
import com.mediapicker.gallery.domain.entity.PhotoFile
import com.mediapicker.gallery.domain.entity.Rule
import com.mediapicker.gallery.domain.entity.Validation
import com.mediapicker.gallery.presentation.activity.GalleryActivity
import com.mediapicker.gallery.presentation.fragments.DefaultPage
import com.mediapicker.gallery.presentation.fragments.HomeFragment
import com.mediapicker.gallery.presentation.viewmodels.VideoFile
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    private val REQUEST_VIDEO_CAPTURE: Int = 1000
    private var fragment: HomeFragment? = null
    private var actualImage: File? = null
    private val chooseImagesURI: List<Uri> = ArrayList()

    private val compressedImage: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpGallery()
        showStepFragment()
    }

    private fun getValidation(): Validation {
        return Validation.ValidationBuilder()
            .setMinPhotoSelection(Rule.MinPhotoSelection(1, "Minimum 0 photos can be selected "))
            .setMaxPhotoSelection(Rule.MaxPhotoSelection(12, "Maximum 12 photos can be selected "))
            .build()
    }

    private fun setUpGallery() {
        val galleryConfig = GalleryConfig.GalleryConfigBuilder(
            applicationContext,
            BuildConfig.APPLICATION_ID + ".provider",
            MyClientGalleryCommunicator()
        )
            .useMyPhotoCamera(true)
            .useMyVideoCamera(false)
            .needToShowCover(false)
            .mediaScanningCriteria(GalleryConfig.MediaScanningCriteria("", ""))
            .typeOfMediaSupported(GalleryConfig.MediaType.PhotoWithFolderOnly)
            .validation(getValidation())
            .build()
        Gallery.init(galleryConfig)
    }

    private fun attachGalleryFragment() {
        try {
            val transaction = supportFragmentManager.beginTransaction()
            fragment = HomeFragment.getInstance(
                SelectedItemHolder.listOfSelectedPhotos,
                SelectedItemHolder.listOfSelectedVideos,
                defaultPageType = DefaultPage.PhotoPage
            )
            transaction.replace(container.id, fragment!!, fragment!!::class.java.simpleName)
            transaction.addToBackStack(fragment!!.javaClass.name)
            transaction.commitAllowingStateLoss()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun jumpToGallery() {
        startActivity(
            GalleryActivity.getGalleryActivityIntent(
                SelectedItemHolder.listOfSelectedPhotos,
                SelectedItemHolder.listOfSelectedVideos,
                defaultPageType = DefaultPage.PhotoPage, context = baseContext
            )
        )
    }

    private fun showStepFragment() {
        try {
            val transaction = supportFragmentManager.beginTransaction()
            val fragment = StepFragment()
            transaction.replace(container.id, fragment, fragment::class.java.simpleName)
            transaction.commitAllowingStateLoss()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    inner class MyClientGalleryCommunicator : IGalleryCommunicator {
        override fun onCloseMainScreen() {
            Toast.makeText(baseContext, "Close on main screen", Toast.LENGTH_LONG).show()

        }

        override fun actionButtonClick(
                listOfSelectedPhotos: List<PhotoFile>,
                listofSelectedVideos: List<VideoFile>
        ) {
            SelectedItemHolder.listOfSelectedPhotos = listOfSelectedPhotos
            SelectedItemHolder.listOfSelectedVideos = listofSelectedVideos
            showStepFragment()

            Log.e("TAG", listOfSelectedPhotos.toString())

            this.showMessage(listOfSelectedPhotos.toString())

            Toast.makeText(this@MainActivity, listOfSelectedPhotos.toString(),Toast.LENGTH_SHORT).show()

        }

        override fun onFolderSelect() {
            showMessage("folderClicked")
        }

        override fun captureImage() {
            showMessage("captureImage")
        }

        override fun onImageCaptured(capturedImage: File) {
            showMessage("onImageCaptured")
        }

        override fun recordVideo() {
            dispatchTakeVideoIntent()
        }

        override fun onVideoRecorded(file: File) {
            showMessage("onVideoRecorded")
        }

        private fun showMessage(msg: String) {
            Toast.makeText(applicationContext, msg, Toast.LENGTH_LONG).show()

        }
    }


    private fun dispatchTakeVideoIntent() {
        Intent(MediaStore.ACTION_VIDEO_CAPTURE).also { takeVideoIntent ->
            takeVideoIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            val videoUri: Uri? = intent.data
            Toast.makeText(baseContext, "Recorded ", Toast.LENGTH_LONG).show()
            fragment?.reloadMedia()
        }
    }

}


object SelectedItemHolder {
    var listOfSelectedPhotos = emptyList<PhotoFile>()
    var listOfSelectedVideos = emptyList<VideoFile>()
}