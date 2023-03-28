package pt.isel.ls.tasks.db.modules.boards

import pt.isel.ls.tasks.db.transactionManager.TransactionManager
import pt.isel.ls.tasks.db.transactionManager.connection
import pt.isel.ls.tasks.domain.Board
import java.sql.ResultSet
import java.sql.Statement

class BoardsDataPostgres: BoardsDB {

    companion object{
        fun ResultSet.toBoard() =
            Board(getInt(1), getString(2), getString(3))

    }

    override fun createNewBoard(conn: TransactionManager, name: String, description: String): Int {
        val res = conn.connection().prepareStatement(
            "INSERT INTO boards(name, description) VALUES (?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )
        res.setString(1, name)
        res.setString(2, description)

        if(res.executeUpdate() == 0) throw Error("Board Create Failed(sql)")

        res.generatedKeys.also {
            return if (it.next()) it.getInt(1) else -1
        }
    }

    override fun addUserToBoard(conn: TransactionManager, userId: Int, boardId: Int): Int {
        val res = conn.connection().prepareStatement(
            "INSERT INTO user_board(user_id, board_id) VALUES (?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )
        res.setInt(1, userId)
        res.setInt(2, boardId)

        if(res.executeUpdate() == 0) throw Error("Couldnt add user to board(sql)")

        res.generatedKeys.also {
            return if (it.next()) it.getInt(1) else -1
        }
    }

    override fun getUserBoards(conn: TransactionManager, userId: Int): List<Board> {
        val obj = conn.connection().prepareStatement(
            "SELECT * FROM boards JOIN user_board ON id = board_id WHERE user_id = ?",
        )
        obj.setInt(1, userId)

        val res = obj.executeQuery()

        val boards = mutableListOf<Board>()
        while (res.next())
            boards.add(res.toBoard())

        return boards
    }

    override fun getBoardDetails(conn: TransactionManager, boardId: Int): Board {
        val prp = conn.connection().prepareStatement(
            "SELECT * FROM boards WHERE id = ?",
        )
        prp.setInt(1, boardId)

        val res = prp.executeQuery()
        if (res.next())
            return res.toBoard()
        else
            throw Error("No board")
    }
}