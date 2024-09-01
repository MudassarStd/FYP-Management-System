package com.android.cuifypmanagementsystem.repository


import com.android.cuifypmanagementsystem.datamodels.LoggedInUserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.auth.EmailAuthProvider
import javax.inject.Inject

class UserAuthRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun userLogin(email: String, password: String): Result<LoggedInUserData> {
        return try {
            // Attempt to sign in with email and password
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return Result.Failure(IllegalArgumentException("User ID is null"))

            // Fetch user role
            val roleResult = fetchUserRole(userId)
            when (roleResult) {
                is Result.Success -> Result.Success(LoggedInUserData(userId, roleResult.data))
                is Result.Failure -> Result.Failure(roleResult.exception)
                else -> Result.Failure(IllegalStateException("Unknown error while fetching role"))
            }

        } catch (e: Exception) {
            // Handle login failures
            Result.Failure(e)
        }
    }

    private suspend fun fetchUserRole(userId: String): Result<String> {
        return try {
            // Retrieve the role document
            val role = firestore.collection("userRoles").document(userId).get().await().getString("role")
            role?.let { Result.Success(it) } ?: Result.Failure(NoSuchElementException("Role not found"))
        } catch (e: Exception) {
            // Handle document retrieval failures
            Result.Failure(Exception("Failed to fetch user role: ${e.message}", e))
        }
    }

    // Function to send a password reset email
    suspend fun sendPasswordResetEmail(email: String) : Result<Void?>{
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Result.Success(null)
        } catch (e: Exception) {
            Result.Failure(e)
//            throw Exception("Failed to send password reset email: ${e.message}")
        }
    }


    suspend fun userLogout() {
        try {
            firebaseAuth.signOut()  // Sign out the current user
            // Optionally, handle post-signout actions here
        } catch (e: Exception) {
            // Handle possible exceptions here
            e.printStackTrace()
        }
    }

    suspend fun userChangePassword(oldPassword: String, newPassword: String): Result<Void?> {
        val user = firebaseAuth.currentUser
        return if (user != null) {
            val email = user.email ?: return Result.Failure(Exception("User email not found"))
            val credential = EmailAuthProvider.getCredential(email, oldPassword)

            try {
                // Reauthenticate the user
                user.reauthenticate(credential).await()

                // Update the password
                user.updatePassword(newPassword).await()

                Result.Success(null)
            } catch (e: Exception) {
                Result.Failure(e)
            }
        } else {
            Result.Failure(Exception("User not found"))
        }
    }


}
