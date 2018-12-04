package ru.surfstudio.standard.base_ui.loadstate.presentation

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import ru.surfstudio.standard.base_ui.loadstate.PlaceHolderViewContainer
import ru.surfstudio.android.core.mvp.loadstate.SimpleLoadStatePresentation
import ru.surfstudio.standard.base_ui.loadstate.state.EmptyLoadState
import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface
import ru.surfstudio.android.template.base_ui.R
import ru.surfstudio.standard.base_ui.loadstate.utils.clickAndFocus

/**
 * Представление состояния EmptyLoadState, с картинкой, тайтлом, сабтайтлом и кнопкой
 */
class EmptyLoadStatePresentation(
        private val placeHolder: PlaceHolderViewContainer
) : SimpleLoadStatePresentation<EmptyLoadState>() {

    @StringRes
    var messageText: Int = R.string.load_state_empty

    @ColorInt
    private val backgroundColor = ContextCompat.getColor(placeHolder.context, R.color.colorWindowBackgroundDark)

    private lateinit var messageView: TextView

    private val view: View by lazy {
        LayoutInflater.from(placeHolder.context)
                .inflate(R.layout.load_state_empty, placeHolder, false)
                .apply {
                    messageView = findViewById(R.id.empty_load_state_tv)
                }
    }

    override fun showState(state: EmptyLoadState) {
        initViews(view)

        with(placeHolder) {
            changeViewTo(view)
            setBackgroundColor(backgroundColor)
            clickAndFocus(true)
            show()
        }
    }

    override fun hideState(state: EmptyLoadState, nextState: LoadStateInterface) {
        with(placeHolder) {
            setBackgroundColor(ContextCompat.getColor(placeHolder.context, android.R.color.transparent))
            clickAndFocus(false)
        }
    }

    private fun initViews(view: View) {
        messageView.text = view.context.getString(messageText)
    }
}