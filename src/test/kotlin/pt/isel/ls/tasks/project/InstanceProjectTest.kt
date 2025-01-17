package pt.isel.ls.tasks.project

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.http4k.client.JavaHttpClient
import org.http4k.server.Jetty
import org.http4k.server.asServer
import org.slf4j.LoggerFactory
import pt.isel.ls.tasks.PORT
import pt.isel.ls.tasks.api.TasksAPI
import pt.isel.ls.tasks.db.TasksDataMem
import pt.isel.ls.tasks.db.dataStorage.TasksDataStorage
import pt.isel.ls.tasks.services.TaskServices
import kotlin.test.BeforeTest

abstract class InstanceProjectTest {
    companion object {
        @OptIn(ExperimentalSerializationApi::class)
        val format = Json { explicitNulls = false }
        const val path = "http://localhost:$PORT/"
        val send = JavaHttpClient()
        val logger = LoggerFactory.getLogger("Tasks API")
        val db = TasksDataMem(TasksDataStorage())
        val services =
            TaskServices(db) /*TaskServices(TasksDataPostgres("JDBC_DATABASE_URL"))*/
        val app = TasksAPI.invoke(services, logger)

        val jettyServer = app.asServer(Jetty(PORT)).start()
    }

    @BeforeTest
    fun rebootDataMem() {
        db.reset()
    }
}
