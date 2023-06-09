package kr.ac.kumoh.newrun.domain.model

data class RecordListItem (
    val route: List<LatLng>,
    val dDay: String,
    val date: String,
    val distance: String,
    val velocity: String,
    val time: String
)



