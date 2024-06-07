package inter.storymemet

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.google.gson.Gson
import inter.storymemet.paging.StoryPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class Repository private constructor(
    private val userPreference: UserPreference
) {

    suspend fun registerUser(name: String, email: String, password: String): RegisterResponse {
        return withContext(Dispatchers.IO) {
            val response = ApiConfig.getApiService().register(name, email, password).execute()
            if (response.isSuccessful) {
                Log.i("info", response.message())
                return@withContext response.body()!!
            } else {
                val jsonInString = response.errorBody()?.string()
                return@withContext Gson().fromJson<RegisterResponse?>(jsonInString, RegisterResponse::class.java)
            }
        }
    }

    suspend fun loginUser(email: String, password: String): LoginResponse {
        return withContext(Dispatchers.IO) {
            val response = ApiConfig.getApiService().login(email, password).execute()
            if (response.isSuccessful) {
                return@withContext response.body()!!
            } else {
                val jsonInString = response.errorBody()?.string()
                return@withContext Gson().fromJson<LoginResponse?>(jsonInString, LoginResponse::class.java)
            }
        }
    }

    fun getStoryPaging(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(this, userPreference)
            }
        ).liveData
    }

//    suspend fun getStoriesRepo(token: String): StoryResponse {
//        return withContext(Dispatchers.IO) {
//            if (token.isEmpty()) {
//                return@withContext StoryResponse()
//            } else {
//                return@withContext ApiConfig.getApiServiceWithToken(token).getStories().execute().body()!!
//
//            }
//        }
//    }

    suspend fun getStoriesRepo(token: String, page: Int, size: Int): StoryResponse {
        return withContext(Dispatchers.IO) {
            if (token.isEmpty()) {
                return@withContext StoryResponse()
            } else {
                return@withContext ApiConfig.getApiServiceWithToken(token).getStories(page, size).execute().body()!!

            }
        }
    }

    suspend fun getAllStoriesWithLocationRepo(token: String): StoryResponse {
        return withContext(Dispatchers.IO) {
            if (token.isEmpty()) {
                return@withContext StoryResponse()
            } else {
                return@withContext ApiConfig.getApiServiceWithToken(token).getAllStoriesWithLocation().execute().body()!!

            }
        }
    }

    suspend fun uploadImage(imageFile: File, description: String, token: String): RegisterResponse {
        return withContext(Dispatchers.IO) {
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            val response = ApiConfig.getApiServiceWithToken(token).uploadImage(multipartBody, requestBody).execute()
            return@withContext if (response.isSuccessful) {
                response.body()!!
            } else {
                val jsonInString = response.errorBody()?.string()
                Gson().fromJson<RegisterResponse?>(jsonInString, RegisterResponse::class.java)
            }
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            userPreference: UserPreference
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(userPreference)
            }.also { instance = it }
    }
}