package pt.isel.ls.tasks

import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory
import pt.isel.ls.tasks.api.TasksAPI
import pt.isel.ls.tasks.db.TasksDataMem
import pt.isel.ls.tasks.db.dataStorage.TasksDataStorage
import pt.isel.ls.tasks.services.TaskServices

const val PORT = 9000

fun main() {
    val logger = LoggerFactory.getLogger("Tasks API")
    val services = TaskServices(TasksDataMem(TasksDataStorage())) // TaskServices(TasksDataPostgres("JDBC_DATABASE_URL"))
    val api = TasksAPI(services)

    val app = api
        .getRoutes()
        .withFilter(Logger(logger))

    val jettyServer = app.asServer(Jetty(PORT)).start()
    logger.info("server started listening")

    readln()
    jettyServer.stop()

    logger.info("leaving Main")
}