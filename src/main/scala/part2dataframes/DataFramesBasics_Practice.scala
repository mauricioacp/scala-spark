package part2dataframes

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{DoubleType, LongType, StringType, StructField, StructType}

object DataFramesBasics_Practice extends App {

  //creating spark session
  //entry point for reading and writting data frames
  val spark = SparkSession.builder()
    .appName("DataFrames Basics")
    .config("spark.master", "local") //you can add any configs you want for each config one value
    .getOrCreate()

  //reading a DF
  val firstDF = spark.read
    .format("json")
    //this option isn't good for production we must define our own schema
    .option("inferSchema", "true")
    .load("src/main/resources/data/cars.json")

  //showing a DF
  //  firstDF.show()
  //  firstDF.printSchema()

  //get rows
  firstDF.take(10).foreach(println)

  // spark types
  //LongType si a singleton that spark uses internally to describe the type of the data inside the DF
  val longType = LongType

  // schema
  //  val carsSchema=StructType(
  //    Array(
  //      StructField("Name",StringType,nullable = true)//nullable true is the default
  //
  //    )
  //  )

  val carsSchema = StructType(Array(
    StructField("Name", StringType),
    StructField("Miles_per_Gallon", DoubleType),
    StructField("Cylinders", LongType),
    StructField("Displacement", DoubleType),
    StructField("Horsepower", LongType),
    StructField("Weight_in_lbs", LongType),
    StructField("Acceleration", DoubleType),
    StructField("Year", StringType),
    StructField("Origin", StringType)
  ))

  //obatin a schema
  //will adopt the structure from above
  val carsDFSchema = firstDF.schema

  //read a DF with your schema
  val carsDFWithSchema = spark.read
    .format("json")
    .schema(carsDFSchema)
    .load("src/main/resources/data/cars.json")

  //create rows by hand
  val myRow = Row("chevrolet chevelle malibu", 18.0, 8L, 307.0, 130L, 3504L, 12.0, "1970-01-01", "USA")

  //create DF from tuples
  val cars = Seq(
    ("chevrolet chevelle malibu", 18.0, 8L, 307.0, 130L, 3504L, 12.0, "1970-01-01", "USA"),
    ("buick skylark 320", 15.0, 8L, 350.0, 165L, 3693L, 11.5, "1970-01-01", "USA"),
    ("plymouth satellite", 18.0, 8L, 318.0, 150L, 3436L, 11.0, "1970-01-01", "USA"),
    ("amc rebel sst", 16.0, 8L, 304.0, 150L, 3433L, 12.0, "1970-01-01", "USA"),
    ("ford torino", 17.0, 8L, 302.0, 140L, 3449L, 10.5, "1970-01-01", "USA"),
    ("ford galaxie 500", 15.0, 8L, 429.0, 198L, 4341L, 10.0, "1970-01-01", "USA"),
    ("chevrolet impala", 14.0, 8L, 454.0, 220L, 4354L, 9.0, "1970-01-01", "USA"),
    ("plymouth fury iii", 14.0, 8L, 440.0, 215L, 4312L, 8.5, "1970-01-01", "USA"),
    ("pontiac catalina", 14.0, 8L, 455.0, 225L, 4425L, 10.0, "1970-01-01", "USA"),
    ("amc ambassador dpl", 15.0, 8L, 390.0, 190L, 3850L, 8.5, "1970-01-01", "USA")
  )

  val manualCarsDF = spark.createDataFrame(cars) //schema auto-inferred

  //note: DFs have schemas, rows do not

  //create DFs with implicits

  import spark.implicits._

  val manualCarsDFWithImplicits = cars.toDF("Name", "MPG", "Cylinders", "Displacement", "HP", "Weight", "Acceleration", "Year", "CountryOrigin")

  //  manualCarsDF.printSchema()
  //  manualCarsDFWithImplicits.printSchema()

  /**
    * Exercise:
    * 1) Create a manual DF describing smartphones
    *   - make
    *   - model
    *   - screen dimension
    *   - camera megapixels
    *
    * 2) Read another file from the data/ folder, e.g. movies.json
    *   - print its schema
    *   - count the number of rows, call count()
    */

  val smartPhones = Seq(
    ("Xiaomi", "RedmiNote9", 5.6, 12),
    ("Huawei", "Lite9", 6.2, 24),
    ("Oppo", "A92020", 10.2, 26),
    ("Apple", "IphonePro12", 5.2, 18),
    ("Alcatel", "Nova2020", 5.5, 15),
  )

  val smartPhoneDFWithImplicits = smartPhones.toDF("Make", "Model", "ScreenDimensions", "MegaPixels")

  smartPhoneDFWithImplicits.show()

  val moviesDF = spark.read
    .format("json")
    .option("inferSchema", "true")
    .load("src/main/resources/data/movies.json")

  moviesDF.printSchema()
  println(s"The Movies DF has ${moviesDF.count()} rows")


}
