package pl.expert.mobilewzr.util

fun String.isFullTime(): Boolean {
    return this.matches(Regex("S[0-9][0-9]-[0-9][0-9]"))
}