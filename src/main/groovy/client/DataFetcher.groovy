package client

import groovy.sql.Sql

class DataFetcher {
    private final Sql sql
    private final dbName

    public DataFetcher(dbConfig = [:], dbName = "") {
        if(dbConfig) {
            def properties = new Properties()
            properties.setProperty('user', dbConfig.user)
            properties.setProperty('password', dbConfig.password)
            //properties.setProperty('selectMethod', 'cursor')
            properties.setProperty('responseBuffering','adaptive')
            sql = Sql.newInstance(dbConfig.url, properties, dbConfig.driver)
           // sql.connection.autoCommit = false
            this.dbName = dbName
        }
    }

    def fetchEachRow(Closure closure) {
        if(sql) {
            String pmsData = """
                    |SELECT * FROM [000028].[dbo].$dbName
                   """.stripMargin()
               // sql.withStatement{ stmt ->  stmt.fetchSize = 150}
                sql.eachRow(pmsData) { row ->
                    closure(row)
                }
        }else {
            10.times {
                closure("Hello for $it times...")
            }
        }
    }
}








