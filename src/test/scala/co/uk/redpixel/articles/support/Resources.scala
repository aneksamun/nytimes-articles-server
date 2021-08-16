package co.uk.redpixel.articles.support

import java.io.File

trait Resources {

  def loadFile(resource: String) =
    new File(getClass.getResource(s"/$resource").toURI)
}
