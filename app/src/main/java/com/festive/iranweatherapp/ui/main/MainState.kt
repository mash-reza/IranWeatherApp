package com.festive.iranweatherapp.ui.main

sealed class MainState {
    object Choose : MainState()
    class Home(val id:Int) : MainState()
}