package co.uk.redpixel.articles.support

import org.scalacheck.Gen
import org.scalacheck.Gen.Choose

package object properties {

  def genNonPosNum[T](implicit num: Numeric[T], c: Choose[T]): Gen[T] =
    Gen.negNum[T].flatMap(n => Gen.choose(n, num.zero))

  def genNonNegNum[T](implicit num: Numeric[T], c: Choose[T]): Gen[T] =
    Gen.posNum[T].flatMap(n => Gen.choose(num.zero, n))
}
