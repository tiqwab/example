package com.tiqwab.example.modeling

import scalikejdbc.{AsIsParameterBinder, DBSession}
import scalikejdbc.interpolation.SQLSyntax
import skinny.orm.SkinnyMapperWithId
import skinny.orm.feature.CRUDFeatureWithId

/*
 * ID まわり
 */

// TODO: 実際は type member を使っているが、generics でも問題ないはず
// -> IsoIdCompanion で rawValue の型を使いたいので type member でないといけない
//    ここでは LongIdLike に限定することで回避しておく
// -> 別に generics でも型の指定はできるのでやはりこの場合どちらでも良さそう
// TODO: Any を (明示的に) 継承する意味？
trait IdLike[T] {
  def value: T
}

trait LongIdLike extends IdLike[Long]

// CRUDMapper 内で DbID <-> ID の相互変換を個々のクラスに依存せず定義できるように iso を導入する
// Iso は型クラスとして使用する
// -> 2 つのクラスの相互変換を行うので IdLike に継承させて... よりその方が使いやすそう
// 実際は Prism から継承させている
// 実際は 2 つの Iso を組み合わせる`compose` や Iso[A, B] から Iso[B, A] を作成する `reverse` なんかも定義している
trait Iso[A, B] {
  def get(a: A): B
  def reverseGet(b: B): A
}

object Iso {
  def apply[A, B](_get: A => B)(_reverseGet: B => A): Iso[A, B] =
    new Iso[A, B] {
      override def get(a: A): B = _get(a)
      override def reverseGet(b: B): A = _reverseGet(b)
    }
}

trait IsoIdCompanion[B, A <: IdLike[B]] {
  def apply(value: B): A

  implicit def longLongIdIso: Iso[B, A] =
    Iso(apply)(_.value)
}

trait IsoLongIdCompanion[A <: LongIdLike] {
  def apply(value: Long): A

  implicit def longLongIdIso: Iso[Long, A] =
    Iso(apply)(_.value)
}

/*
 * Entity まわり
 */

trait Entity[ID] {
  def id: ID

  // Entity's equality depends on only id
  override def equals(obj: scala.Any): Boolean = obj match {
    case e: Entity[ID] => id == e.id
    case _             => false
  }
  // Re-implement `hashCode` since `equals` is overridden
  // reference for `##`: https://stackoverflow.com/questions/9068154/what-is-the-difference-between-and-hashcode
  override def hashCode(): Int = 31 * id.##
}

/*
 * Repository まわり
 */

// Use generics
trait Repository[ID, E <: Entity[ID], Context] {
  def findById(id: ID)(implicit ctx: Context): Option[E]
  def store(entity: E)(implicit ctx: Context): Unit
  def deleteById(id: ID)(implicit ctx: Context): Int
  def count()(implicit ctx: Context): Long
}

trait RepositoryOnMemory[ID, E <: Entity[ID], Context]
    extends Repository[ID, E, Context] {

  protected val entities: scala.collection.mutable.Map[ID, E]

  override def findById(id: ID)(implicit ctx: Context): Option[E] =
    synchronized {
      entities.get(id)
    }

  override def store(entity: E)(implicit ctx: Context): Unit =
    synchronized {
      entities.update(entity.id, entity)
    }

  override def deleteById(id: ID)(implicit ctx: Context): Int =
    synchronized {
      entities.remove(id).map(_ => 1).getOrElse(0)
    }

  override def count()(implicit ctx: Context): Long =
    synchronized {
      entities.size
    }

}

/*
 * `CRUDFeatureWithId` requires to implement methods:
 * - def defaultAlias: Alias[Application]
 * - def extract(rs: WrappedResultSet, n: ResultName[Application]): Application
 * - def idToRawValue(id: ApplicationId): Any
 * - def rawValueToId(rawValue: Any): ApplicationId
 */
// TODO: E と ConcreteE をわけているけど、そうしたい場面ってそんなにある？
trait RepositoryOnJDBC[
    ID, E >: ConcreteE <: Entity[ID], ConcreteE <: Entity[ID]]
    extends Repository[ID, E, DBSession] {
  // TODO: ここは自分型アノテーションを使う必然性がある？
  self: CRUDFeatureWithId[ID, ConcreteE] =>

  protected def namedValuesWithoutId(entity: E): Seq[(SQLSyntax, Any)]

  // TODO: Implicit ParameterBinderFactor[Any] for the parameter type Any is missing で怒られる
  // とりあえず `AsIsParameterBinder` でごまかす
  protected def idNamedValue(entity: E): (SQLSyntax, Any) =
    column.id -> AsIsParameterBinder(idToRawValue(entity.id))

  protected def namedValues(entity: E): Seq[(SQLSyntax, Any)] =
    idNamedValue(entity) +: namedValuesWithoutId(entity)

  override def store(entity: E)(implicit ctx: DBSession): Unit = {
    findById(entity.id)
      .map { _ =>
        updateById(entity.id)
          .withNamedValues(namedValuesWithoutId(entity): _*)
      }
      .getOrElse {
        createWithNamedValues(namedValues(entity): _*)
      }
  }

  override def count()(implicit ctx: DBSession): Long = {
    count(fieldName = Symbol(""), distinct = false)
  }

}

// ここは trait ではなく抽象クラスである必要がある
// - `idToRawValue`, `rawValueToId` の定義に implicit に idIso を 使いたい
// - これらのメソッドは継承元で定義されているので signature を変更できない
// - コンストラクタとして implicit idIso を渡すしかない
abstract class CRUDMapperLongIdRepository[ID, E >: ConcreteE <: Entity[ID],
ConcreteE <: Entity[ID]](implicit idIso: Iso[Long, ID])
    extends SkinnyMapperWithId[ID, ConcreteE]
    with CRUDFeatureWithId[ID, ConcreteE]
    with RepositoryOnJDBC[ID, E, ConcreteE] {

  override def useAutoIncrementPrimaryKey: Boolean = false

  override def idToRawValue(id: ID): Any = idIso.reverseGet(id)

  override def rawValueToId(value: Any): ID =
    idIso.get(value.asInstanceOf[Long])

}

// TODO: CRUDFeatureWithId と SkinnyMapperWithId の違いのニュアンスを掴んでおきたい
// - skinnyORM が提供している trait `SkinnyCRUDMapperWithId` はまさに上の 2 つを継承している
// - `CRUDFeatureWithId` が文字通り CRUD 操作の定義を担当していそう
// - `SkinnyMapperWithId` が Mapper として必要なその他諸々を担当していそう
