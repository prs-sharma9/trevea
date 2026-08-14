package com.learn.android.trevea.utils

import android.text.Html

fun String.decodedHtml(): String {
    return Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
}