/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.example.android.gdgfinder

import android.content.res.Resources
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.android.gdgfinder.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.InputStream

/*
* https://developer.android.com/codelabs/kotlin-android-training-material-design-dimens-colors#6
*
* Styles and themes on Android allow you to separate the details of your app design from the UI
* structure and behavior, similar to stylesheets in web design.
*
* A style is a collection of attributes that specify the appearance for a single View.
*
* A theme is a collection of attributes that's applied to an entire app, activity, or view
* hierarchy —not just an individual view.
*
* Themes can also apply styles to non-view elements, such as the status bar and window background.
*
* If you want child views to inherit styles, apply the style with the android:theme attribute.
* Android color tool at material.io
*
* A Theme is used to set the global theme for the entire app. A ThemeOverlay is used to override
* (or "overlay") that theme for specific views, especially the toolbar.
*
* You do this by applying the desired theme to the root view of the view hierarchy for which you
* want to use it.
*
* https://material.io/tools/color/.
*
*
*
* */
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setupResources()
        setupNavigation()
    }

    /**
     * Called when the hamburger menu or back button are pressed on the Toolbar
     *
     * Delegate this to Navigation.
     */
    override fun onSupportNavigateUp() = navigateUp(findNavController(R.id.nav_host_fragment), binding.drawerLayout)

    /**
     * Setup Navigation for this Activity
     */
    private fun setupNavigation() {
        // first find the nav controller
        val navController = findNavController(R.id.nav_host_fragment)

        setSupportActionBar(binding.toolbar)

        // then setup the action bar, tell it about the DrawerLayout
        setupActionBarWithNavController(navController, binding.drawerLayout)


        // finally setup the left drawer (called a NavigationView)
        binding.navigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination: NavDestination, _ ->
            val toolBar = supportActionBar ?: return@addOnDestinationChangedListener
            when(destination.id) {
                R.id.home -> {
                    toolBar.setDisplayShowTitleEnabled(false)
                    binding.heroImage.visibility = View.VISIBLE
                }
                else -> {
                    toolBar.setDisplayShowTitleEnabled(true)
                    binding.heroImage.visibility = View.GONE
                }
            }
        }
    }

    private fun setupResources() {
        val assetsReadme: BufferedReader = applicationContext.assets.open("readme.txt").bufferedReader()
        val rawReadme = applicationContext.resources.openRawResource(R.raw.readme)
    }

}
