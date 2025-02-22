package com.example.notetaker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform