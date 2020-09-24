package pl.expert.mobilewzr.util

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import pl.expert.mobilewzr.R

fun String.isFullTime(): Boolean {
    return this.matches(Regex("S[0-9][0-9]-[0-9][0-9]"))
}

/**
 * Sets the default alert dialog animations, background and then show it
 */
fun AlertDialog.setAttrsAndShow() {
    this.window?.setBackgroundDrawableResource(R.drawable.background_dialog)
    this.window?.attributes?.windowAnimations = R.style.AlertDialogAnimation
    this.show()
}

fun Int.dpToPx(context: Context): Float {
    return this * context.resources.displayMetrics.density
}

/**
 * Adds blue dot next to tab layout name
 */
fun TabLayout.addCurrentDayOrWeekIndicator(context: Context, tabNumber: Int) {
    this.getTabAt(tabNumber)?.orCreateBadge?.apply {
        horizontalOffset = -10.dpToPx(context).toInt()
        verticalOffset = 10.dpToPx(context).toInt()
        badgeGravity = BadgeDrawable.TOP_START
        backgroundColor = ContextCompat.getColor(context, R.color.colorPrimary)
    }
}

/**
 * Scroll to start after submitting the list. Works only with LinearLayoutManager
 */
fun ListAdapter<*, *>.addScrollToStartAfterSubmitListener(recyclerView: RecyclerView) {
    this.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            (recyclerView.layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(
                positionStart,
                0
            )
        }
    })
}