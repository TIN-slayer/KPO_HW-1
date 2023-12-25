package session.dao

import session.entity.SessionEntity
import java.io.File
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import jsonDao.JsonDao
import session.dao.exception.SessionDaoNotFoundException

class JsonSessionDao : SessionDao {
    companion object {
        private const val JSON_PATH = "session.json"
    }

    private val sessionEntities: MutableList<SessionEntity>

    init {
        val gson = Gson()
        val file = File(JSON_PATH)
        if (file.length() == 0L) {
            file.writeText("")
            sessionEntities = mutableListOf()
        } else {
            val json = file.readText()
            val listType = object : TypeToken<MutableList<SessionEntity>>() {}.type
            sessionEntities = gson.fromJson(json, listType)
        }
    }

    override fun getMaxSessionId(): Int = (if (sessionEntities.size > 0) sessionEntities.maxOf { it.id } + 1 else 0)

    override fun saveSession(sessionEntity: SessionEntity) {
        sessionEntities.add(sessionEntity)
        JsonDao.encodeJson(JSON_PATH, sessionEntities)
    }

    override fun findSessionById(id: Int): SessionEntity {
        return sessionEntities.find { it.id == id } ?: throw SessionDaoNotFoundException("Сеанс с Id $id не найден")
    }

    override fun deleteSession(id: Int) {
        val len = sessionEntities.size
        sessionEntities.removeIf { it.id == id }
        if (len == sessionEntities.size) throw SessionDaoNotFoundException("Сеанс с Id $id не найден")
        JsonDao.encodeJson(JSON_PATH, sessionEntities)
    }

    override fun getSessions(): MutableList<SessionEntity> = sessionEntities
}
