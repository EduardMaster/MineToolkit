package net.eduard.api.lib.database.api

enum class SQLEngineType(private val sqlLink : String, val driverLocation : String) {
    MYSQL("jdbc:mysql://%host:%port/%database?username=%user&password=%password&autoReconnect=true"
    , "com.mysql.jdbc.Driver"),
    SQLITE("jdbc:sqlite:%database",
    "org.sqlite.JDBC");

    fun getUrl(host : String , port : Int, username : String, password : String ,  database : String ): String {
        return sqlLink.replace("%host" , host)
                .replace("%port" ,"$port" )
                .replace("%user" , username)
                .replace("%password", password)
                .replace("%database" , database)
    }
}