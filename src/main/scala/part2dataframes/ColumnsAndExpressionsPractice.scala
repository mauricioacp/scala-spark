package part2dataframes

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, column, expr}

object ColumnsAndExpressionsPractice extends App {

  val spark=SparkSession.builder()
    .appName("DF Columns and Expressions")
    .config("spark.master","local")
    .getOrCreate()

  val carsDF=spark.read
    .option("inferSchema","true")
    .json("src/main/resources/data/cars.json")

  //Columns
  val firstColumn=carsDF.col("Name")
  // selecting (projecting)
  val carsNameDF=carsDF.select(firstColumn)

  //various select methods
  import spark.implicits._
  carsDF.select(
    carsDF.col("Name"),
    col("Acceleration"),
    column("Weight_in_lbs"),
    'Year, //scala symbol, auto-converted to column
    $"HorsePower", //fancier interpolated string, returns a columns object
    expr("Origin") //EXPRESSION
  )

  // select with plain column names
  carsDF.select("Name","Year")

}
