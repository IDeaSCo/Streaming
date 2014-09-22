package client

import groovy.sql.GroovyResultSet
import groovy.sql.Sql

class DataFetcher {
    private final Sql sql
    private final dbName



    public DataFetcher(dbConfig = [:], dbName = "") {
        if(dbConfig) {
            sql = Sql.newInstance(dbConfig.url, dbConfig.user, dbConfig.password, dbConfig.driver)
            this.dbName = dbName
        }
    }

    def fetchEachRow(Closure closure) {
        if(sql) {

            String pmsData = """
                    |SELECT * FROM [000028].[dbo].$dbName
                   """.stripMargin()
            println "Successfully Fetched ${sql.rows(pmsData).size()} Rows"
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








