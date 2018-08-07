package ru.surfstudio.android.animations.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.animations.anim.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Cross fade animation
        show_cross_fade_animation_btn.setOnClickListener {
            AnimationUtil.crossfadeViews(first_iv, second_iv)
        }
        reset_cross_fade_animation_btn.setOnClickListener {
            AnimationUtil.crossfadeViews(second_iv, first_iv)
        }

        //Fade-In & Fade Out
        fade_animation_widget.setShowAnimationCallback { it.fadeOut() }
        fade_animation_widget.setResetAnimationCallback { it.fadeIn() }

        // Pulse animation
        pulse_animation_widget.setResetBtnEnabled(false)
        pulse_animation_widget.setShowAnimationCallback{
            pulse_animation_widget.setShowBtnEnabled(false)
            it.pulseAnimation()
        }

        // New size animation
        //new_size_animation_widget.setShowAnimationCallback { it.toSize(100, 100) }
    }
}
