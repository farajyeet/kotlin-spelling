package extensions

import java.util.ArrayList

public inline fun <T> Iterable<T>.any() : Boolean {
    return this.iterator().hasNext()
}

public inline fun <T> Iterable<T>.or(other: Iterable<T>): Iterable<T> {
    if (this.any())
        return this
    return other
}

public inline fun <T> T.asiterable(): Iterable<T> {
    var list = ArrayList<T>()
    list.add(this)
    return list
}

public inline fun <T> T.or(other: Iterable<T>): Iterable<T> {
    return if (this != null) this.asiterable() else other
}

public inline fun <T> Iterable<T>.or(other: T): Iterable<T> {
    return if (this.any()) this else other.asiterable()
}