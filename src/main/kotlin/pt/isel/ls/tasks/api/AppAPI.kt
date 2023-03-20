package pt.isel.ls.tasks.api

import kotlinx.datetime.Clock
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.routes
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory
import pt.isel.ls.tasks.api.routers.boards.BoardsRouter
import pt.isel.ls.tasks.api.routers.users.models.UsersRouter


private val logger = LoggerFactory.getLogger("Tasks API")

fun getDate(request: Request): Response {
    return Response(Status.OK)
        .header("content-type", "text/plain")
        .body(Clock.System.now().toString())
}

fun logRequest(request: Request) {
    logger.info(
        "incoming request: method={}, uri={}, content-type={} accept={}",
        request.method,
        request.uri,
        request.header("content-type"),
        request.header("accept")
    )
}

/*

O que é preferivel, criar um objeto para cada situação Resposta e request ou tentar adaptar o JSON encoder e decoder
A maneira como o Router está programado, é ok?
*/
fun main() {

    val app = routes(
        UsersRouter.UsersRoutes(),
        BoardsRouter.BoardsRoutes()
    )

    val jettyServer = app.asServer(Jetty(9000)).start()
    logger.info("server started listening")

    readln()
    jettyServer.stop()

    logger.info("leaving Main")
}
