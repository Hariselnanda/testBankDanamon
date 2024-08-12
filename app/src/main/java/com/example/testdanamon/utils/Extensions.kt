package com.example.testdanamon.utils

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData


fun View.visible(){
    this.visibility = View.VISIBLE
}

fun View.inVisible(){
    this.visibility = View.INVISIBLE
}

fun View.gone(){
    this.visibility = View.GONE
}

fun String.isValidEmail(): Boolean {
    val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
    return emailRegex.matches(this)
}

fun String.isPasswordLongEnough() = length >= 6

fun String.isPasswordHasEnoughDigits() = count(Char::isDigit) > 0

fun String.isPasswordHasMixedCase() = any(Char::isLowerCase) && any(Char::isUpperCase)

fun String.isPasswordHasSpecialChar() = any { it in "~!@#$%^&*()-_=+[]{};:'\",.<>/?\\" }


// Kondisi hanya dengan panjang password
val passwordCondition1 = listOf(String::isPasswordLongEnough)

// Kombinasi kondisi yang terdiri dari panjang password, password harus terdapat digit angka dan password harus terdapat huruf besar dan huruf kecil
val passwordCondition2 = listOf(String::isPasswordLongEnough, String::isPasswordHasEnoughDigits, String::isPasswordHasMixedCase)

// Kombinasi kondisi yang terdiri dari panjang password, password harus terdapat digit angka, password harus terdapat huruf besar dan huruf kecil, dan password harus terdapat spesial karakter
val passwordCondition3 = listOf(String::isPasswordLongEnough, String::isPasswordHasEnoughDigits, String::isPasswordHasMixedCase, String::isPasswordHasSpecialChar)

// Kondisi
// Validasi Kondisi 1
val String.condition1Check get() = passwordCondition1.all { check -> check(this) }
// Validasi Kondisi 2
val String.condition2Check get() = passwordCondition2.all { check -> check(this) }
// Validasi Kondisi 3
val String.condition3Check get() = passwordCondition3.all { check -> check(this) }


inline fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
    this.observe(owner) { value ->
        value?.let(observer)
    }
}

inline fun <T> LiveData<T>.observeNull(owner: LifecycleOwner, crossinline observer: () -> Unit) {
    this.observe(owner) { value ->
        if (value == null) {
            observer()
        }
    }
}

