import groovy.sql.Sql

def cli = new CliBuilder(usage:'client -d <databaseUrl> [--dbUser=someUser] [--dbPwd=somePwd] [--dbDriver=someDriver] [--dbName=someDbName] -n <recordCount>')
cli.with {
    d  args:1, argName: 'db',longOpt:'dburl','REQUIRED,DB Url', optionalArg:false
    _  args:1, argName: 'dbUser',longOpt:'dbUser','REQUIRED,DB User,Usage Eg: --dbUser=someUser', optionalArg:false
    _  args:1, argName: 'dbPwd',longOpt:'dbPwd','REQUIRED,DB Password, Usage Eg: --dbPwd=somePwd', optionalArg:false
    _  args:1, argName: 'dbDriver',longOpt:'dbDriver','REQUIRED,DB Driver,Usage Eg: --dbDriver=dbDriver', optionalArg:false
    _  args:1, argName: 'dbName', longOpt:'dbName', 'REQUIRED,DB Name,Usage Eg: --dbName=dbName', optionalArg:false
    n  args:1, argName: 'recordCount', longOpt:'recordCount','REQUIRED, Number of Records to create',optionalArg:false
}

def options = cli.parse(args)

if(!options) {
    return
}

PrintWriter console = new PrintWriter(System.out,true)

if(options.arguments()){
    console.println "Cannot understand ${options.arguments()}"
    cli.usage()
    return
}

def db = [url:options.d, user:options.dbUser, password:options.dbPwd, driver:options.dbDriver]
def dbName = options.dbName
def sql = Sql.newInstance(db.url, db.user, db.password, db.driver)
sql.execute """
             |IF OBJECT_ID('[000028].[dbo].$dbName', 'U') IS NOT NULL
             |DROP TABLE [000028].[dbo].$dbName;
             |
             |CREATE TABLE [000028].[dbo].$dbName ( [Col1] [nvarchar](100) ,[Col2] [nvarchar](100) ,[Col3] [nvarchar](100)
             |,[Col4]  [nvarchar](100) ,[Col5] [nvarchar](100) ,[Col6] [nvarchar](100)
             |,[Col7] [nvarchar](100) ,[Col8] [nvarchar](100) ,[Col9] [nvarchar](100) ,[Col10] [nvarchar](100)
             |,[Col11]  [nvarchar](100) ,[Col12] [nvarchar](100) ,[Col13] [nvarchar](100) ,[Col14] [nvarchar](100),[Col15] [nvarchar](100)
             |,[Col16]  [nvarchar](100) ,[Col17] [nvarchar](100) ,[Col18] [nvarchar](100) ,[Col19] [nvarchar](100),[Col20] [nvarchar](100)
             |,[Col21]  [nvarchar](100) ,[Col22] [nvarchar](100) ,[Col23] [nvarchar](100) ,[Col24] [nvarchar](100),[Col25] [nvarchar](100)
             |,[Col26]  [nvarchar](100) ,[Col27] [nvarchar](100) ,[Col28] [nvarchar](100) ,[Col29] [nvarchar](100),[Col30] [nvarchar](100)
             |,[Col31]  [nvarchar](100) ,[Col32] [nvarchar](100) ,[Col33] [nvarchar](100) ,[Col34] [nvarchar](100),[Col35] [nvarchar](100)
             |)
            """.stripMargin()


def howManyRecords = options.recordCount
def howManyColumns = 1..35
def insertStatement =
    """
      |INSERT INTO [000028].[dbo].$dbName
      |     ([Col1],[Col2],[Col3],[Col4],[Col5],[Col6],[Col7],[Col8],[Col9],[Col10]
      |     ,[Col11],[Col12],[Col13],[Col14],[Col15],[Col16],[Col17],[Col18],[Col19],[Col20]
      |     ,[Col21],[Col22],[Col23],[Col24],[Col25],[Col26],[Col27],[Col28],[Col29],[Col30]
      |     ,[Col31],[Col32],[Col33],[Col34],[Col35]
      |     )
      |  VALUES
      |    (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
    """.stripMargin()

sql.withBatch(howManyRecords.toInteger(), insertStatement) { ps ->
    howManyRecords.toInteger().times {
        def rowData = howManyColumns.collect { "Col#$it".toString() }
        ps.addBatch(*rowData)
    }
}
def selectQuery = """
                    |SELECT * FROM [000028].[dbo].$dbName
                   """.stripMargin()
println "Successfully Added ${sql.rows(selectQuery).size()} Rows"