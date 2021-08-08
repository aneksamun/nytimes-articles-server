package co.uk.redpixel.articles.config

import eu.timepit.refined.types.all.NonSystemPortNumber

final case class ServerConfig(httpPort: NonSystemPortNumber)
