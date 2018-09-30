package com.hbvhuwe.explorergithub.ui

import android.content.Context
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView
import android.view.GestureDetector
import android.view.View


class RecyclerViewTouchListener(
        private val context: Context,
        private val recyclerView: RecyclerView,
        private val clickListener: RecyclerViewTouchListener.ClickListener
): RecyclerView.OnItemTouchListener {
    private var gestureDetector: GestureDetector = GestureDetector(
            context,
            object: GestureDetector.SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent?): Boolean {
                    return true
                }

                override fun onLongPress(e: MotionEvent) {
                    val child = recyclerView.findChildViewUnder(e.x, e.y)
                    if (child != null) {
                        clickListener.onLongClick(child, recyclerView.getChildViewHolder(child))
                    }
                }
            }
    )

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        val child = rv.findChildViewUnder(e.x, e.y)
        if (child != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildViewHolder(child))
        }
        return false
    }

    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }

    interface ClickListener {
        fun onClick(view: View, vh: RecyclerView.ViewHolder)

        fun onLongClick(view: View, vh: RecyclerView.ViewHolder)
    }
}