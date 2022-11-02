package com.aaron.emaillinkauthsample.nav

interface SampleDestination {
    val route: String
}

object SignIn : SampleDestination {
    override val route = "sign_in"
}

object Home : SampleDestination {
    override val route = "home"
}