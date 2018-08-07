package ru.surfstudio.android.animations.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ru.surfstudio.android.animations.anim.AnimationUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showCrossFadeAnimationBtn.setOnClickListener {
            AnimationUtil.crossfadeViews(first_iv, second_iv)
        }
        resetCrossFadeAnimationBtn.setOnClickListener {
            AnimationUtil.crossfadeViews(second_iv, first_iv)
        }
    }
}
