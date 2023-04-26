package pt.isel.ls.tasks.db.modules.tokens

import pt.isel.ls.tasks.db.transactionManager.TransactionManager

/**
 * Token Management.
 * */
interface TokensDB {
    /**
     * Create a new token.
     *
     * @param conn connection to a database.
     * @param token user token.
     * @param userId user unique identifier.
     *
     * @return user's unique token.
     * */
    fun createNewToken(conn: TransactionManager, token: String, userId: Int): String

    /**
     * Get the id of a user.
     *
     * @param conn connection to a database.
     * @param token user token.
     *
     * @return a User id.
     * */
    fun getUserID(conn: TransactionManager, token: String): Int

    /**
     * Verify if token exists.
     *
     * @param conn connection to a database.
     * @param token user unique token.
     *
     * @return true if exists or false if it does not exist.
     */
    fun hasToken(conn: TransactionManager, token: String): Boolean
}
