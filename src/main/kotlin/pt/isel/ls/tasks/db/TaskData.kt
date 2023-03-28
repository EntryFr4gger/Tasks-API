package pt.isel.ls.tasks.db

import pt.isel.ls.tasks.db.modules.boards.BoardsDB
import pt.isel.ls.tasks.db.modules.cards.CardsDB
import pt.isel.ls.tasks.db.modules.lists.ListsDB
import pt.isel.ls.tasks.db.modules.users.UsersDB
import pt.isel.ls.tasks.db.transactionManager.TransactionManager
import java.sql.Connection

/**
 *
 * */
interface TaskData {

    /**
     *
     * */
    fun <R> execute(function: (TransactionManager) -> R): R

    /**
     *
     * */
    val users: UsersDB
    val boards: BoardsDB
    val lists: ListsDB
    val cards: CardsDB
}