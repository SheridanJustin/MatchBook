package week11.st221773.matchbook.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import week11.st221773.matchbook.model.Book

class BookRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    private fun userDoc() =
        db.collection("users").document(
            auth.currentUser?.uid
                ?: throw IllegalStateException("User not logged in")
        )

    // ---------- FAVORITES ----------

    suspend fun addFavorite(book: Book): Result<Unit> {
        return try {
            val docRef = userDoc()
                .collection("favorites")
                .document(book.title)   // using title as document id (ok for this app)

            val data = mapOf(
                "title" to book.title,
                "author" to book.author,
                "reason" to book.reason,
                "addedAt" to FieldValue.serverTimestamp(),
                // added to enrich book details
                "coverUrl" to book.coverUrl,
                "description" to book.description,
                "categories" to book.categories,
                "previewLink" to book.previewLink
            )

            docRef.set(data, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFavorite(title: String): Result<Unit> {
        return try {
            userDoc()
                .collection("favorites")
                .document(title)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavorites(): Result<List<Book>> {
        return try {
            val snapshot = userDoc()
                .collection("favorites")
                .get()
                .await()

            val list = snapshot.documents.map { doc ->
                Book(
                    title = doc.getString("title") ?: "",
                    author = doc.getString("author") ?: "",
                    reason = doc.getString("reason") ?: "",
                    coverUrl = doc.getString("coverUrl"),
                    description = doc.getString("description"),
                    categories = doc.get("categories") as? List<String>,
                    previewLink = doc.getString("previewLink")
                )
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------- TO BE READ ----------

    suspend fun addToBeRead(book: Book): Result<Unit> {
        return try {
            val docRef = userDoc()
                .collection("toBeRead")
                .document(book.title)

            val data = mapOf(
                "title" to book.title,
                "author" to book.author,
                "reason" to book.reason,
                "addedAt" to FieldValue.serverTimestamp(),
                "coverUrl" to book.coverUrl,
                "description" to book.description,
                "categories" to book.categories,
                "previewLink" to book.previewLink
            )

            docRef.set(data, SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFromToBeRead(title: String): Result<Unit> {
        return try {
            userDoc()
                .collection("toBeRead")
                .document(title)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getToBeRead(): Result<List<Book>> {
        return try {
            val snapshot = userDoc()
                .collection("toBeRead")
                .get()
                .await()

            val list = snapshot.documents.map { doc ->
                Book(
                    title = doc.getString("title") ?: "",
                    author = doc.getString("author") ?: "",
                    reason = doc.getString("reason") ?: "",
                    coverUrl = doc.getString("coverUrl"),
                    description = doc.getString("description"),
                    categories = doc.get("categories") as? List<String>,
                    previewLink = doc.getString("previewLink")
                )
            }
            Result.success(list)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
