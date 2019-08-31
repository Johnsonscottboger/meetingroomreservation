package com.aning.meetingroomreservation.extension

/**
 * Returns `this` value if it satisfies the given [predicate] or [another], if it doesn't.
 */
public inline fun <T> T.takeIf(predicate: (T) -> Boolean, another: T): T {
    return if (predicate(this)) this else another
}