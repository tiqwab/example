package example

object Draft1 {

  def multByTwo1(n: Int): Int =
    n * 2

  def multByTwo2[A](n: Int)(f: Int => A): A =
    f(n * 2)

  lazy val multSample: Int = multByTwo2(3)(x => x) // 6

  // simple
  def factorial1(n: Int): Int =
    if (n <= 1) 1
    else n * factorial1(n - 1)

  // tailrec
  def factorial2(n: Int): Int = {
    @scala.annotation.tailrec
    def loop(m: Int, acc: Int): Int =
      if (m <= 1) acc
      else loop(m - 1, acc * m)
    loop(n, 1)
  }

  // CPS
  def factorialCPS[A](n: Int)(cont: Int => A): A =
    if (n <= 1) cont(1)
    else factorialCPS(n - 1)(x => cont(n * x))

  // factorialCPS(3)(x1 => x1)
  // factorialCPS(2)(x2 => (x1 => x1)(3 * x2))
  // factorialCPS(1)(x3 => (x2 => (x1 => x1)(3 * x2))(2 * x3))
  // (x3 => (x2 => (x1 => x1)(3 * x2))(2 * x3))(1)
  // (x2 => (x1 => x1)(3 * x2))(2)
  // (x1 => x1)(6)
  // 6
  lazy val sample1: Int = factorialCPS(3)(identity) // 3! (=6)

  case class MyFile(name: String) {
    def open(): Unit = println(s"open $name")
    def close(): Unit = println(s"close $name")
    def content: String = s"i am $name"
  }

  case class MyConnection() {
    def open(): Unit = ()
    def close(): Unit = ()
  }

  def withFile[A](fileName: String)(f: MyFile => A): A = {
    val file = MyFile(fileName)
    file.open()
    try {
      f(file)
    } finally {
      file.close()
    }
  }

  def withConnection[A](f: MyConnection => A): A = {
    val conn = MyConnection()
    conn.open()
    try {
      f(conn)
    } finally {
      conn.close()
    }
  }

  def processFile(): Unit =
    withFile("file1") { f1 =>
      // do something
      println(f1)
    }

  def processThreeFiles(): Unit =
    withFile("file1") { f1 =>
      withFile("file2") { f2 =>
        withFile("file3") { f3 =>
          // do something
          println((f1, f2, f3))
        }
      }
    }

  def withFiles[A](fileNames: List[String])(f: List[MyFile] => A): A = {
    def loop(names: List[String], acc: List[MyFile]): A =
      names match {
        case Nil     => f(acc)
        case x :: xs => withFile(x)(file => loop(xs, file :: acc))
      }
    loop(fileNames, List.empty[MyFile])
  }

  lazy val sample2: Option[Int] =
    Option(1).flatMap { x =>
      Option(x + 1).flatMap { y =>
        Option(y * 2)
      }
    }

  lazy val sample3: Option[Int] =
    for {
      x <- Option(1)
      y <- Option(x + 1)
    } yield y * 2

  object MonadExercise {
    import scala.language.higherKinds

    trait Monad[F[_]] {
      def pure[A](a: A): F[A]
      def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
    }

    sealed trait MyOption[+A] {}
    case class MySome[+A](value: A) extends MyOption[A]
    case object MyNone extends MyOption[Nothing]

    object MyOption {
      def apply[A](a: A): MyOption[A] = MySome(a)
    }

    implicit def optionMonad: Monad[Option] = new Monad[Option] {
      override def pure[A](a: A): Option[A] = Some(a)
      override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa.flatMap(f)
    }

    implicit def myOptionMonad: Monad[MyOption] = new Monad[MyOption] {
      override def pure[A](a: A): MyOption[A] = MySome(a)
      override def flatMap[A, B](fa: MyOption[A])(f: A => MyOption[B]): MyOption[B] =
        fa match {
          case MySome(v) => f(v)
          case MyNone    => MyNone
        }
    }

    implicit final class MyOptionOps[+A](opt: MyOption[A]) {
      def flatMap[B](f: A => MyOption[B])(implicit F: Monad[MyOption]): MyOption[B] =
        F.flatMap(opt)(f)

      def map[B](f: A => B)(implicit F: Monad[MyOption]): MyOption[B] =
        F.flatMap(opt)(x => F.pure(f(x)))
    }

  }

  object ContSample {
    // ref. https://qiita.com/pab_tech/items/fc3d160a96cecdead622
    case class Cont[R, A](run: (A => R) => R) {
      def flatMap[B](f: A => Cont[R, B]): Cont[R, B] =
        Cont(g => run(a => f(a).run(g)))
      def map[B](f: A => B): Cont[R, B] =
        flatMap(x => Cont.pure(f(x)))
    }

    object Cont {
      def pure[R, A](a: A): Cont[R, A] = Cont(f => f(a))
    }

    case class MyFile(name: String) {
      def open(): Unit = println(s"open $name")
      def close(): Unit = println(s"close $name")
      def content: String = s"i am $name"
    }

    def withFile[R](name: String): Cont[R, MyFile] = Cont { f =>
      val file = MyFile(name)
      file.open()
      try {
        f(file)
      } finally {
        file.close()
      }
    }

    /*
     * scala> sample1
     * open file1.txt
     * open file2.txt
     * open file3.txt
     * i am file1.txt. i am file2.txt. i am file3.txt
     * close file3.txt
     * close file2.txt
     * close file1.txt
     */
    lazy val sample1: Unit = {
      val cont: Cont[Unit, String] = for {
        file1 <- withFile("file1.txt")
        file2 <- withFile("file2.txt")
        file3 <- withFile("file3.txt")
      } yield s"${file1.content}. ${file2.content}. ${file3.content}"
      cont.run(println)
    }

  }

}
