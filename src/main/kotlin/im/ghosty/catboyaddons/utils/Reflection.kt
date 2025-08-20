package im.ghosty.catboyaddons.utils

object Reflection {

    fun callMethod(obj: Any, vararg methodName: String) {
        val method = obj.javaClass.declaredMethods.find { methodName.contains(it.name) } ?: throw NoSuchMethodException("Method not found")
        method.isAccessible = true
        method.invoke(obj)
    }

}