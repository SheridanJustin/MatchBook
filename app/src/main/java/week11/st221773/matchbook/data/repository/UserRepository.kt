package week11.st221773.matchbook.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    /**
     * Creates /users/{uid} document if it does not already exist.
     * You can call this right after signUp or on first login.
     */
    suspend fun createUserDocumentIfNeeded(email: String): Result<Unit> {
        val uid = auth.currentUser?.uid
            ?: return Result.failure(IllegalStateException("User not logged in"))

        return try {
            val docRef = db.collection("users").document(uid)
            val snapshot = docRef.get().await()

            if (!snapshot.exists()) {
                val data = mapOf(
                    "email" to email,
                    "createdAt" to FieldValue.serverTimestamp()
                )
                docRef.set(data).await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
