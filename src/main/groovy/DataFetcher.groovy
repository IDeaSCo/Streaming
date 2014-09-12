import groovy.sql.Sql

def db = [url:'jdbc:sqlserver://localhost:1433', user:'sa', password:'', driver:'com.microsoft.sqlserver.jdbc.SQLServerDriver']
def sql = Sql.newInstance(db.url, db.user, db.password, db.driver)

def sendToG3(pmsDataRow) {
    println(pmsDataRow)
}

def pmsData = 'Select * from [000028].sampleData'
sql.eachRow(pmsData) { pmsDataRow ->
    sendToG3(pmsDataRow)
}




