package inter.storymemet

data class UserModel(
    val email: String,
    val token: String,
    val isLogin: Boolean = false
)