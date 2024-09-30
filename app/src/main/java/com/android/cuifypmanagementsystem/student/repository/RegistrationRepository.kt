package com.android.cuifypmanagementsystem.student.repository

import com.android.cuifypmanagementsystem.auth.model.StudentRegistration
import com.android.cuifypmanagementsystem.student.datamodel.Student
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.STUDENTS_REGISTRATIONS_COLLECTION
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.STUDENT_COLLECTION
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.USER_ROLES_COLLECTION
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegistrationRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun registerStudent(studentRegistration: StudentRegistration, student: Student): Result<Unit> {
        return try {

            if (isAlreadyRegistered(student.registrationNumber)) {
                return Result.Failure(Exception("Student already registered"))
            }

            val authResult = firebaseAuth.createUserWithEmailAndPassword(studentRegistration.email, studentRegistration.password).await()
            val uid = authResult.user?.uid ?: throw Exception("User ID is null")

            firestore.runTransaction { transaction ->
                val registrationDocRef = firestore.collection(STUDENTS_REGISTRATIONS_COLLECTION).document(student.registrationNumber)
                val studentDocRef = firestore.collection(STUDENT_COLLECTION).document(uid)
                val roleDocRef = firestore.collection(USER_ROLES_COLLECTION).document(uid)
                // Check if registration number already exists within the transaction
                val registrationDocSnapshot = transaction.get(registrationDocRef)
                if (registrationDocSnapshot.exists()) {
                    throw Exception("Student already registered")
                }

                transaction.set(studentDocRef, student)
                transaction.set(roleDocRef, mapOf("role" to "student"))
                transaction.set(registrationDocRef, mapOf("student" to uid))

            }.await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    private suspend fun isAlreadyRegistered(regNumber: String): Boolean {
        return try {
            val docSnapshot = firestore.collection(STUDENTS_REGISTRATIONS_COLLECTION).document(regNumber).get().await()
            docSnapshot.exists()
        } catch (e: Exception) {
            false
        }
    }
}