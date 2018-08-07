package ru.surfstudio.android.animations.sample.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.animation_layout.view.*
import ru.surfstudio.android.animations.sample.R

class AnimationWidget(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {

    private lateinit var imageView: ImageView

    init {
        initViews()
        initAttr(context, attrs)
    }

    private fun initViews() {
        View.inflate(context, R.layout.animation_layout, this)
        imageView = findViewById(R.id.animation_iv)
    }

    private fun initAttr(context: Context, attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AnimationWidget)
        try {
            animation_name_tv.text = a.getString(R.styleable.AnimationWidget_animationName)
            show_animation_btn.text = a.getString(R.styleable.AnimationWidget_showBtnText)
            reset_animation_btn.text = a.getString(R.styleable.AnimationWidget_resetBtnText)
        } finally {
            a.recycle()
        }
    }

    fun setShowAnimationCallback(animate: (imageView: ImageView) -> Unit) {
        show_animation_btn.setOnClickListener { animate(imageView) }
    }

    fun setResetAnimationCallback(animate: (imageView: ImageView) -> Unit) {
        reset_animation_btn.setOnClickListener { animate(imageView) }
    }
}