package pt.isel.ls.tasks.api.routers.boards.models

import jdk.jfr.Description
import kotlinx.serialization.Serializable

@Serializable
data class Board(val id:Int, val name: String, val description: String)
