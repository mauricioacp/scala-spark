package part1recap

import scala.concurrent.Future
import scala.util.{Failure, Success}

class ScalaRecap_practice extends App {

  //values and variables
  val aBoolean: Boolean = false;

  //expressions

  val anIfExpression = if (2 > 3) "bigger" else "smaller"

  //instructions vs expressions
  val theUnit = println("Hello, scala") // ->returns the unit type // Unit = void

  //functions
  def myFunction(x: Int) = 42

  //OOP
  class Animal

  //  class Dog extends Animal

  trait Carnivore {
    def eat(animal: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(animal: Animal): Unit = println("Crunch!")
  }

  //singleton pattern
  object MySingleton

  // companions
  object Carnivore

  //generics
  trait Mylist[+A] //co variant

  //methos notation
  val x = 1 + 2
  val y = 1.+(2)

  //Functional  Programming //anonymous function
  val incrementer: (Int) => Int = x => x + 1
  val incremented = incrementer(42) //43

  //map, flatmap, filter (higher order functions) they can receive functions as arguments
  val processedList = List(1, 2, 3).map(incrementer)

  //Pattern matching
  val unknown: Any = 45

  val ordinal = unknown match {

    case 1 => "first"
    case 2 => "second"
    case _ => "unknown"

  }
  //try-catch
  try {
    throw new NullPointerException
  } catch {
    //catch is done via pattern matching
    case _: NullPointerException => "some returned value"
    case _ => "something else"
  }

  //Future
  //they abstract the way of computations on separate threads

  import scala.concurrent.ExecutionContext.Implicits.global

  val aFuture = Future {
    // some expensive computation, runs on another thread
    42
  }

  aFuture.onComplete {
    case Success(meaningOfLife) => println(s"Ive found the $meaningOfLife")
    case Failure(ex) => println(s"I have failed:$ex")

  }

  //partial functions

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 43
    case 8 => 56
    case _ => 999
  }

  //Implicits


  //Auto injection by the compiler
  //without implicit notation the compiler will figure out that in the list
  //of arguments for the implicit method the implicit val will be applied
  def methodWithImplicitArgument(implicit x: Int) = x + 43

  implicit val implicitInt = 67

  val implicitCall = methodWithImplicitArgument

  //implicit conversion - implicit defs
  //case classes light weight data structures  that have a bunch of utility
  //methods already implemented by the compiler
  case class Person(name: String) {
    def greet = println(s"sHi mi name is $name")
  }

  implicit def fromStringToPerson(name: String): Person = Person(name)

  //the compiler allows to use the method directly on the string
  //the string is automatically converted to a Person by calling the
  "Bob".greet //=> fromStringToPerson("Bob").greet

  //implicit conversion - implicit classes
  implicit class Dog(name: String) {
    def bark = println("Bark!")
  }

  //the compiler will automatically convert lassie into a dog
  "Lassie".bark

  //Implicit classes are almost preferable than implicit defs

  /*
  * Alcance de las llamadas implicitas
  * Local scope (implicit class)
  * imported scope (future example)
  * companion objects of the types involved in the method call (like lists (x,x,x,x).sorted)
  * */


}
