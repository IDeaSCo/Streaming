import groovy.sql.Sql

def db = [url:'jdbc:sqlserver://localhost:1433', user:'sa', password:'IDeaS123', driver:'com.microsoft.sqlserver.jdbc.SQLServerDriver']
def sql = Sql.newInstance(db.url, db.user, db.password, db.driver)
sql.execute """
             |IF OBJECT_ID('[000028].[dbo].sampleData', 'U') IS NOT NULL
             |DROP TABLE [000028].[dbo].sampleData;
             |
             |CREATE TABLE [000028].[dbo].sampleData ( [Col1] [nvarchar](100) ,[Col2] [nvarchar](100) ,[Col3] [nvarchar](100)
             |,[Col4]  [nvarchar](100) ,[Col5] [nvarchar](100) ,[Col6] [nvarchar](100)
             |,[Col7] [nvarchar](100) ,[Col8] [nvarchar](100) ,[Col9] [nvarchar](100) ,[Col10] [nvarchar](100) )
            """.stripMargin()


def howManyRecords = 5000
def howManyColumns = 1..10
def insertStatement =
    """
      |INSERT INTO [000028].[dbo].sampleData
      |     ([Col1],[Col2],[Col3],[Col4],[Col5],[Col6]
      |    ,[Col7],[Col8],[Col9],[Col10])
      |  VALUES
      |    (?,?,?,?,?,?,?,?,?,?)
    """.stripMargin()

sql.withBatch(howManyRecords, insertStatement) { ps ->
    howManyRecords.times {
        def rowData = howManyColumns.collect { "Col#$it".toString() }
        ps.addBatch(*rowData)
    }
}

println "Successfully Added ${sql.rows('SELECT * FROM [000028].[dbo].sampleData').size()} Rows"