package net.eduard.api.lib.database.annotations

@Target(AnnotationTarget.FIELD)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class TableData(
 val name: String = "",
 val size : Int = 100,
 val unique: Boolean = false,
 val nullable: Boolean = false,
 val defaultValue : String = "",
 val reference: Boolean = false,
 val primary : Boolean = false,
 val json : Boolean = false,
 val sqlType : String = ""
)