package com.example.catalogmovie.ui.screen.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import androidx.core.app.ActivityOptionsCompat
import com.example.catalogmovie.R
import com.example.catalogmovie.databinding.ActivitySplashScreenBinding
import com.example.catalogmovie.ui.screen.genrelist.GenreListActivity
import com.example.catalogmovie.ui.screen.movielist.MovieListActivity
import com.example.catalogmovie.utils.getAnim
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var topAnim: Animation
    private lateinit var bottomAnim: Animation
    private lateinit var viewBinding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        viewBinding.lifecycleOwner = this
        setContentView(viewBinding.root)

        initAnimation()
        GlobalScope.launch {
            startAnimation()
            delay(1500L)
            startActivity(
                Intent(this@SplashScreenActivity, GenreListActivity::class.java),
                ActivityOptionsCompat.makeCustomAnimation(
                    applicationContext,
                    android.R.anim.fade_in, android.R.anim.fade_out
                ).toBundle()
            )
            finish()
        }
    }

    private fun initAnimation() {
        topAnim = getAnim(R.anim.splash_top)
        bottomAnim = getAnim(R.anim.splash_bottom)
    }

    private fun startAnimation() {
        viewBinding.rlMovieIcon.startAnimation(topAnim)
        viewBinding.tvAppName.startAnimation(bottomAnim)
    }
}