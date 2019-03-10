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
          println(f1, f2, f3)
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

}
