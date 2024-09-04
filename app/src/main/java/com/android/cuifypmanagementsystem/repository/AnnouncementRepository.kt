package com.android.cuifypmanagementsystem.repository

import com.android.cuifypmanagementsystem.datamodels.Announcement
import com.android.cuifypmanagementsystem.utils.FirebaseCollections.ANNOUNCEMENT_COLLECTION
import com.android.cuifypmanagementsystem.utils.Result
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AnnouncementRepository @Inject constructor(
    private val firestore: FirebaseFirestore) {

    suspend fun publishAnnouncement(announcement: Announcement) : Result<Unit> {
        return try {
            val docRef = firestore.collection(ANNOUNCEMENT_COLLECTION).document()
            docRef.set(announcement.copy(id = docRef.id)).await()
            Result.Success(Unit)
        } catch (e:Exception) {
            Result.Failure(e)
        }
    }
}