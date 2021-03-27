package net.eduard.api.lib.score



class DisplayBoardSlide : DisplayBoardLine {
    @Transient
    private var lastModification: Long = 0

    @Transient
    var current = 0
    var durationTicks = 1L;
    var versions = mutableListOf<String>()
    override var text: String
        get() = versions[current]
        set(value) {

        }


    fun check() {
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

    override var position: Int
        get() = current
        set(value) {}

}