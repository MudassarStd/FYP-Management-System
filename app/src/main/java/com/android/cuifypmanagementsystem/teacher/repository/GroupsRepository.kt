package com.android.cuifypmanagementsystem.teacher.repository

import com.android.cuifypmanagementsystem.datamodels.Group
import com.android.cuifypmanagementsystem.datamodels.GroupDisplayModel
import com.android.cuifypmanagementsystem.datamodels.GroupRequest
import com.android.cuifypmanagementsystem.student.datamodel.Project
import com.android.cuifypmanagementsystem.teacher.utils.GroupDataType
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.STUDENT_GROUPS_COLLECTION
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.TEACHER_COLLECTION
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
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
                    val groups = teacherDocRef.get().await().get("groups") as? List<Group>

                    groupDisplayModels = groups?.flatMap { group ->
                        group.groups?.map { groupId ->
                            val groupData = firestore.collection(STUDENT_GROUPS_COLLECTION).document(groupId).get().await()

                            val groupMembers = groupData.get("groupMembers") as? List<String> ?: emptyList()
                            val project = groupData.get("project") as? Project
                            GroupDisplayModel(
                                firestoreId = groupId,
                                groupMembers = groupMembers,
                                batch = group.batch,
                                project = project
                            )
                        } ?: emptyList()
                    } ?: emptyList()
                }

                GroupDataType.REQUESTS -> {

                    val groupRequests = teacherDocRef.get().await().get("groupRequests") as? List<GroupRequest>

                    groupDisplayModels = groupRequests?.flatMap { group ->
                        group.requests?.map { groupId ->
                            val groupData = firestore.collection(STUDENT_GROUPS_COLLECTION).document(groupId).get().await()

                            val groupMembers = groupData.get("groupMembers") as? List<String> ?: emptyList()
                            val project = groupData.get("project") as? Project
                            GroupDisplayModel(
                                firestoreId = groupId,
                                groupMembers = groupMembers,
                                batch = group.batch,
                                project = project
                            )
                        } ?: emptyList()
                    } ?: emptyList()
                }
            }
            Result.Success(groupDisplayModels)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }


}