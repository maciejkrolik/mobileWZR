package pl.expert.mobilewzr.util

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayout
import pl.expert.mobilewzr.R
import java.text.SimpleDateFormat
import java.util.*

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
 * Converts Date object to formatted string used in messaging features
 */
fun Date.toFormattedString(withNewLine: Boolean = true): String {
    val format = if (withNewLine) {
        "HH:mm\ndd/MM/yyyy"
    } else {
        "HH:mm dd/MM/yyyy"
    }
    val sdf = SimpleDateFormat(format, Locale.UK).apply {
        timeZone = TimeZone.getTimeZone("Europe/Warsaw")
    }
    return sdf.format(this)
}

/**
 * Hides the keyboard
 */
fun Activity.hideKeyboard() {
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
    }
}