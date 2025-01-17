package pt.isel.ls.tasks.db.modules.boards

import pt.isel.ls.tasks.db.dataStorage.TasksDataStorage
import pt.isel.ls.tasks.db.errors.NotFoundException
import pt.isel.ls.tasks.db.transactionManager.TransactionManager
import pt.isel.ls.tasks.domain.Board
import pt.isel.ls.tasks.domain.Card
import pt.isel.ls.tasks.domain.User
import java.sql.SQLException
import kotlin.collections.List
import kotlin.collections.set
import pt.isel.ls.tasks.domain.List as _List

class BoardsDataMem(private val source: TasksDataStorage) : BoardsDB {

    init {
        source.boards[1] = Board(1, "ISEL", "Cenas do 4 semestre do isel")
        source.boards[2] = Board(2, "Compras", "Ida ao supermercado")
        source.boards[3] = Board(3, "Limpeza", "O que falta limpar cá em casa")
        source.nextBoardId.addAndGet(3)

        source.userBoard[1] = listOf(1, 2, 3)
        source.userBoard[2] = listOf(1, 2)
        source.userBoard[3] = listOf(3)
    }

    override fun createNewBoard(conn: TransactionManager, name: String, description: String) =
        source.nextBoardId.getAndIncrement().let { id ->
            source.boards[id] = Board(id, name, description)
            id
        }

    override fun addUserToBoard(conn: TransactionManager, userId: Int, boardId: Int): Boolean {
        val ub = source.userBoard[userId]
        source.userBoard[userId] = (ub ?: emptyList()) + boardId
        return !source.userBoard[userId].isNullOrEmpty()
    }

    override fun getBoardDetails(conn: TransactionManager, boardId: Int): Board =
        source.boards[boardId] ?: throw NotFoundException("Couldn't get Board($boardId) Details")

    override fun getAllLists(conn: TransactionManager, boardId: Int, skip: Int, limit: Int): List<_List> {
        val filteredLists = source.lists.toList().mapNotNull {
            it.second.takeIf { list ->
                list.boardId == boardId
            }
        }
        val startIndex = minOf(skip, filteredLists.size)
        val endIndex = minOf(startIndex + limit, filteredLists.size)
        return filteredLists.subList(startIndex, endIndex)
    }

    override fun getAllCards(
        conn: TransactionManager,
        boardId: Int,
        skip: Int,
        limit: Int,
        onlyReturnArchived: Boolean
    ): List<Card> {
        val filteredCards = source.cards.toList().mapNotNull {
            it.second.takeIf { card ->
                card.boardId == boardId && if (onlyReturnArchived) card.listId == null else true
            }
        }
        val startIndex = minOf(skip, filteredCards.size)
        val endIndex = minOf(startIndex + limit, filteredCards.size)
        return filteredCards.subList(startIndex, endIndex)
    }

    override fun getBoardUsers(conn: TransactionManager, boardId: Int, skip: Int, limit: Int): List<User> {
        val filteredUsers = source.userBoard.filter { hash -> hash.value.contains(boardId) }
            .map {
                val user = source.users[it.key] ?: throw NotFoundException("User not found with userId:${it.key}")
                User(user.id, user.name, user.email)
            }
        val startIndex = minOf(skip, filteredUsers.size)
        val endIndex = minOf(startIndex + limit, filteredUsers.size)
        return filteredUsers.subList(startIndex, endIndex)
    }

    override fun searchBoards(conn: TransactionManager, skip: Int, limit: Int, name: String, userId: Int): List<Board> {
        val filteredBoards = source.boards.values.filter { board -> board.name.contains(name, ignoreCase = true) }
        val startIndex = minOf(skip, filteredBoards.size)
        val endIndex = minOf(startIndex + limit, filteredBoards.size)
        return filteredBoards.subList(startIndex, endIndex)
    }

    override fun deleteBoard(conn: TransactionManager, boardId: Int): Boolean {
        val res = source.boards.remove(boardId)
        return res != null || throw SQLException("Board($boardId) delete was unsuccessful")
    }

    override fun hasBoardName(conn: TransactionManager, name: String) =
        source.boards.values.find { it.name == name } != null

    override fun hasBoard(conn: TransactionManager, boardId: Int) =
        source.boards[boardId] != null
}
