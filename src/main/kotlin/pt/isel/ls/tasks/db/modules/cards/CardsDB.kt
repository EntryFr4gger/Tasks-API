package pt.isel.ls.tasks.db.modules.cards

import kotlinx.datetime.LocalDate
import pt.isel.ls.tasks.db.errors.NotFoundException
import pt.isel.ls.tasks.db.transactionManager.TransactionManager
import pt.isel.ls.tasks.domain.Card

/**
 * Card Management
 * */
interface CardsDB {

    /**
     * Creates a new card in a list.
     * Cix should be the next available slot
     *
     * @param conn connection to a database.
     * @param name the task name.
     * @param description the task description.
     * @param dueDate the task's conclusion date.
     * @param boardId board unique identifier.
     * @param listId list unique identifier.
     *
     * @return card unique identifier.
     * */
    fun createNewCard(
        conn: TransactionManager,
        name: String,
        description: String,
        cix: Int,
        dueDate: LocalDate?,
        boardId: Int,
        listId: Int?
    ): Int

    /**
     * Get the detailed information of a card.
     *
     * @param conn connection to a database.
     * @param cardId card unique identifier.
     *
     * @return a Card.
     *
     * @throws NotFoundException couldn't get Card Details
     * */
    fun getCardDetails(conn: TransactionManager, cardId: Int): Card

    /**
     * Moves a card to a list.
     *
     * @param conn connection to a database.
     * @param listId list unique identifier.
     * @param cardId card unique identifier.
     *
     * @return a card id.
     * */
    fun moveCard(conn: TransactionManager, listId: Int?, cardId: Int): Boolean

    /**
     * Delete a card.
     *
     * @param conn connection to a database.
     * @param cardId card unique identifier.
     *
     * @return true if it has deleted or false otherwise.
     * */
    fun deleteCard(conn: TransactionManager, cardId: Int): Boolean

    /**
     * Verify if the card exists.
     *
     * @param conn connection to a database.
     * @param cardId card unique identifier.
     *
     * @return true if exists or false if it doesn't exist.
     * */
    fun hasCard(conn: TransactionManager, cardId: Int): Boolean

    /**
     * Updates card with new index.
     *
     * @param conn connection to a database.
     * @param cardId card unique identifier.
     * @param cix desired index.
     *
     * @return true if executed correctly, false otherwise.
     * */
    fun organizeCardSeq(conn: TransactionManager, cardId: Int, cix: Int)
}
