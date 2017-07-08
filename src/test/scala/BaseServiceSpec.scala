import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest._

trait BaseServiceSpec extends WordSpec with Matchers with ScalatestRouteTest with Routes {
}
