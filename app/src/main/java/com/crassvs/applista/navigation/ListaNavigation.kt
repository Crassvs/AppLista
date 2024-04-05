package com.crassvs.applista.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.crassvs.applista.screens.ListaSplashScreen
import com.crassvs.applista.screens.home.HomeScreen
import com.crassvs.applista.screens.login.LoginScreen


@Composable
fun ListaNavigation(){
    val navController = rememberNavController()
    NavHost(navController = navController,
        startDestination = ListaScreens.ListaSplashScreen.name
    ){
        composable(ListaScreens.ListaSplashScreen.name) {
            ListaSplashScreen(navController = navController)
        }
        composable(ListaScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(ListaScreens.HomeScreen.name) {
            HomeScreen(navController = navController)
        }
    }
}