package com.shelazh.myclass.ui.editProfile

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.openCamera
import com.crocodic.core.extension.openGallery
import com.crocodic.core.extension.snacked
import com.crocodic.core.extension.text
import com.crocodic.core.extension.textOf
import com.crocodic.core.helper.StringHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.shelazh.myclass.R
import com.shelazh.myclass.base.activity.BaseActivity
import com.shelazh.myclass.data.local.User
import com.shelazh.myclass.data.repository.UserRepository
import com.shelazh.myclass.databinding.ActivityEditProfileBinding
import com.shelazh.myclass.util.binding.ImageViewBindingAdapter.imageBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileActivity :
    BaseActivity<ActivityEditProfileBinding, EditProfileViewModel>(R.layout.activity_edit_profile) {

    @Inject
    lateinit var userRepository: UserRepository

    private var user: User? = null
    private var filePhoto: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observe()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnEditProfile.setOnClickListener {
            validateForm()
        }
        binding.imgProfileEdit.setOnClickListener {
            openPictureDialog()
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    user = viewModel.getUser()
                    Log.d("EditProfileActivity", "User data: $user")
                    if (user != null) {
                        binding.etNameEdit.setText(user!!.name)
                        binding.etPhoneEdit.setText("${user!!.phone}")

                        val avatar = StringHelper.generateTextDrawable(
                            StringHelper.getInitial(user!!.name?.trim()),
                            ContextCompat.getColor(this@EditProfileActivity, R.color.grape),
                            binding.imgProfileEdit.measuredWidth
                        )
                        if (user!!.photo.isNullOrEmpty()) {
                            binding.imgProfileEdit.setImageDrawable(avatar)
                        } else {
                            val requestOption = RequestOptions().placeholder(avatar).circleCrop()
                            Glide
                                .with(this@EditProfileActivity)
                                .load(StringHelper.validateEmpty(user!!.photo))
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .apply(requestOption)
                                .error(avatar)
                                .into(binding.imgProfileEdit)
                        }
                    }
                }
            }
        }
    }

    private fun openPictureDialog() {
        MaterialAlertDialogBuilder(this).apply {
            setItems(R.array.label_image_source) { dialog, which ->
                when (which) {
                    0 -> activityLauncher.openCamera(
                        this@EditProfileActivity,
                        "$packageName.fileprovider"
                    ) { file, e ->
                        filePhoto = file
                        displayImageFromFile(file, binding.imgProfileEdit)
                    }

                    1 -> activityLauncher.openGallery(this@EditProfileActivity) { file, e ->
                        filePhoto = file
                        displayImageFromFile(file, binding.imgProfileEdit)
                    }
                }
            }
        }
    }

    private fun displayImageFromFile(imageFile: File?, imageView: ImageView) {
        if (imageFile == null) return
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
        imageView.setImageBitmap(bitmap)
    }

    private fun validateForm() {
        val name = binding.etNameEdit.text.toString().trim()
        val phone = binding.etPhoneEdit.text.toString().trim()

        val oldName = user?.name
        val oldPhone = user?.phone.toString()

        if (filePhoto == null) {
            if (name.isEmpty() || phone.isEmpty()) {
                binding.root.snacked("Tidak boleh kosong")
                return
            }
            if (name == oldName && phone == oldPhone) {
                binding.root.snacked("Tidak ada perubahan data")
                return
            }
            viewModel.updateProfileName(name, phone)
        } else {
            viewModel.updateProfile(name, phone, filePhoto!!)
        }
    }
}
//    companion object {
//        const val DATA = "data"
//    }