package api.routes

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import api.Resource
import com.typesafe.scalalogging.StrictLogging
import mappings.JsonMappings

trait BaseRoute extends SprayJsonSupport with Resource with JsonMappings with StrictLogging
