package com.android.cuifypmanagementsystem.teacher.repository

import android.util.Log
import com.android.cuifypmanagementsystem.datamodels.Group
import com.android.cuifypmanagementsystem.datamodels.GroupRequest
import com.android.cuifypmanagementsystem.datamodels.Teacher
import com.android.cuifypmanagementsystem.student.datamodel.Project
import com.android.cuifypmanagementsystem.student.datamodel.Student
import com.android.cuifypmanagementsystem.teacher.datamodel.GroupDisplayModel
import com.android.cuifypmanagementsystem.teacher.utils.GroupDataType
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.STUDENT_COLLECTION
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.STUDENT_GROUPS_COLLECTION
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.TEACHER_COLLECTION
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupsRepository @Inject constructor(
    private val firestore : FirebaseFirestore
) {
    suspend fun fetchGroups(
        teacherId: String,
        groupDataType: GroupDataType
    ): Result<List<GroupDisplayModel>> {
        return try {

            val teacherDocRef = firestore.collection(TEACHER_COLLECTION).document(teacherId)

            val groupDisplayModels: List<GroupDisplayModel>

            when (groupDataType) {
                GroupDataType.GROUPS -> {
                    // Fetch groups as a list of maps
                    val groups = teacherDocRef.get().await().get("groups") as? List<Map<String, Any>>

                    groupDisplayModels = groups?.flatMap { groupMap ->
                        // Extract the batch and group IDs
                        val batch = groupMap["batch"] as? String
                        val groupIds = groupMap["groups"] as? List<String>

                        // For each group ID, fetch group data and create GroupDisplayModel
                        groupIds?.map { groupId ->
                            val groupData = firestore.collection(STUDENT_GROUPS_COLLECTION).document(groupId).get().await()

                            val projectMap = groupData.get("project") as? Map<String, Any>
                            val project = projectMap?.let {
                                Project(
                                    title = it["title"] as? String ?: "",
                                    description = it["description"] as? String ?: "",
                                    techStack = it["techStack"] as? List<String>
                                )
                            }

                            GroupDisplayModel(
                                firestoreId = groupId,
                                groupMembers = listOf(),  // Assuming you'll fetch group members elsewhere
                                batch = batch,  // Use the batch extracted from the map
                                project = project
                            )
                        } ?: emptyList()
                    } ?: emptyList()
                }




                GroupDataType.REQUESTS -> {
                    val groupRequests = teacherDocRef.get().await().get("groupRequests") as? List<Map<String, Any>>

                    Log.d("GroupDataTesting", "Repository: Group Requests:  $groupRequests")

                    groupDisplayModels = groupRequests?.flatMap { requestMap ->
                        val batch = requestMap["batch"] as? String // Extract batch
                        val groupIds = requestMap["requests"] as? List<String> // Extract the list of group IDs

                        groupIds?.map { groupId ->
                            val groupData = firestore.collection(STUDENT_GROUPS_COLLECTION).document(groupId).get().await()
                            Log.d("GroupDataTesting", "Repository: Group Data By ID:  $groupData")

//                            val project = groupData.get("project") as? Project

                            val projectMap = groupData.get("project") as? Map<String, Any>
                            val project = projectMap?.let {
                                Project(
                                    title = it["title"] as? String ?: "",
                                    description = it["description"] as? String ?: "",
                                    techStack = it["techStack"] as? List<String>
                                )
                            }

                            val groupMembers = groupData.get("groupMembers") as? List<String>

                            GroupDisplayModel(
                                firestoreId = groupId,
                                groupMembers = groupMembers,  // Assuming you're fetching this elsewhere
                                batch = batch,  // Use batch from the request map
                                project = project
                            )
                        } ?: emptyList()
                    } ?: emptyList()
                }

            }
            Result.Success(groupDisplayModels)
        } catch (e: Exception) {
            throw e
            Result.Failure(e)
        }
    }

    suspend fun getGroupMemberNames(memberIds : List<String>) {
        val names = mutableListOf<String>()
        try {
            memberIds.forEach { id ->
                val student  = firestore.collection(STUDENT_COLLECTION).document(id).get().await()
                if (student.exists()) {
                    val studentObj = student.toObject(Student::class.java)
                    names.add(studentObj!!.name)
                }
            }
        } catch (e : Exception) {

        }
    }


    suspend fun addGroupToSupervision(
        groupId: String,
        teacherId: String,
        batch: String
    ): Boolean {
        if (groupId.isEmpty() || teacherId.isEmpty() || batch.isEmpty()) {
            return false
        }

        val teacherDocRef = firestore.collection("teachers").document(teacherId)

        return try {
            val document = teacherDocRef.get().await()
            if (!document.exists()) {
                return false
            }

            val teacherFromFirestore = document.toObject(Teacher::class.java)
                ?: return false

            val existingGroupEntry = teacherFromFirestore.groups?.find { it.batch == batch }

            if (existingGroupEntry != null) {
                existingGroupEntry.groups = existingGroupEntry.groups!!.toMutableList().apply { add(groupId) }
            } else {
                val newGroupEntry = Group(batch = batch, groups = listOf(groupId))
                teacherFromFirestore.groups = (teacherFromFirestore.groups?.toMutableList()
                    ?: mutableListOf()).apply { add(newGroupEntry) }
            }

            teacherDocRef.update("groups", teacherFromFirestore.groups).await()

            removeGroupRequest(groupId, teacherId, batch)

            true
        } catch (e: Exception) {
            throw e
            Log.e("AddGroupToSupervision", "Error handling teacherId: $teacherId", e)
            false
        }
    }

    suspend fun removeGroupRequest(groupId: String, teacherId: String, batch: String): Boolean {
        if (groupId.isEmpty() || teacherId.isEmpty() || batch.isEmpty()) {
            return false
        }

        val teacherDocRef = firestore.collection("teachers").document(teacherId)

        return try {
            val document = teacherDocRef.get().await()
            if (!document.exists()) {
                return false
            }

            val teacher = document.toObject(Teacher::class.java) ?: return false

            val existingGroupRequest = teacher.groupRequests?.find { it.batch == batch }

            if (existingGroupRequest != null) {
                val updatedRequests = existingGroupRequest.requests?.toMutableList()?.apply {
                    remove(groupId)  // Remove the groupId from the list
                }

                if (updatedRequests != null) {
                    existingGroupRequest.requests = updatedRequests
                }

                // Update the teacher's groupRequests
                teacherDocRef.update("groupRequests", teacher.groupRequests).await()
            }

            true
        } catch (e: Exception) {
            Log.e("RemoveGroupRequest", "Error removing groupId: $groupId from requests", e)
            false
        }
    }


}