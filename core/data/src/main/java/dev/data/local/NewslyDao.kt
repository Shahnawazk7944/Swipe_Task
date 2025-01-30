//package dev.data.local
//
//import androidx.room.Dao
//import androidx.room.Delete
//import androidx.room.Insert
//import androidx.room.OnConflictStrategy
//import androidx.room.Query
//import com.example.newsly.domain.model.News
//import kotlinx.coroutines.flow.Flow
//
//@Dao
//interface NewslyDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun bookmarkNews(news: News)
//
//    @Delete
//    suspend fun deleteNews(news: News)
//
//    @Query("SELECT * FROM News")
//    fun getBookmarkedNews(): Flow<List<News>>
//
//    @Query("SELECT * FROM News WHERE url=:url")
//    suspend fun isNewsBookmarked(url: String): News?
//}