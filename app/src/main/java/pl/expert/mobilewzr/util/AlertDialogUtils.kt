package pl.expert.mobilewzr.util

import androidx.appcompat.app.AlertDialog
import pl.expert.mobilewzr.R

/**
 * Sets the default alert dialog animations, background and then show it
 */
fun AlertDialog.setAttrsAndShow() {
    this.window?.setBackgroundDrawableResource(R.drawable.background_dialog)
    this.window?.attributes?.windowAnimations = R.style.AlertDialogAnimation
    this.show()
}