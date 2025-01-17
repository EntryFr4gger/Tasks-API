package pt.isel.ls.tasks.db.modules.users

import pt.isel.ls.tasks.db.errors.NotFoundException
import pt.isel.ls.tasks.db.modules.boards.BoardsDataPostgres.Companion.toBoard
import pt.isel.ls.tasks.db.transactionManager.TransactionManager
import pt.isel.ls.tasks.db.transactionManager.connection
import pt.isel.ls.tasks.domain.Board
import pt.isel.ls.tasks.domain.User
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class UsersDataPostgres : UsersDB {

    companion object {
        fun ResultSet.toUserWithPass() =
            User(getInt(1), getString(2), getString(3), getString(4))

        fun ResultSet.toUser() =
            User(getInt(1), getString(2), getString(3))
    }

    override fun createNewUser(conn: TransactionManager, name: String, email: String, password: String): Int {
        val obj = conn.connection().prepareStatement(
            "INSERT INTO users(name, email, password) VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        )
        obj.setString(1, name)
        obj.setString(2, email)
        obj.setString(3, password)

        if (obj.executeUpdate() == 0) throw SQLException("User Creation Failed with name:$name")

        obj.generatedKeys.also {
            return if (it.next()) it.getInt(1) else -1
        }
    }

    override fun loginUserInfo(conn: TransactionManager, email: String): User {
        val obj = conn.connection().prepareStatement(
            "SELECT * FROM users WHERE email = ?"
        )
        obj.setString(1, email)

        val res = obj.executeQuery()
        if (res.next()) {
            return res.toUserWithPass()
        } else {
            throw NotFoundException("Couldn't get User with email($email) Details")
        }
    }

    override fun getUserDetails(conn: TransactionManager, userId: Int): User {
        val obj = conn.connection().prepareStatement(
            "SELECT id, name, email FROM users WHERE id = ?"
        )
        obj.setInt(1, userId)

        val res = obj.executeQuery()
        if (res.next()) {
            return res.toUser()
        } else {
            throw NotFoundException("Couldn't get User($userId) Details")
        }
    }

    override fun getUserBoards(conn: TransactionManager, skip: Int, limit: Int, userId: Int): List<Board> {
        val obj = conn.connection().prepareStatement(
            "SELECT * FROM boards JOIN user_board ON id = board_id WHERE user_id = ? OFFSET ? LIMIT ?"
        )
        obj.setInt(1, userId)
        obj.setInt(2, skip)
        obj.setInt(3, limit)

        val res = obj.executeQuery()

        val boards = mutableListOf<Board>()
        while (res.next())
            boards.add(res.toBoard())

        return boards
    }

    override fun deleteBoardUsers(conn: TransactionManager, boardId: Int) {
        val res = conn.connection().prepareStatement(
            "DELETE FROM user_board WHERE board_id = ?"
        )
        res.setInt(1, boardId)
        if (res.executeUpdate() == 0) throw SQLException("UserBoard($boardId) delete was unsuccessful")
    }

    override fun getAllUsersNotInBoard(conn: TransactionManager, boardId: Int): List<User> {
        val prp = conn.connection().prepareStatement(
            "SELECT DISTINCT ON (id) id, name, email FROM users u JOIN user_board ub ON ub.user_id = u.id WHERE user_id NOT IN (SELECT DISTINCT user_id FROM user_board ub where ub.board_id = ?)"
        )

        prp.setInt(1, boardId)

        val res = prp.executeQuery()

        val users = mutableListOf<User>()
        while (res.next())
            users += res.toUser()

        return users
    }

    override fun hasUserEmail(conn: TransactionManager, email: String): Boolean {
        val res = conn.connection().prepareStatement(
            "SELECT * FROM users WHERE email = ?"
        )
        res.setString(1, email)

        return res.executeQuery().next()
    }

    override fun hasUser(conn: TransactionManager, userId: Int): Boolean {
        val res = conn.connection().prepareStatement(
            "SELECT * FROM users WHERE id = ?"
        )
        res.setInt(1, userId)

        return res.executeQuery().next()
    }

    override fun hasUserBoards(conn: TransactionManager, userId: Int): Boolean {
        val res = conn.connection().prepareStatement(
            "SELECT * FROM user_board WHERE user_id = ?"
        )
        res.setInt(1, userId)

        return res.executeQuery().next()
    }

    override fun validateResquestBoard(conn: TransactionManager, boardId: Int, requestId: Int): Boolean {
        val obj = conn.connection().prepareStatement(
            "SELECT * FROM user_board WHERE board_id = ?"
        )
        obj.setInt(1, boardId)

        val res = obj.executeQuery()
        while (res.next())
            if (res.getInt(1) == requestId) {
                return true
            }
        return false
    }

    override fun validateResquestCard(conn: TransactionManager, cardId: Int, requestId: Int): Boolean {
        val obj = conn.connection().prepareStatement(
            """
                SELECT user_id FROM user_board ub
                    JOIN cards c ON c.board_id = ub.board_id
                    WHERE id = ?
            """.trimIndent()
        )
        obj.setInt(1, cardId)

        val res = obj.executeQuery()
        while (res.next())
            if (res.getInt(1) == requestId) {
                return true
            }
        return false
    }

    override fun validateResquestList(conn: TransactionManager, listId: Int, requestId: Int): Boolean {
        val obj = conn.connection().prepareStatement(
            """
                SELECT user_id FROM user_board ub
                    JOIN lists l ON l.board_id = ub.board_id
                    WHERE id = ?
            """.trimIndent()
        )
        obj.setInt(1, listId)

        val res = obj.executeQuery()
        while (res.next())
            if (res.getInt(1) == requestId) {
                return true
            }
        return false
    }
}
