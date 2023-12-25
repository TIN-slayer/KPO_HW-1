package session.entity

data class SessionEntity(
    var movieId: Int,
    var startDateTime: String,
    var price: Int, // in wood
    var id: Int,
    var seatTable: MutableList<MutableList<Boolean>>,
    var hasStarted: Boolean
)
