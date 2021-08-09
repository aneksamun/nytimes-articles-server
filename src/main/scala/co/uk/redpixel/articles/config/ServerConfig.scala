package co.uk.redpixel.articles.config

import com.comcast.ip4s.Port
import pureconfig.ConfigReader
import pureconfig.error.CannotConvert

final case class ServerConfig(httpPort: Port)

object ServerConfig {

  implicit val PortReader: ConfigReader[Port] =
    ConfigReader[Int].emap(value => Port.fromInt(value) toRight CannotConvert(
      value.toString, "Port", s"must be between ${Port.MinValue} and ${Port.MaxValue}"
    ))
}
