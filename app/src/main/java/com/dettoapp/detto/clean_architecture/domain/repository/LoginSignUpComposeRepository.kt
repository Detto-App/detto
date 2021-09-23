package com.dettoapp.detto.clean_architecture.domain.repository

interface LoginSignUpComposeRepository
{
    fun getUserRole(): Int
}