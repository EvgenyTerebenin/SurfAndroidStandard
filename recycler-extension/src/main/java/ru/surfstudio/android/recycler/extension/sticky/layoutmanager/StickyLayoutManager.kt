/*
    Copyright 2016 Brandon Gogetap

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
package ru.surfstudio.android.recycler.extension.sticky.layoutmanager

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import ru.surfstudio.android.recycler.extension.sticky.item.StickyFooter
import ru.surfstudio.android.recycler.extension.sticky.item.StickyHeader
import java.util.*

class StickyLayoutManager(context: Context, orientation: Int, reverseLayout: Boolean, headerHandler: StickyHeaderHandler) : LinearLayoutManager(context, orientation, reverseLayout) {

    private var positioner: StickyHeaderPositioner? = null
    private var headerHandler: StickyHeaderHandler? = null

    private val headerPositions = ArrayList<Int>()  //позиции элементов с поведением Sticky Header
    private val footerPositions = ArrayList<Int>() //позиции элементов с поведением Sticky Footer

    private var viewRetriever: ViewRetriever.RecyclerViewRetriever? = null
    private var headerElevation = StickyHeaderPositioner.NO_ELEVATION
    private var listener: StickyHeaderListener? = null

    private val visibleHeaders: Map<Int, View>
        get() {
            val visibleHeaders = LinkedHashMap<Int, View>()

            for (i in 0 until childCount) {
                val view = getChildAt(i)
                val dataPosition = getPosition(view)
                if (headerPositions.contains(dataPosition)) {
                    visibleHeaders[dataPosition] = view
                }
            }
            return visibleHeaders
        }

    private val visibleFooters: Map<Int, View>
        get() {
            val visibleFooters = LinkedHashMap<Int, View>()

            for (i in 0 until childCount) {
                val view = getChildAt(i)
                val dataPosition = getPosition(view)
                if (footerPositions.contains(dataPosition)) {
                    visibleFooters[dataPosition] = view
                }
            }
            return visibleFooters
        }

    constructor(context: Context, headerHandler: StickyHeaderHandler) : this(context, LinearLayoutManager.VERTICAL, false, headerHandler) {
        init(headerHandler)
    }

    init {
        init(headerHandler)
    }

    private fun init(stickyHeaderHandler: StickyHeaderHandler) {
        Preconditions.checkNotNull(stickyHeaderHandler, "StickyHeaderHandler == null")
        this.headerHandler = stickyHeaderHandler
    }

    /**
     * Register a callback to be invoked when a header is attached/re-bound or detached.
     *
     * @param listener The callback that will be invoked, or null to unset.
     */
    fun setStickyHeaderListener(listener: StickyHeaderListener?) {
        this.listener = listener
        if (positioner != null) {
            positioner!!.setListener(listener)
        }
    }

    /**
     * Enable or disable elevation for Sticky Headers.
     *
     *
     * If you want to specify a specific amount of elevation, use
     * [StickyLayoutManager.elevateHeaders]
     *
     * @param elevateHeaders Enable Sticky Header elevation. Default is false.
     */
    fun elevateHeaders(elevateHeaders: Boolean) {
        this.headerElevation = if (elevateHeaders)
            StickyHeaderPositioner.DEFAULT_ELEVATION
        else
            StickyHeaderPositioner.NO_ELEVATION
        elevateHeaders(headerElevation)
    }

    /**
     * Enable Sticky Header elevation with a specific amount.
     *
     * @param dp elevation in dp
     */
    fun elevateHeaders(dp: Int) {
        this.headerElevation = dp
        if (positioner != null) {
            positioner!!.setElevateHeaders(dp)
        }
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
        super.onLayoutChildren(recycler, state)
        cacheHeaderPositions()
        if (positioner != null) {
            runPositionerInit()
        }
        Log.d("LOG", "1111 onLayoutChildren")
        resetStickyItemsPositioner()
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val scroll = super.scrollVerticallyBy(dy, recycler, state)
        if (Math.abs(scroll) > 0) {
            resetStickyItemsPositioner()
        }
        //Log.d("LOG", "1111 headerPositionToShow = ${findLastVisibleItemPosition()}")
        return scroll
    }

    override fun scrollHorizontallyBy(dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
        val scroll = super.scrollHorizontallyBy(dx, recycler, state)
        if (Math.abs(scroll) > 0) {
            resetStickyItemsPositioner()
        }
        return scroll
    }

    override fun removeAndRecycleAllViews(recycler: RecyclerView.Recycler) {
        super.removeAndRecycleAllViews(recycler)
        positioner?.clearHeader()
        positioner?.clearFooter()
    }

    override fun onAttachedToWindow(view: RecyclerView?) {
        Preconditions.validateParentView(view!!)
        viewRetriever = ViewRetriever.RecyclerViewRetriever(view)
        positioner = StickyHeaderPositioner(view)
        positioner!!.setElevateHeaders(headerElevation)
        positioner!!.setListener(listener)
        if (headerPositions.size > 0) {
            // Layout has already happened and header positions are cached. Catch positioner up.
            positioner!!.setStickyPositions(headerPositions, footerPositions)
            runPositionerInit()
        }
        Log.d("LOG", "1111 onAttachedToWindow")
        super.onAttachedToWindow(view)
    }

    private fun runPositionerInit() {
        positioner!!.reset(orientation)
        resetStickyItemsPositioner()
    }

    /**
     * Перерасчёт позиций закреплённых элементов списка.
     */
    private fun resetStickyItemsPositioner() {
        positioner?.updateHeaderState(
                findFirstVisibleItemPosition(),
                visibleHeaders,
                viewRetriever,
                findFirstCompletelyVisibleItemPosition() == 0
        )
        positioner?.updateFooterState(
                findLastVisibleItemPosition(),
                visibleFooters,
                viewRetriever,
                false
        )
    }

    private fun cacheHeaderPositions() {
        headerPositions.clear()
        val adapterData = headerHandler?.getAdapterData()
        if (adapterData == null) {
            positioner?.setStickyPositions(headerPositions, footerPositions)
            return
        }

        for (i in adapterData.indices) {
            if (adapterData[i] is StickyHeader) {
                headerPositions.add(i)
            } else if (adapterData[i] is StickyFooter) {
                footerPositions.add(i)
            }
        }
        Log.d("LOG", "1111 footerPositions = $footerPositions")
        if (positioner != null) {
            positioner!!.setStickyPositions(headerPositions, footerPositions)
        }
    }
}