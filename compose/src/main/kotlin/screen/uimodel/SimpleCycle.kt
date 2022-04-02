package screen.uimodel

enum class SimpleCycle(val displayableString: String, val imgName: String? = null) {
    Turning("Turning", "od.png"),
    Boring("Boring", "id.png"),
    Facing("Facing", "facing.png"),
    Threading("Threading", "threading.png"),
    Drilling("Drilling\nReaming"),
    KeySlot("Slotting", "slotting.png"),
}