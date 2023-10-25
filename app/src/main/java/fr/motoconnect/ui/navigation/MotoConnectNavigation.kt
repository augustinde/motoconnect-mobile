package fr.motoconnect.ui.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import fr.motoconnect.R

enum class MotoConnectNavigation(@StringRes val title: Int, @DrawableRes val icon: Int){
    Homepage(title = R.string.homepage, icon = R.drawable.homepage),
    Map(title = R.string.map, icon = R.drawable.map),
    Moto(title = R.string.moto, icon = R.drawable.moto),
    Profile(title = R.string.profile, icon = R.drawable.profile)
}