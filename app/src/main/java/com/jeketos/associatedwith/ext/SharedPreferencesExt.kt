@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package com.jeketos.associatedwith.ext

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.jeketos.associatedwith.rest.Json
import io.reactivex.Observable
import java.lang.ClassCastException
import java.lang.Double as JDouble

fun Context.getPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

@Suppress("UNCHECKED_CAST")
fun <T> SharedPreferences.put(key: String, value: T, async: Boolean = true) {
    val edit = edit()
    when (value) {
        is Int -> edit.putInt(key, value)
        is Float -> edit.putFloat(key, value)
        is Double -> edit.putLong(key, java.lang.Double.doubleToLongBits(value))
        is Long -> edit.putLong(key, value)
        is String -> edit.putString(key, value)
        is Boolean -> edit.putBoolean(key, value)
        is Set<*> ->
            if (value.first() != null && value.first() is String)
                edit.putStringSet(key, value as Set<String>)
            else
                edit.putString(key, valueToString<T>(value))
        else -> {
            edit.putString(key, valueToString(value))
        }
    }
    if(async) edit.apply() else edit.commit()
}

private fun <T> valueToString(value: T): String {
    val string = Json.DEFAULT.toString(value)
    if (string.toByteArray().size > 1_000_000) {
        throw OutOfMemoryError("Object size must be lower than 1 MB.")
    }
    return string
}

fun SharedPreferences.getDouble(key: String, default: Double): Double
        = JDouble.longBitsToDouble(getLong(key, JDouble.doubleToLongBits(default)))

// for primitives in non exists returns 0, false for boolean, “” for string, emptySet() for Set<String>
inline fun <reified T> SharedPreferences.get(key: String): T? =
        when(T::class){
            Int::class ->  get(key, 0) as T
            Float::class -> get(key, 0f) as T
            Long::class -> get(key, 0L) as T
            Double::class -> get(key, 0.0) as T
            String::class -> get(key, "") as T
            Boolean::class -> get(key, false) as T
            Set::class ->
                try {
                    get(key, emptySet()) as T
                } catch (e: ClassCastException){
                    Json.DEFAULT.toObject(this.getString(key, null), T::class.java)
                }
            else -> Json.DEFAULT.toObject(this.getString(key, null), T::class.java)
        }

inline fun <reified T>SharedPreferences.get(key: String, default: T): T = when(default){
    is Int -> get(key, default) as T
    is Float -> get(key, default) as T
    is Double -> get(key, default) as T
    is Long -> get(key, default) as T
    is String ->  get(key, default) as T
    is Boolean ->  get(key, default) as T
    is Set<*> ->
        try {
            get(key, emptySet())
        } catch (e: ClassCastException){
            get<T>(key) ?: default
        } as T
    else -> get<T>(key) ?: default
}

fun SharedPreferences.get(key: String, default: Double): Double = getDouble(key, default)
fun SharedPreferences.get(key: String, default: Int): Int = getInt(key, default)
fun SharedPreferences.get(key: String, default: Long): Long = getLong(key, default)
fun SharedPreferences.get(key: String, default: Float): Float = getFloat(key, default)
fun SharedPreferences.get(key: String, default: Boolean): Boolean = getBoolean(key, default)
fun SharedPreferences.get(key: String, default: String): String = getString(key, default)
fun SharedPreferences.get(key: String, default: Set<String>): MutableSet<String> = getStringSet(key, default)

fun SharedPreferences.remove(vararg key: String) {
    edit().apply{key.forEach { this.remove(it) }}.apply()
}

inline fun <reified T>SharedPreferences.observe(key: String, default: T): Observable<T> =
        Observable.create{ emitter ->
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                emitter.onNext(get<T>(key) ?: default)
            }
            registerOnSharedPreferenceChangeListener(listener)
            emitter.setCancellable { unregisterOnSharedPreferenceChangeListener(listener) }
        }