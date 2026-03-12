package com.android.notes

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.android.login.view.LoginScreenRoute
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute

@Serializable
object SignUpRoute

@Serializable
object NotesRoute

@Composable
fun NotesNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = LoginRoute,
        modifier = modifier
    ) {
        composable<LoginRoute> {
            LoginScreenRoute(
                navigateToNotes = {
//                    navController.navigate(NotesRoute) {
//                        popUpTo(LoginRoute) { inclusive = true }
//                    }
                },
                navigateToSignUp = {
//                    navController.navigate(SignUpRoute)
                }
            )
        }
        
        composable<SignUpRoute> {
            // Placeholder for Sign Up Screen
        }
        
        composable<NotesRoute> {
            // Placeholder for Notes Screen
        }
    }
}
