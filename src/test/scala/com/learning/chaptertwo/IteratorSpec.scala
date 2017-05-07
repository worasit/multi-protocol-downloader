package com.learning.chaptertwo

import org.scalatest.{FlatSpec, Matchers}


class IteratorSpec extends FlatSpec with Matchers {

  "An Iterator object" should "display its description as section 2, lesson 25" in {
    Iterator.initDescription shouldEqual "Section 2, Lesson 25"
  }
}
