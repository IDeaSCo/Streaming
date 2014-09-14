package client

import groovy.sql.Sql

class DataFetcher {
    private final Sql sql
    private final String pmsData = 'Select * from [000028].sampleData'

    public DataFetcher(dbConfig = [:]) {
        if(dbConfig) {
            sql = Sql.newInstance(dbConfig.url, dbConfig.user, dbConfig.password, dbConfig.driver)
        }
    }

    def fetchEachRow(Closure closure) {
        if(sql) {
            sql.eachRow(pmsData) { pmsDataRow ->
                closure(pmsDataRow)
            }
        } else {
            10.times {
                closure("Hello for $it times...")
            }
        }
    }
}








