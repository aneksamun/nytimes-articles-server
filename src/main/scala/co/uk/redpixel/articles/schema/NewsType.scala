package co.uk.redpixel.articles.schema

import co.uk.redpixel.articles.data.Headlines
import sangria.schema.{Field, ObjectType, OptionType, StringType, fields}

object NewsType {

  def apply(): ObjectType[Unit, Headlines] =
    ObjectType(
      name = "News",
      fields = fields(
        Field(
          name = "link",
          fieldType = OptionType(StringType),
          resolve = _.value.link
        ),
        Field(
          name = "title",
          fieldType = OptionType(StringType),
          resolve = _.value.title
        )
      )
    )
}
