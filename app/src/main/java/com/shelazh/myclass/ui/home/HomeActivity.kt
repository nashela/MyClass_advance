package com.shelazh.myclass.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.crocodic.core.base.adapter.ReactiveListAdapter
import com.crocodic.core.extension.openActivity
import com.crocodic.core.helper.StringHelper
import com.crocodic.core.helper.log.Log
import com.shelazh.myclass.ui.detailProfile.DetailProfileActivity
import com.shelazh.myclass.R
import com.shelazh.myclass.base.activity.BaseActivity
import com.shelazh.myclass.data.local.User
import com.shelazh.myclass.ui.setting.SettingActivity
import com.shelazh.myclass.databinding.ActivityHomeBinding
import com.shelazh.myclass.databinding.ItemFriendBinding
import com.shelazh.myclass.ui.detailFriend.DetailFriendActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.activity = this

        observe()

        binding.navigationView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_notification -> {
                    Toast.makeText(this, R.string.no_notification, Toast.LENGTH_SHORT).show()
                    true
                }

                R.id.nav_setting -> {
                    val intent = Intent(this, SettingActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> false
            }.also {
                binding.drawerLayout.closeDrawers()
            }
        }

        binding.btnMenu.setOnClickListener {
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_dehaze_24)
    }

    private fun getFriend() = lifecycleScope.launch {
        viewModel.listFriend()
    }

    private fun observe() {
        val adapter = ReactiveListAdapter<ItemFriendBinding, User>(R.layout.item_friend).initItem { position, data ->
            toDetail(data)
        }

        // Observe friendResponse
        lifecycleScope.launch {
            viewModel.friendResponse.collect {
                withContext(Dispatchers.Main) {
                    it.data.let {
                        adapter.submitList(it)
                    }
                }
            }
        }

        // Observe user data and set to headerView
        lifecycleScope.launch {
            viewModel.user.collect {
                withContext(Dispatchers.Main) {
                    val user = it.firstOrNull()
                    if (user != null) {
                        val headerView = binding.navigationView.getHeaderView(0)
                        val headerUserPhoto = headerView.findViewById<ImageView>(R.id.iv_header)
                        val headerUserName = headerView.findViewById<TextView>(R.id.tv_name_header)
                        val headerUserPhone = headerView.findViewById<TextView>(R.id.tv_phone_header)

                        headerUserName.text = user.name
                        headerUserPhone.text = user.phone
                        val avatar = StringHelper.generateTextDrawable(
                            StringHelper.getInitial(user.name?.trim()),
                            ContextCompat.getColor(this@HomeActivity, R.color.grape),
                            headerUserPhoto.measuredWidth
                        )
                        if (user.photo.isNullOrEmpty()) {
                            headerUserPhoto.setImageDrawable(avatar)
                        } else {
                            val requestOption = RequestOptions().placeholder(avatar).circleCrop()
                            Glide
                                .with(this@HomeActivity)
                                .load(StringHelper.validateEmpty(user.photo))
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .apply(requestOption)
                                .error(avatar)
                                .into(headerUserPhoto)
                        }

                        // Set click listener for the header view
                        headerView.setOnClickListener {
                            Log.d("Header test")
                            val intent = Intent(this@HomeActivity, DetailProfileActivity::class.java).apply {
                                putExtra("id", user.id)
                                putExtra("name", user.name)
                                putExtra("phone", user.phone)
                                putExtra("photoUrl", user.photo)
                            }
                            startActivity(intent)
                            binding.drawerLayout.closeDrawers()
                        }
                    }
                }
            }
        }

        binding.recyclerView.adapter = adapter
    }

    private fun toDetail(data: User) {
        openActivity<DetailFriendActivity> {
            putExtra(DetailFriendActivity.DATA, data)
            putExtra("id", data.id)
            putExtra("name", data.name)
            putExtra("school", data.schoolId)
            putExtra("phone", data.phone)
        }
    }

    override fun onStart() {
        super.onStart()
        getFriend()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                } else {
                    binding.drawerLayout.openDrawer(GravityCompat.START)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
