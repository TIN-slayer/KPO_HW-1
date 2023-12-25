package session.dao

import session.entity.SessionEntity

interface SessionDao {
    fun getMaxSessionId(): Int
    fun saveSession(sessionEntity:  SessionEntity)
    fun findSessionById(id: Int):  SessionEntity
    fun deleteSession(id: Int)
    fun getSessions(): MutableList<SessionEntity>
}