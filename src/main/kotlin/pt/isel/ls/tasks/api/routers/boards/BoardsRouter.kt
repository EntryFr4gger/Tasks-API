package pt.isel.ls.tasks.api.routers.boards

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.routing.routes
import pt.isel.ls.tasks.api.logRequest
import pt.isel.ls.tasks.api.routers.IRouter
import pt.isel.ls.tasks.api.routers.boards.models.*
import pt.isel.ls.tasks.services.Services

class BoardsRouter(private val services: Services) : IRouter {
    companion object{
        fun routes(services: Services) = BoardsRouter(services).routes
    }
    override val routes = routes(
        "board" bind Method.POST to ::postBoard,
        "board/{board_id}/users/{user_id}" bind Method.POST to ::postUserToBoard,
        "boards/{board_id}" bind Method.GET to ::getBoard,
        "users/{user_id}/boards" bind Method.GET to ::getUserBoards

    )

    
    private fun getUserBoards(request: Request): Response {
        logRequest(request)
        val user_id = request.path("user_id")?.toIntOrNull() ?: return Response(Status.BAD_REQUEST).body("ID not valid")
        val boards = services.getBoards(user_id)
        return Response(Status.OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(UserBoardsDTO(boards)))
    }


    private fun getBoard(request: Request): Response {
        logRequest(request)
        val board_id = request.path("board_id")?.toIntOrNull() ?: return Response(Status.BAD_REQUEST).body("ID not valid")
        val board = services.getBoard(board_id)
        return Response(Status.OK)
            .header("content-type", "application/json")
            .body(Json.encodeToString(BoardDTO(board)))
    }

    //trocar o user ID para o corpo?
    private fun postUserToBoard(request: Request): Response {
        logRequest(request)
        val board_id = request.path("board_id")?.toIntOrNull() ?: return Response(Status.BAD_REQUEST).body("ID not valid")
        val user_id = request.path("user_id")?.toIntOrNull() ?: return Response(Status.BAD_REQUEST).body("ID not valid")
        val response = services.addUserToBoard(user_id,board_id) //can be removed but can be useful
        return Response(Status.OK)
    }


    private fun postBoard(request: Request): Response {
        logRequest(request)
        val boardInfo = Json.decodeFromString<CreateBoardDTO>(request.bodyString())
        val boardID = services.createBoard(boardInfo.name, boardInfo.description)
        return Response(Status.CREATED)
            .header("content-type", "application/json")
            .body(Json.encodeToString(BoardIdDTO(boardID)))
    }
}