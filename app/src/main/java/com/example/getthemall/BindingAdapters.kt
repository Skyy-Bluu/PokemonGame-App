package com.example.getthemall

import android.animation.ObjectAnimator
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.getthemall.inventory.InventoryGridAdapter
import com.example.getthemall.battle.BattleViewModel
import com.example.getthemall.battle.Turn
import com.example.getthemall.network.Pokemon

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?){
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()

        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image))
            .into(imgView)
    }
}

// Could change this to update based on the HP diff from the livedata object
@BindingAdapter("damageTakenAnimation")
fun damageTakenAnimation(imgView: ImageView, dmgTaken: Boolean){
    if(dmgTaken){
        imgView.startAnimation(AnimationUtils.loadAnimation(imgView.context, R.anim.shake))
    }
}

@BindingAdapter("turnAnimation")
fun turnAnimation(imgView: ImageView, turn: Turn?){
    imgView.startAnimation(AnimationUtils.loadAnimation(imgView.context, R.anim.wobble))
    when (turn) {
        Turn.ENEMY -> {
            ObjectAnimator.ofFloat(imgView, "translationY", -780f).apply {
                duration = 800
                start()
            }
        }
        Turn.PLAYER -> {
            ObjectAnimator.ofFloat(imgView, "translationY", 1f).apply {
                duration = 800
                start()
            }
        }
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Pokemon>?){
    val adapter = recyclerView.adapter as InventoryGridAdapter

    adapter.submitList(data)
}