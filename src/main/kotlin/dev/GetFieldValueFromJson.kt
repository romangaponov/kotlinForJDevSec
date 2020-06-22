package dev

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

var currentDataNode: DataNode? = DataNode()

fun main() {
    var counter: Int = 0
    val objectMapper = jacksonObjectMapper()
    val filePath = "src/main/resources/responses/suborder/"

    val readText = File("${filePath}newSub.json").readText(Charsets.UTF_8)
    val readValue = objectMapper.readValue<HashMap<String, Any>>(readText)


    readValue.forEach { (k, v) ->

        if (v is HashMap<*, *>) {
            currentDataNode?.addChild(
                DataNode(key = k, value = v, parent = currentDataNode)
            )
            println(currentDataNode)
            counter++
            if (counter == readValue.size) {
                searchValueInDataNode(currentDataNode)
            }
        } else {
            currentDataNode?.checked = true
        }
    }

}

fun searchValueInDataNode(dataNode: DataNode?) {

    val children = dataNode?.children

    children?.forEachIndexed { index, elem ->
        currentDataNode = elem
        if (elem?.value is HashMap<*, *>) {
            println("elem-> $elem")
            val hashMap = elem.value as HashMap<*, *>
            val first = hashMap.keys.first().toString()
            println("key - $first")
            hashMap.forEach { key, value ->
                currentDataNode?.addChild(
                    DataNode(key = key.toString(), value = value, parent = currentDataNode)
                )
            }
            println("second ->  $currentDataNode")
            if (index == children.size-1) {
                searchValueInDataNode(currentDataNode)
            }
        } else {
            println("third ->  $currentDataNode")
            currentDataNode?.checked = true
        }
    }
}

data class DataNode(
    var key: String? = null,
    var checked: Boolean = false,
    var value: Any? = null,
    var parent: DataNode? = null,
    var children: MutableList<DataNode?> = mutableListOf()
) {
    fun addChild(node: DataNode) {
        children.add(node)
    }

    override fun toString(): String {
        val map = mutableMapOf<String, Any?>(
            "key" to key,
            "checked" to checked,
            "value" to value

        )
        return jacksonObjectMapper().writeValueAsString(map)
    }
}
