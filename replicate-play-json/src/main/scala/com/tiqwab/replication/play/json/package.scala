package com.tiqwab.replication.play

package object json {
  // FIXME 実際の定義はこんな適当なのか？
  def unlift[T, U](f: T => Option[U]): T => U =
    t => f(t).get
}
