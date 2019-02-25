package com.example.sushikub.notificationtest

object NotificationDataFactory {

    operator fun invoke(): List<Notifies> {
        return listOf(Notifies("amazon", "https://amazon.com"),
                Notifies("amazon", "https://amazon.com"),
                Notifies("amazon", "https://amazon.com"),
                Notifies("amazon", "https://amazon.com"))
    }
}