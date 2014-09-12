package client

import groovy.sql.Sql

class DataFetcher {
    private final Sql sql
    private final String pmsData = 'Select * from [000028].sampleData'

    DataFetcher(dbConfig) {
        sql = Sql.newInstance(dbConfig.url, dbConfig.user, dbConfig.password, dbConfig.driver)
    }

    def forEachRowFetch(Closure closure) {
        sql.eachRow(pmsData) { pmsDataRow ->
            closure(pmsDataRow)
        }
    }
}








