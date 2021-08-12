package co.uk.redpixel.articles.schema

import co.uk.redpixel.articles.data.Headlines
import sangria.macros.derive.{ObjectTypeName, deriveObjectType}
import sangria.schema.ObjectType

object NewsType {

  def apply(): ObjectType[Unit, Headlines] =
    deriveObjectType[Unit, Headlines](ObjectTypeName("News"))
}
