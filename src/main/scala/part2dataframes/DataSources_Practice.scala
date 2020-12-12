package part2dataframes

import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.sql.types.{DateType, DoubleType, LongType, StringType, StructField, StructType}

object DataSources_Practice extends App {

  val spark=SparkSession.builder()
    .appName("Data sources and formats")
    .config("spark.master","local")
    .getOrCreate()

  val carsSchema = StructType(Array(
    StructField("Name", StringType),
    StructField("Miles_per_Gallon", DoubleType),
    StructField("Cylinders", LongType),
    StructField("Displacement", DoubleType),
    StructField("Horsepower", LongType),
    StructField("Weight_in_lbs", LongType),
    StructField("Acceleration", DoubleType),
    StructField("Year", DateType),
    StructField("Origin", StringType)
  ))
  /*
  * Reading a DF:
  * -format
  * -schema or inferSchema =true (optional)
  * -zero or more options
  * -path
  * */
  val carsDF = spark.read
    .format("json")
    .schema(carsSchema) //enforce a schema
    .option("mode","failFast") //others: dropMalformed (will ignore faulty rows), permissive (default)
    .option("path","src/main/resources/data/cars.json")
    .load()

  carsDF.show()

  //alternative reading
  //equivalent of passing many options
  //compute the options dynamically at runtime
  val carsDFWithOptionMap=spark.read
    .format("json")
    .options(Map(
      "mode"->"failFast",
      "path"->"src/main/resources/data/cars.json",
      "inferSchema"->"true"
    ))
    .load()

  /*
  * Write DFs
  * -format
  * -save mode = overwrite, append, ignore, errorIfExists
  * -path
  * -zero or more options
  */

  carsDF.write
    .format("json")
    .mode(SaveMode.Overwrite)
    .save("src/main/resources/data/cars_dupe.json")

  // JSON Flags

  spark.read
    .schema(carsSchema)
    .option("dateFormat","yyyy-MM-dd") // couple with schema; if sparks fails parsing it will put null
    .option("allowSingleQuotes","true")
    //uncompressed -> default; others are -> bzip2, gzip, lz4 (7zip), snappy, deflate
    .option("compression","uncompressed")//databases are usually big and compression can save quite a lot of space
    //instead of format json -> json("path")
    .json("src/main/resources/data/cars.json")

  //CSV Flags  //"its extremely hard to parse CSV from the first try"
  val stocksSchema = StructType(Array(
    StructField("symbol",StringType),
    //Attempt to parse de date in this way -> MMM dd yyyy
    StructField("date",DateType),
    StructField("price",DoubleType)
  ))

  spark.read
    .format("csv")
    .schema(stocksSchema)
    .option("dateFormat","MMM dd yyyy")
    //csv may or may not have a header ...
    .option("header","true")
    //separator
    .option("sep",",")
    //this is important there is no notion of null in csv
    .option("nullValue"," ")
    .csv("src/main/resources/data/stocks.csv")


  // Parquet
  // (open source compressed binary storage
  // format optimized for fast reading of columns)
  //it is very predictable you dont need so many options as csv
  carsDF.write
    .mode(SaveMode.Overwrite)
    .save("src/main/resources/data/cars.parquet") //parquet format is the default format in spark


  //Text files
  spark.read.text("src/main/resources/data/sampleTextFile.txt").show()

  // Reading from a remote DB
  val driver = "org.postgresql.Driver"
  val url = "jdbc:postgresql://localhost:5432/rtjvm"
  val user = "docker"
  val password = "docker"

  val employeesDF=spark.read
    .format("jdbc")
    .option("driver",driver)
    .option("url",url)
    .option("user",user)
    .option("password",password)
    .option("dbtable", "public.employees")
    .load()

  /**
    * Exercise: read the movies DF, then write it as
    * - tab-separated values file
    * - snappy Parquet
    * - table "public.movies" in the Postgres DB
    */

  val moviesDF = spark.read.json("src/main/resources/data/movies.json")

  // TSV
  moviesDF.write
    .format("csv")
    .option("header", "true")
    .option("sep", "\t")
    .save("src/main/resources/data/movies.csv")

  // Parquet
  moviesDF.write.save("src/main/resources/data/movies.parquet")

  // save to DF
  moviesDF.write
    .format("jdbc")
    .option("driver", driver)
    .option("url", url)
    .option("user", user)
    .option("password", password)
    .option("dbtable", "public.movies")
    .save()



}
