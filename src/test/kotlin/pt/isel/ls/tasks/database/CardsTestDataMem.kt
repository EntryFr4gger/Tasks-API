package pt.isel.ls.tasks.database

import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.Test
import pt.isel.ls.tasks.db.TasksDataMem
import pt.isel.ls.tasks.db.dataStorage.TasksDataStorage
import pt.isel.ls.tasks.db.errors.NotFoundException
import pt.isel.ls.tasks.db.modules.cards.CardsDataMem
import pt.isel.ls.tasks.domain.Card
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CardsTestDataMem {
    private val storage = TasksDataStorage()
    private val source = TasksDataMem(storage)
    private val cards = CardsDataMem(storage)

    @Test
    fun `Creates a new card in a list`() {
        source.run { conn ->
            val card = Card(
                1,
                "Study",
                "study for success",
                LocalDate(2023, 3, 21),
                null,
                1,
                1
            )
            val id =
                cards.createNewCard(
                    conn,
                    card.name,
                    card.description,
                    card.dueDate,
                    card.boardId,
                    card.listId
                )
            assertEquals(
                card.copy(id = id),
                storage.cards[id]
            )
        }
    }

    @Test
    fun `Get detail of a card`() {
        source.run { conn ->
            assertEquals(
                Card(2, "Entrega 1", "Entrega inicial do autorouter", LocalDate(2023, 4, 3), 1, 1, 2),
                cards.getCardDetails(conn, 2)
            )
        }
    }

    @Test
    fun `Throws an error for a nonexistent card`() {
        source.run { conn ->
            assertFailsWith<NotFoundException> {
                cards.getCardDetails(conn, Int.MAX_VALUE)
            }
        }
    }

    @Test
    fun `Move card from a list`() {
        source.run { conn ->
            assertTrue { cards.moveCard(conn, 3, 2) }
        }
    }

    @Test
    fun `Delete card from a list`() {
        source.run { conn ->
            cards.deleteCard(conn, 1)
            assertFalse { cards.hasCard(conn, 1) }
        }
    }

    @Test
    fun `Confirm that the card already exist`() {
        source.run { conn ->
            assertTrue { cards.hasCard(conn, 1) }
        }
    }

    @Test
    fun `Confirm that the card do not exist`() {
        source.run { conn ->
            assertFalse { cards.hasCard(conn, Int.MAX_VALUE) }
        }
    }

    @Test
    fun `Organize cards`() {
        source.run { conn ->
            assertTrue(cards.organizeCardSeq(conn, 3, 2))
            assertEquals(storage.cards[3]!!.cix, 2)
        }
    }

    @Test
    fun `Organize cards fails`() {
        source.run { conn ->
            assertFalse(cards.organizeCardSeq(conn, Int.MAX_VALUE, 2))
        }
    }
}