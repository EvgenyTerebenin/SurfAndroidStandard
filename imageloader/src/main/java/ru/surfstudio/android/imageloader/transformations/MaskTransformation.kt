/*
  Copyright (c) 2018-present, SurfStudio LLC.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package ru.surfstudio.android.imageloader.transformations

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.annotation.DrawableRes
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool

/**
 * Маскирование изображения произвольной фигурой.
 *
 * Поддерживает 9-patch маски.
 */
class MaskTransformation(val context: Context,
                         private val overlayBundle: OverlayBundle) : BaseGlideImageTransformation() {

    private val paint = Paint()

    init {
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    override fun getId() = MaskTransformation::class.java.canonicalName.toString()

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
        val width = toTransform.width
        val height = toTransform.height

        val bitmap = pool.get(width, height, Bitmap.Config.ARGB_8888)
        bitmap.setHasAlpha(true)

        val mask = getMaskDrawable(context, overlayBundle.maskResId)

        val canvas = Canvas(bitmap)
        mask.setBounds(0, 0, width, height)
        mask.draw(canvas)
        canvas.drawBitmap(toTransform, 0f, 0f, paint)

        return bitmap
    }

    override fun hashCode() = getId().hashCode()

    override fun equals(other: Any?) = other is MaskTransformation

    private fun getMaskDrawable(context: Context, maskId: Int) =
            (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.getDrawable(maskId)
            } else {
                @Suppress("DEPRECATION")
                context.resources.getDrawable(maskId)
            }) ?: throw IllegalArgumentException("MaskTransformation error / maskId is invalid")

    /**
     * Конфигурационные данных для трансформации [MaskTransformation].
     */
    data class OverlayBundle(val isOverlay: Boolean = false,
                             @DrawableRes val maskResId: Int = -1)
}