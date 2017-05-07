package textprocessing

object RegExPatterns extends App {
  val linePattern = """(\w+), (\w+)\s+(\(\d\d\d\) \d\d\d-\d\d\d\d)""".r
  
  val source = io.Source.fromFile("bigFile.txt")
  for(linePattern(last,first,number) <- source.getLines) {
    // do stuff with last, first, and the number
  }
  source.close
}