package com.example.core.domain.use_case

class FilterDigits {

    operator fun invoke(text: String): String {
        return text.filter { it.isDigit() }
    }
}