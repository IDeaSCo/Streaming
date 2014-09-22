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
            properties.setProperty('selectMethod', 'cursor')
            sql = Sql.newInstance(dbConfig.url, properties, dbConfig.driver)
            this.dbName = dbName
        }
    }

    def fetchEachRow(Closure closure) {
        if(sql) {
            String pmsData = """
                    |SELECT * FROM [000028].[dbo].$dbName
                   """.stripMargin()
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








