package com.makeawitz.witzcraft.image

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory

object BitmapDecoder {
    fun decode(
        resources: Resources,
        resId: Int,
        width: Int,
        height: Int
    ): Bitmap {
        val bitmap: Bitmap
        // Decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, resId, options)
        // Raw height and width of image
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth
        var targetWidth = imageWidth
        var targetHeight = imageHeight
        // Calculate inSampleSize
        var inSampleSize = 1
        if (imageWidth > width || imageHeight > height) {
            val halfWidth = imageWidth / 2
            val halfHeight = imageHeight / 2
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfWidth / inSampleSize > width && halfHeight / inSampleSize > height) {
                inSampleSize *= 2
                targetWidth /= 2
                targetHeight /= 2
            }
        }
        options.inSampleSize = inSampleSize
        // Decode inactive with inSampleSize set
        options.inJustDecodeBounds = false
        bitmap = BitmapFactory.decodeResource(resources, resId, options)
        // Scaling inactive to fit in the view
        return Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
    }
}