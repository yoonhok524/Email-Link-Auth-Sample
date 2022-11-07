package com.aaron.emaillinkauthsample.nav

interface SampleDestination {
    val route: String
}

object SignIn : SampleDestination {
    override val route = "sign_in"
}

object Home : SampleDestination {
    const val KEY_EMAIL_LINK = "emailLink"

    override val route = "home?emailLink={${KEY_EMAIL_LINK}}"

    fun getRoute(emailLink: String): String {
        return "home?emailLink={$emailLink}"
    }
}