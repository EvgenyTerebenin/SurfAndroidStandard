package ru.surfstudio.android.loadstate.sample.ui.base.loadstate.states

import ru.surfstudio.android.core.mvp.loadstate.LoadStateInterface

class EmptyLoadState: LoadStateInterface {
    override fun equals(other: Any?): Boolean {
        return other is EmptyLoadState
    }
}