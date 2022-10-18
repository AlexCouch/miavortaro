package net.miavortaro.application.dao

import User
import WordEntry
import net.miavortaro.application.model.WordEntryCache
import kotlinx.coroutines.selects.select
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.notEqSubQuery
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.Closeable

interface DAOFacade : Closeable{
    fun init()

    fun createWord(word: String, definition: String): String

    fun queryWord(word: String, definition: String): Boolean

    fun getWord(word: String): WordEntryCache

    fun searchWords(word: String): List<String>

    fun top(amount: Int): List<String>

    fun all(): List<String>

    fun deleteWord(word: String)

    fun queryUser(username: String): Boolean
    fun authUser(user: User): Boolean
    fun createUser(user: User): Boolean
    fun deleteUser(user: User): Boolean

    fun clear()
}

class DAOFacadeDatabase(
    val db: Database = Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")
): DAOFacade {
    override fun init(){
        transaction(db){
            SchemaUtils.create(WordEntries)
            SchemaUtils.create(UserEntries)
        }
    }

    override fun queryWord(word: String, definition: String): Boolean = transaction(db){
        WordEntries.select {
            exists(WordEntries.select{
                (WordEntries.word eq word) and (WordEntries.definition eq definition)
            })
        }.any()
    }

    override fun createWord(word: String, definition: String): String = transaction(db){
        if(!queryWord(word, definition)){
            WordEntries.insert {
                it[WordEntries.word] = word
                it[WordEntries.definition] = definition
            } get WordEntries.word
        }else{
            word
        }
    }

    override fun getWord(word: String): WordEntryCache = transaction(db){
        val row = WordEntries.select {
            WordEntries.word.eq(word)
        }.first()
        WordEntryCache(word, row[WordEntries.definition])
    }

    override fun top(amount: Int): List<String> = transaction(db) {
        WordEntries.alias("w2").let {
            WordEntries.join(it, JoinType.LEFT, WordEntries.word, it[WordEntries.definition])
                .slice(WordEntries.word, it[WordEntries.word].count())
                .selectAll()
                .groupBy(WordEntries.word)
                .orderBy(it[WordEntries.word].count(), SortOrder.ASC)
                .limit(amount)
                .map { entry -> entry[WordEntries.word] }
        }
    }

    override fun searchWords(word: String): List<String> = transaction(db){
        WordEntries.alias("w2").let {
            WordEntries.join(it, JoinType.LEFT, WordEntries.word, it[WordEntries.definition])
                .slice(WordEntries.word, it[WordEntries.word].count())
                .selectAll()
                .groupBy(WordEntries.word)
                .orderBy(it[WordEntries.word].count(), SortOrder.ASC)
                .filter{
                    it[WordEntries.word].startsWith(word)
                }
                .map { entry -> entry[WordEntries.word] }
        }
    }

    override fun all(): List<String> = transaction(db){
        WordEntries.alias("w2").let {
            WordEntries.join(it, JoinType.LEFT, WordEntries.word, it[WordEntries.definition])
                .slice(WordEntries.word, it[WordEntries.word].count())
                .selectAll()
                .groupBy(WordEntries.word)
                .orderBy(it[WordEntries.word].count(), SortOrder.ASC)
                .map { entry -> entry[WordEntries.word] }
        }
    }

    override fun deleteWord(word: String): Unit = transaction(db){
        WordEntries.deleteWhere { WordEntries.word.eq(word) }
    }

    override fun queryUser(username: String) = transaction(db) {
        UserEntries.select {
            exists(UserEntries.select{
                UserEntries.username eq username
            })
        }.any()
    }

    override fun authUser(user: User): Boolean = transaction(db) {
        UserEntries.select {
            (UserEntries.username eq user.username) and (UserEntries.passwordHashed eq user.passwordHashed)
        }.any()
    }

    override fun createUser(user: User): Boolean = transaction(db) {
        if(queryUser(user.username)){
            false
        }else{
            UserEntries.insert {
                it[UserEntries.username] = user.username
                it[UserEntries.passwordHashed] = user.passwordHashed
            }
            true
        }
    }

    override fun deleteUser(user: User): Boolean = transaction(db) {
        if(!queryUser(user.username)){
            false
        }else{
            UserEntries.deleteWhere { UserEntries.username.eq(user.username) }
            true
        }
    }

    override fun clear(): Unit = transaction(db){
        WordEntries.deleteAll()
        UserEntries.deleteAll()
    }

    override fun close() {
    }
}