import com.nongped.Application
import org.scalatest._


class GreetingSpec extends FlatSpec with Matchers {
  "A greeting message" should "says Hello Scala" in {
    Application.greetingMsg shouldEqual "Hello Scala"
  }
}
