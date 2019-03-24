package example
import scala.collection.mutable

object Draft1 {}

object ConceptSample {

  case class Person(name: String, age: Int)

  val personNameOrdering: Ordering[Person] = new Ordering[Person] {
    override def compare(x: Person, y: Person): Int =
      // implicitly[Ordering[String]].compare(x.name, y.name)
      x.name.compareTo(y.name)
  }

  val personAgeOrdering: Ordering[Person] = new Ordering[Person] {
    override def compare(x: Person, y: Person): Int = x.age.compareTo(y.age)
  }

  val alice = Person("Alice", 21)
  val bob = Person("Bob", 26)
  val chris = Person("Chris", 20)
  val people = Seq(alice, bob, chris)
  val peopleStringPair = Seq((alice, "3"), (alice, "2"), (alice, "1"))

  // scala> personWithMaxAge
  // res1: Person = Person(Bob,26)
  val personWithMaxAge: Person = people.max(personAgeOrdering)
  // or
  object WithAgeOrderingImplicit {
    implicit lazy val ordering: Ordering[Person] = personAgeOrdering
    val personWithMaxAge2 = people.max

    // already defined in scala.math.Ordering
    implicit def orderingTuple2[A, B](implicit ordA: Ordering[A], ordB: Ordering[B]): Ordering[(A, B)] =
      new Ordering[(A, B)] {
        override def compare(x: (A, B), y: (A, B)): Int = {
          val res = ordA.compare(x._1, y._1)
          if (res != 0) res
          else ordB.compare(x._2, y._2)
        }
      }

    // scala> WithAgeOrderingImplicit.peopleStringMax
    // res2: (Person, String) = (Person(Alice,21),3)
    val peopleStringMax = peopleStringPair.max
  }

  object WithOrdered {
    case class Person2(name: String, age: Int) extends Ordered[Person2] {
      override def compare(that: Person2): Int = age.compareTo(that.age)
    }

    case class PersonForOrderedWithName(person: Person) extends Ordered[PersonForOrderedWithName] {
      override def compare(that: PersonForOrderedWithName): Int = person.name.compareTo(that.person.name)
    }

    case class PersonForOrderedWithAge(person: Person) extends Ordered[PersonForOrderedWithAge] {
      override def compare(that: PersonForOrderedWithAge): Int = person.age.compareTo(that.person.age)
    }

    // actually Ordering is derived from Ordered
    val personMaxName =
      people.map(PersonForOrderedWithName.apply).max.person // Person(Chris,20)

    case class ToPairOrdered[A <: Ordered[A], B <: Ordered[B]](a: A, b: B) extends Ordered[ToPairOrdered[A, B]] {
      override def compare(that: ToPairOrdered[A, B]): Int = {
        val res = a.compareTo(that.a)
        if (res != 0) res
        else b.compareTo(that.b)
      }
    }

    case class ToStringOrdered(value: String) extends Ordered[ToStringOrdered] {
      override def compare(that: ToStringOrdered): Int = value.compareTo(that.value)
    }

    val peopleStringMax = peopleStringPair.map {
      case (x, y) => ToPairOrdered(PersonForOrderedWithName(x), ToStringOrdered(y))
    }.max
    val res = (peopleStringMax.a.person, peopleStringMax.b.value) // (Person(Alice,21),3)
  }

}

object ImplicitConversion {
  import scala.languageFeature.implicitConversions
  implicit def fooConversion(x: Int): Boolean = x != 0

  object EvilJavaConversions {
    import scala.collection.JavaConversions._

    case class Foo(s: String)

    val map: Map[Foo, String] = Map(
      Foo("a") -> "a",
      Foo("b") -> "b"
    )

    val v = map.get("a") // should be a type error, actually returns null
  }

  object WithJavaConversions {
    import scala.collection.JavaConversions._
    val javaList = new java.util.ArrayList[Int]
    javaList.add(1)
    val scalaList: mutable.Buffer[Int] = javaList
  }

  object WithJavaConverters {
    import scala.collection.JavaConverters._
    val javaList = new java.util.ArrayList[Int]
    javaList.add(1)
    val scalaList: mutable.Buffer[Int] = javaList.asScala
  }
}
