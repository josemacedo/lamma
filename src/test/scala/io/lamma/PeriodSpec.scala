package io.lamma

import org.scalatest.{Matchers, WordSpec}

class PeriodSpec extends WordSpec with Matchers {

  "apply" should {
    "work" in {
      val expected = Period(Date(2014, 1, 5), Date(2015, 5, 1))
      Period((2014, 1, 5) -> (2015, 5, 1)) should be(expected)
    }
  }

  "fromDates" should {
    "return empty when input list is empty or only one date" in {
      Period.fromDates(Nil) should be('empty)
      Period.fromDates(List(Date(2014, 4, 1))) should be('empty)
    }

    "return periods when input list contains more than 2 dates" in {
      val input = Date(2014, 4, 10) :: Date(2014, 4, 20) :: Date(2014, 4, 30) :: Nil
      val expected = Period((2014, 4, 11) -> (2014, 4, 20)) :: Period((2014, 4, 21) -> (2014, 4, 30)) :: Nil
      Period.fromDates(input) should be(expected)
    }
  }

}
