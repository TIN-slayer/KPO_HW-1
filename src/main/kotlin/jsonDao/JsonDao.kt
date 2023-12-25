package jsonDao

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import movie.dao.JsonMovieDao
import movie.entity.MovieEntity
import java.io.File
import java.nio.charset.Charset

// Типо статического класса, поэтому не сделал для него интерфейс
class JsonDao {
    companion object {
        //private val gson = Gson()
        private val gsonPretty = GsonBuilder().setPrettyPrinting().create()

        // Не работает из-за проблем с кастом
//        fun <T> decodeJson(path: String): MutableList<T>{
//            val file = File(path)
//            if (file.length() == 0L) {
//                file.writeText("")
//                return mutableListOf()
//            } else {
//                val json = file.readText()
//                val listType = object : TypeToken<MutableList<T>>() {}.type
//                return gson.fromJson(json, listType)
//            }
//        }

        fun <T> encodeJson(path: String, list: MutableList<T>) {
            val file = File(path)
            val json: String = gsonPretty.toJson(list)
            file.writeText(json, Charset.defaultCharset())
        }
    }
}