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

if(options.arguments()){
    println "Cannot understand ${options.arguments()}"
    cli.usage()
    return
}

def db = [url:options.d, user:options.dbUser, password:options.dbPwd, driver:options.dbDriver]
def dbName = options.dbName
//def sql = Sql.newInstance("jdbc:oracle:thin:@localhost:1522/oracle", "SCOTT", "IDeaS123", "oracle.jdbc.OracleDriver")
def sql = Sql.newInstance(db.url, db.user, db.password, db.driver)
//sql.execute """
//             | DROP TABLE  oracle.$dbName
//            """.stripMargin()

sql.execute """
             |CREATE TABLE $dbName ( Col1 varchar (100) ,Col2 varchar (100) ,Col3 varchar (100)
             |,Col4  varchar (100) ,Col5 varchar (100) ,Col6 varchar (100)
             |,Col7 varchar (100) ,Col8 varchar (100) ,Col9 varchar (100) ,Col10 varchar (100)
             |,Col11  varchar (100) ,Col12 varchar (100) ,Col13 varchar (100) ,Col14 varchar (100),Col15 varchar (100)
             |,Col16  varchar (100) ,Col17 varchar (100) ,Col18 varchar (100) ,Col19 varchar (100),Col20 varchar (100)
             |,Col21  varchar (100) ,Col22 varchar (100) ,Col23 varchar (100) ,Col24 varchar (100),Col25 varchar (100)
             |,Col26  varchar (100) ,Col27 varchar (100) ,Col28 varchar (100) ,Col29 varchar (100),Col30 varchar (100)
             |,Col31  varchar (100) ,Col32 varchar (100) ,Col33 varchar (100) ,Col34 varchar (100),Col35 varchar (100) )
            """.stripMargin()

def howManyRecords = Integer.parseInt(options.recordCount)
def howManyColumns = 1..35
def insertStatement =
  """
    |INSERT INTO $dbName
    |     (Col1,Col2,Col3,Col4,Col5,Col6,Col7,Col8,Col9,Col10
    |     ,Col11,Col12,Col13,Col14,Col15,Col16,Col17,Col18,Col19,Col20
    |     ,Col21,Col22,Col23,Col24,Col25,Col26,Col27,Col28,Col29,Col30
    |     ,Col31,Col32,Col33,Col34,Col35
    |     )
    |  VALUES
    |    (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)
  """.stripMargin()

def batchSize = 1000
def numberOfBatches = howManyRecords/batchSize

def start = System.currentTimeMillis()
numberOfBatches.times {
    sql.withBatch(batchSize, insertStatement) { ps ->
        batchSize.times {
            def rowData = howManyColumns.collect { "Col#$it".toString() }
            ps.addBatch(*rowData)
        }
    }
    println "Inserted $batchSize records...remaining ${howManyRecords - batchSize * it}"
}

def timeTaken = System.currentTimeMillis() - start
println "Time Taken : ${timeTaken / 1000} secs"
