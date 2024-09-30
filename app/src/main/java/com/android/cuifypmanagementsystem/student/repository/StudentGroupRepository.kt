package com.android.cuifypmanagementsystem.student.repository

import com.android.cuifypmanagementsystem.utils.FirebaseCollections.STUDENTS_REGISTRATIONS_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.android.cuifypmanagementsystem.student.datamodel.Group
import com.android.cuifypmanagementsystem.student.datamodel.Student
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.STUDENT_COLLECTION
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.STUDENT_GROUPS_COLLECTION
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class StudentGroupRepository  @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun isAlreadyInGroup(regNumber: String) {
        val docRef =
            firestore.collection(STUDENTS_REGISTRATIONS_COLLECTION).document(regNumber).get()
                .await()
    }

    suspend fun registerGrouping(group: Group): Result<Unit> = coroutineScope {
        val firestore = FirebaseFirestore.getInstance()
        val batchSizeLimit = 500

        try {
            // Step 1: Run all queries concurrently to get the student document references
            val studentRefs = group.groupMembers!!.map { memberReg ->
                async {
                    val querySnapshot: QuerySnapshot = firestore.collection(STUDENT_COLLECTION)
                        .whereEqualTo("registrationNumber", memberReg)
                        .limit(1) // Assuming registrationNumber is unique, limit to 1 result
                        .get()
                        .await()

                    // Return the student document reference if found
                    querySnapshot.documents.firstOrNull()?.reference
                }
            }.mapNotNull { it.await() } // Await all queries and filter out nulls

            // Step 2: Break into batches and update student documents in Firestore
            studentRefs.chunked(batchSizeLimit).forEach { chunk ->
                val batch = firestore.batch()

                chunk.forEach { studentRef ->
                    batch.update(studentRef, "group", group)
                }
                batch.commit().await()
            }

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }


    suspend fun registerGroup(group: Group): Result<Unit> {
        return try {
            val studentDocuments = group.groupMembers!!.mapNotNull { regNumber ->
                val querySnapshot = firestore.collection(STUDENT_COLLECTION)
                    .whereEqualTo("registrationNumber", regNumber)
                    .get()
                    .await()

                querySnapshot.documents.firstOrNull()
            }

            firestore.runTransaction { transaction ->
                val groupDocRef = firestore.collection(STUDENT_GROUPS_COLLECTION).document()
                val groupWithId = group.copy(firestoreId = groupDocRef.id)
                transaction.set(groupDocRef, groupWithId)

                studentDocuments.forEach { studentDoc ->
                    val studentDocRef = studentDoc.reference
                    transaction.update(studentDocRef, "group", groupDocRef.id)
                }
            }.await()

            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    suspend fun fetchMyGroup(uid: String?): Result<Group?> {
        return try {
            val userDocSnapshot = firestore.collection(STUDENT_COLLECTION).document(uid!!).get().await()
            val groupRef = userDocSnapshot.getString("group")

            if (groupRef == null) {
                Result.Success(null)
            } else {
                val groupDocSnapshot = firestore.collection(STUDENT_GROUPS_COLLECTION).document(groupRef).get().await()
                val group = groupDocSnapshot.toObject(Group::class.java)
                Result.Success(group)
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
    suspend fun getStudentById(id: String): Student? {
        return try {
            val studentDoc = firestore.collection(STUDENT_COLLECTION).document(id).get().await()
            if (studentDoc.exists()) {
                studentDoc.toObject(Student::class.java)
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }


    suspend fun fetchAllAvailableStudents(department: String): Result<List<Student>> {
        return try {
            val querySnapshot = firestore.collection(STUDENT_COLLECTION)
                .whereEqualTo("depart", department) // Filter by department
                .whereEqualTo("group", null) // Ensure group is null
                .get()
                .await()

            val students = querySnapshot.documents.mapNotNull { document ->
                try {
                    document.toObject(Student::class.java)
                } catch (e: Exception) {
                    null
                }
            }

            Result.Success(students)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }


}