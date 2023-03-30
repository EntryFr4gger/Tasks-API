package pt.isel.ls.tasks.api.routers.users.models

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoDTO(val id: Int, val name: String, val email: String)