package com.example.muzplayer.common.extensions

import android.database.Cursor

/**
 * Created by lukatsivilashvili on 20.01.23 4:03 PM.
 */

fun <T> Cursor.getColumnValueNullable(
    name: String,
    fn: (Int) -> T?
): T? {
    val idx = getColumnIndex(name)
    return if (idx > -1) fn(idx) else null
}

fun <T> Cursor.getColumnValue(
    name: String,
    fn: (Int) -> T
): T {
    val idx = getColumnIndex(name)
    return fn(idx)
}