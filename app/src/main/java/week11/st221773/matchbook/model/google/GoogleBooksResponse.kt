package week11.st221773.matchbook.model.google

data class GoogleBooksResponse(
    val items: List<GoogleBookItem>?
)

data class GoogleBookItem(
    val volumeInfo: GoogleVolumeInfo?
)

data class GoogleVolumeInfo(
    val title: String?,
    val authors: List<String>?,
    val description: String?,
    val categories: List<String>?,
    val imageLinks: GoogleImageLinks?,
    val previewLink: String?
)

data class GoogleImageLinks(
    val thumbnail: String?,
    val smallThumbnail: String?
)
