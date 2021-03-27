package net.eduard.api.lib.score


class DisplayBoardSlide(
    var durationTicks: Long = 1L,
    var versions: MutableList<String> = mutableListOf(),
    override var position: Int = 1
) : DisplayBoardLine {

    constructor(durationTicks: Long=1, position: Int=15, vararg versions: String) : this(
        durationTicks,
        versions.toMutableList(),
        position
    )

    @Transient
    private var lastModification: Long = 0

    @Transient
    var current = 0


    override var text: String
        get() = versions[current]
        set(value) {
            versions.add(value)
        }


    override fun check() {
        if (versions.isEmpty()) {
            return
        }
        val duration = durationTicks * 50L
        val now = System.currentTimeMillis()
        if (lastModification + duration < now) {
            current++
            if (current >= versions.size) {
                current = 0
            }
            lastModification = now
        }

    }



}