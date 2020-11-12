package com.qingmei2.rximagepicker_extension_zhihu

import android.app.Activity
import android.os.Build
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi
import androidx.annotation.StyleRes

import com.qingmei2.rximagepicker_extension.MimeType
import com.qingmei2.rximagepicker_extension.engine.ImageEngine
import com.qingmei2.rximagepicker_extension.entity.CaptureStrategy
import com.qingmei2.rximagepicker_extension.entity.SelectionSpec
import com.qingmei2.rximagepicker_extension.filter.Filter
import com.qingmei2.rximagepicker_extension_zhihu.engine.impl.ZhihuGlideEngine

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.ArrayList

import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_BEHIND
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_FULL_USER
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LOCKED
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE
import android.content.pm.ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT

class ZhihuConfigurationBuilder
/**
 * Constructs a new specification builder on the context.
 *
 * @param mimeTypes MIME type set to select.
 */
(mimeTypes: Set<MimeType>, mediaTypeExclusive: Boolean) {

    private val mSelectionSpec: SelectionSpec = SelectionSpec.getNewCleanInstance(ZhihuGlideEngine())

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @IntDef(SCREEN_ORIENTATION_UNSPECIFIED, SCREEN_ORIENTATION_LANDSCAPE, SCREEN_ORIENTATION_PORTRAIT, SCREEN_ORIENTATION_USER, SCREEN_ORIENTATION_BEHIND, SCREEN_ORIENTATION_SENSOR, SCREEN_ORIENTATION_NOSENSOR, SCREEN_ORIENTATION_SENSOR_LANDSCAPE, SCREEN_ORIENTATION_SENSOR_PORTRAIT, SCREEN_ORIENTATION_REVERSE_LANDSCAPE, SCREEN_ORIENTATION_REVERSE_PORTRAIT, SCREEN_ORIENTATION_FULL_SENSOR, SCREEN_ORIENTATION_USER_LANDSCAPE, SCREEN_ORIENTATION_USER_PORTRAIT, SCREEN_ORIENTATION_FULL_USER, SCREEN_ORIENTATION_LOCKED)
    @kotlin.annotation.Retention(value = AnnotationRetention.SOURCE)
    internal annotation class ScreenOrientation

    init {
        mSelectionSpec.mimeTypeSet = mimeTypes
        mSelectionSpec.mediaTypeExclusive = mediaTypeExclusive
        mSelectionSpec.orientation = SCREEN_ORIENTATION_UNSPECIFIED
    }

    /**
     * Whether to show only one media type if choosing medias are only images or videos.
     *
     * @param showSingleMediaType whether to show only one media type, either images or videos.
     * @return [ZhihuConfigurationBuilder] for fluent API.
     * @see SelectionSpec.onlyShowImages
     * @see SelectionSpec.onlyShowVideos
     */
    fun showSingleMediaType(showSingleMediaType: Boolean): ZhihuConfigurationBuilder {
        mSelectionSpec.showSingleMediaType = showSingleMediaType
        return this
    }

    /**z
     * Theme for media selecting Activity.
     *
     *
     * There are two built-in themes:
     * 1. com.qingmei2.rximagepicker_extension_zhihu.R.style.Zhihu_Normal;
     * 2. com.qingmei2.rximagepicker_extension_zhihu.R.style.Zhihu_Dracula.
     * you can define a custom theme derived from the above ones or other themes.
     *
     * @param themeId theme resource id. Default value is com.qingmei2.rximagepicker_extension_zhihu.R.style.Zhihu_Normal
     * @return [ZhihuConfigurationBuilder] for fluent API.
     */
    fun theme(@StyleRes themeId: Int): ZhihuConfigurationBuilder {
        mSelectionSpec.themeId = themeId
        return this
    }

    /**
     * Show a auto-increased number or a check mark when user select media.
     *
     * @param countable true for a auto-increased number from 1, false for a check mark. Default
     * value is false.
     * @return [ZhihuConfigurationBuilder] for fluent API.
     */
    fun countable(countable: Boolean): ZhihuConfigurationBuilder {
        mSelectionSpec.countable = countable
        return this
    }

    /**
     * Maximum selectable count.
     *
     * @param maxSelectable Maximum selectable count. Default value is 1.
     * @return [ZhihuConfigurationBuilder] for fluent API.
     */
    fun maxSelectable(maxSelectable: Int): ZhihuConfigurationBuilder {
        if (maxSelectable < 1)
            throw IllegalArgumentException("maxSelectable must be greater than or equal to one")
        if (mSelectionSpec.maxImageSelectable > 0 || mSelectionSpec.maxVideoSelectable > 0)
            throw IllegalStateException("already set maxImageSelectable and maxVideoSelectable")
        mSelectionSpec.maxSelectable = maxSelectable
        return this
    }

    /**
     * Only useful when [SelectionSpec.mediaTypeExclusive] set true and you want to set different maximum
     * selectable files for image and video media types.
     *
     * @param maxImageSelectable Maximum selectable count for image.
     * @param maxVideoSelectable Maximum selectable count for video.
     * @return
     */
    fun maxSelectablePerMediaType(maxImageSelectable: Int, maxVideoSelectable: Int): ZhihuConfigurationBuilder {
        if (maxImageSelectable < 1 || maxVideoSelectable < 1)
            throw IllegalArgumentException("max selectable must be greater than or equal to one")
        mSelectionSpec.maxSelectable = -1
        mSelectionSpec.maxImageSelectable = maxImageSelectable
        mSelectionSpec.maxVideoSelectable = maxVideoSelectable
        return this
    }

    /**
     * Add filter to filter each selecting item.
     *
     * @param filter [Filter]
     * @return [ZhihuConfigurationBuilder] for fluent API.
     */
    fun addFilter(filter: Filter): ZhihuConfigurationBuilder {
        if (mSelectionSpec.filters == null) {
            mSelectionSpec.filters = ArrayList()
        }
        if (filter == null) throw IllegalArgumentException("filter cannot be null")
        mSelectionSpec.filters!!.add(filter)
        return this
    }

    /**
     * Determines whether the photo capturing is enabled or not on the media grid view.
     *
     *
     * If this value is set true, photo capturing entry will appear only on All Media's page.
     *
     * @param enable Whether to enable capturing or not. Default value is false;
     * @return [ZhihuConfigurationBuilder] for fluent API.
     */
    fun capture(enable: Boolean): ZhihuConfigurationBuilder {
        mSelectionSpec.capture = enable
        return this
    }

    /**
     * Capture strategy provided for the location to save photos including internal and external
     * storage and also a authority for [android.support.v4.content.FileProvider].
     *
     * @param captureStrategy [CaptureStrategy], needed only when capturing is enabled.
     * @return [ZhihuConfigurationBuilder] for fluent API.
     */
    fun captureStrategy(captureStrategy: CaptureStrategy): ZhihuConfigurationBuilder {
        mSelectionSpec.captureStrategy = captureStrategy
        return this
    }

    /**
     * Set the desired orientation of this activity.
     *
     * @param orientation An orientation constant as used in [ScreenOrientation].
     * Default value is [android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT].
     * @return [ZhihuConfigurationBuilder] for fluent API.
     * @see Activity.setRequestedOrientation
     */
    fun restrictOrientation(@ScreenOrientation orientation: Int): ZhihuConfigurationBuilder {
        mSelectionSpec.orientation = orientation
        return this
    }

    /**
     * Set a fixed span count for the media grid. Same for different screen orientations.
     *
     *
     * This will be ignored when [.gridExpectedSize] is set.
     *
     * @param spanCount Requested span count.
     * @return [ZhihuConfigurationBuilder] for fluent API.
     */
    fun spanCount(spanCount: Int): ZhihuConfigurationBuilder {
        if (spanCount < 1) throw IllegalArgumentException("spanCount cannot be less than 1")
        mSelectionSpec.spanCount = spanCount
        return this
    }

    /**
     * Set expected size for media grid to adapt to different screen sizes. This won't necessarily
     * be applied cause the media grid should fill the view container. The measured media grid's
     * size will be as close to this value as possible.
     *
     * @param size Expected media grid size in pixel.
     * @return [ZhihuConfigurationBuilder] for fluent API.
     */
    fun gridExpectedSize(size: Int): ZhihuConfigurationBuilder {
        mSelectionSpec.gridExpectedSize = size
        return this
    }

    /**
     * Photo thumbnail's scale compared to the View's size. It should be a float value in (0.0,
     * 1.0].
     *
     * @param scale Thumbnail's scale in (0.0, 1.0]. Default value is 0.5.
     * @return [ZhihuConfigurationBuilder] for fluent API.
     */
    fun thumbnailScale(scale: Float): ZhihuConfigurationBuilder {
        if (scale <= 0f || scale > 1f)
            throw IllegalArgumentException("Thumbnail scale must be between (0.0, 1.0]")
        mSelectionSpec.thumbnailScale = scale
        return this
    }

    /**
     * Provide an image engine.
     *
     *
     * 1. [ZhihuGlideEngine]
     * And you can implement your own image engine.
     *
     * @param imageEngine [ZhihuGlideEngine]
     * @return [ZhihuConfigurationBuilder] for fluent API.
     */
    fun imageEngine(imageEngine: ImageEngine): ZhihuConfigurationBuilder {
        SelectionSpec.setDefaultImageEngine(imageEngine)
        mSelectionSpec.imageEngine = imageEngine
        return this
    }

    fun build(): SelectionSpec {
        if (mSelectionSpec.themeId == com.qingmei2.rximagepicker_extension.R.style.Theme_AppCompat_Light)
            mSelectionSpec.themeId = R.style.Zhihu_Normal

        return mSelectionSpec
    }
}
