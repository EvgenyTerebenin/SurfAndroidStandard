package ru.surfstudio.android.easyadapter.holder.async

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import ru.surfstudio.android.easyadapter.R
import ru.surfstudio.android.easyadapter.holder.BaseViewHolder

open class AsyncBaseViewHolder private constructor(
        parent: ViewGroup
) : BaseViewHolder(FrameLayout(parent.context)), AsyncViewHolder {
    final override var isItemViewInflated = false
    final override var fadeInDuration = DEFAULT_FADE_IN_DURATION
    final override var resizeDuration = DEFAULT_RESIZE_DURATION

    /**
     * Имплементация BaseViewHolder для асинхроного инфлейта layoutId
     *
     * @param layoutId айди ресурса для инфлейта основной вью
     * @param stubLayoutId айди ресурса для инфлейта вью, которая будет видна до появления основной
     */
    constructor(parent: ViewGroup,
                @LayoutRes layoutId: Int,
                @LayoutRes stubLayoutId: Int = R.layout.default_async_stub_layout
    ) : this(parent) {
        inflateStubView(itemView as ViewGroup, stubLayoutId)
        inflateItemView(parent, itemView, layoutId)
    }
}