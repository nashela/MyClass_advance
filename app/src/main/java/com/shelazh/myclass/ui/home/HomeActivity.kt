package com.shelazh.myclass.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.SearchView
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
import com.crocodic.core.extension.text
import com.crocodic.core.helper.StringHelper
import com.crocodic.core.helper.log.Log
import com.shelazh.myclass.ui.detailProfile.DetailProfileActivity
import com.shelazh.myclass.R
import com.shelazh.myclass.base.activity.BaseActivity
import com.shelazh.myclass.data.SchoolModel
import com.shelazh.myclass.data.local.User
import com.shelazh.myclass.ui.setting.SettingActivity
import com.shelazh.myclass.databinding.ActivityHomeBinding
import com.shelazh.myclass.databinding.ItemFriendBinding
import com.shelazh.myclass.ui.detailFriend.DetailFriendActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {

    lateinit var user: User
    lateinit var schoolList: SchoolModel

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

        viewModel.listFriend()
    }

    private fun observe() {
        val adapter =
            ReactiveListAdapter<ItemFriendBinding, User>(R.layout.item_friend).initItem { position, data ->
                toDetail(data)
            }

        // Observe friendResponse
        lifecycleScope.launch {
            viewModel.friendResponse.collect { response ->
                withContext(Dispatchers.Main) {
                    response.data.let {
                        adapter.submitList(it)
//                        viewModel.listFriend()
                    }
                }
            }
        }

        binding.recyclerView.adapter = adapter

        // Observe user data from ViewModel
        lifecycleScope.launch {
            viewModel.user.collect { user ->
                val headerView = binding.navigationView.getHeaderView(0)
//                val headerPhoto = headerView.findViewById<ImageView>(R.id.img_detail_profile)
                val headerUserName = headerView.findViewById<TextView>(R.id.tv_name_header)
                val headerUserPhone = headerView.findViewById<TextView>(R.id.tv_phone_header)

//                if (user?.photo != null) {
//                    Glide
//                        .with(this@HomeActivity)
//                        .load(StringHelper.validateEmpty(user.photo))
//                        .transition(DrawableTransitionOptions.withCrossFade())
//                        .error(R.drawable.error)
//                        .into(headerPhoto)
//                } else {
//                    headerPhoto.setImageResource(R.color.grape)
//                }

                headerUserName.text = user?.name
                headerUserPhone.text = user?.phone.toString()

                headerView.setOnClickListener {
                    val intent =
                        Intent(this@HomeActivity, DetailProfileActivity::class.java).apply {
                            putExtra("name", user?.name)
                            putExtra("phone", user?.phone)
                            putExtra("photo", user?.photo)
//                        putExtra("school", user?.schoolId)
                        }
                    startActivity(intent)
                    binding.drawerLayout.closeDrawers()
                }
            }
        }
    }

    private fun toDetail(data: User) {
        openActivity<DetailFriendActivity> {
            putExtra(DetailFriendActivity.DATA, data)
            putExtra("id", data.id)
            putExtra("name", data.name)
//            putExtra("school", schoolList.schoolName)
            putExtra("phone", data.phone)
        }
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

//    private fun filterList(query: String) {
//        val filteredList = viewModel.friendResponse.value?.data?.filter {
//            it.name.contains(query, ignoreCase = true)
//        }
//        (binding.recyclerView.adapter as? ReactiveListAdapter<*, *>)?.submitList(filteredList)
//    }
