package com.example.catalogmovie.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.catalogmovie.R
import com.example.catalogmovie.utils.Constant.Companion.IMAGE_BASE_URL_W500
import java.text.SimpleDateFormat
import java.util.*

fun <T : ViewModel> AppCompatActivity.obtainViewModel(viewModelClass: Class<T>) =
    ViewModelProvider(this).get(viewModelClass)

fun ImageView.load(url: String) {
    Glide.with(context)
        .load(url)
        .placeholder(R.drawable.no_image)
        .error(R.drawable.no_image)
        .into(this)
}

fun Context.getAnim(animId: Int): Animation = AnimationUtils.loadAnimation(this, animId)

fun String?.getYear(): String {
    this?.apply {
        return split("-")[0]
    }
    return ""
}

fun Date.format(format: String = "yyyy-MM-dd"): String {
    return try {
        SimpleDateFormat(format, Locale.getDefault()).format(this)
    } catch (e: Exception) {
        throw RuntimeException("${e.message}")
    }
}

fun Int?.getRuntimeFormatted(): String {
    this?.let {
        return (it.toDouble() / 60).toInt().toString() + "h " + it % 60 + "m"
    }
    return ""
}

fun DisplayMetrics.getSizeByScreenSize(): Int {
    return when {
        this.widthPixels in 1001..1999 -> {
            this.widthPixels / 2
        }
        this.widthPixels > 2000 -> {
            this.widthPixels / 4
        }
        else -> {
            this.widthPixels / 3
        }
    }
}

fun String?.toImageUrl(): String {
    this?.let { return IMAGE_BASE_URL_W500+this }
    return ""
}