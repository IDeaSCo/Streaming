package client

import groovy.sql.Sql

class DataFetcherForOracle {
    private final Sql sql
    private final dbName

    public DataFetcherForOracle(dbConfig = [:], dbName = "") {
        if(dbConfig) {
            def properties = new Properties()
            properties.setProperty('user', dbConfig.user)
            properties.setProperty('password', dbConfig.password)
            //response buffering has no impact in Oracle
            // properties.setProperty('responseBuffering','adaptive')
            sql = Sql.newInstance(dbConfig.url, properties, dbConfig.driver)
            this.dbName = dbName
        }
    }

    def fetchEachRowOracle(Closure closure) {
        if(sql) {
            String pmsData = """
                    |SELECT * FROM $dbName
                   """.stripMargin()
            //sql.withStatement{ stmt ->  stmt.fetchSize = 1}
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








