package com.example.homework3.model

data class ContactView(
    val id: Long,
    val name: String,
    val lastName: String,
    val phoneNumber: String,
    val checked: Boolean,
    val isEditState: Boolean
)