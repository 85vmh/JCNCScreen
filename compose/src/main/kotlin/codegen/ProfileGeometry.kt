package codegen

class ProfileGeometry {

    fun getProfile(): List<String> {
        val result = mutableListOf<String>()
        result.add("G0 X1 Z3")
        result.add("G1 X10 Z-30")
        result.add("G0 X15.0")
        return result
    }

    val subroutineNumber = 100

    val hasPockets = false
}