package movie.service

import movie.entity.MovieEntity

interface MovieService {

    fun addMovie(title: String, duration: Int, genre: String, id: Int = -1)

    fun findMovieById(id: Int): MovieEntity

    fun deleteMovie(id: Int)

    fun editMovie(id: Int, title: String = "", duration: Int = -1, genre: String = "")

    fun printMovies()

}
