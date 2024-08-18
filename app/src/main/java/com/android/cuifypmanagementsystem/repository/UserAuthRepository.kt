package com.android.cuifypmanagementsystem.repository


import com.android.cuifypmanagementsystem.datamodels.UserAuthData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.android.cuifypmanagementsystem.utils.Result

class UserAuthRepository(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun userLogin(email: String, password: String): Result<UserAuthData> {
        return try {
            // Attempt to sign in with email and password
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: return Result.Failure(IllegalArgumentException("User ID is null"))

            // Fetch user role
            val roleResult = fetchUserRole(userId)
            when (roleResult) {
                is Result.Success -> Result.Success(UserAuthData(userId, roleResult.data))
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
}
