package com.example.visahackathon

class User(val uuid: String = "",
           val email: String = "",
           val password: String = "",
           val name: String = "",
           val zipCode: Int = 0,
           val profileImageUrl: String = "",
           val amountDonated: Int = 0,
           val donateGoal: Int = 0)