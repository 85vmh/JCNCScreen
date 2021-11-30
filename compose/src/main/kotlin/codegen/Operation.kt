package codegen

interface Operation {
    fun getComment(): List<String>
    fun getOperationCode(): List<String>
}
