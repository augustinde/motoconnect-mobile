package fr.motosecure.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val DarkColorScheme = darkColorScheme(
    primary = Primary1,
    secondary = Secondary1,
    tertiary = Tertiary1,
    onPrimary = OnPrimary1,
    error = Red,
)

private val LightColorScheme = lightColorScheme(
    primary = Primary2,
    secondary = Secondary2,
    tertiary = Tertiary2,
    onPrimary = OnPrimary2,
    error = Red,
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun MotoSecureTheme(activated: Boolean, content: @Composable () -> Unit){

    val colorScheme = if(activated){
        LightColorScheme
    }
    else{
        DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}