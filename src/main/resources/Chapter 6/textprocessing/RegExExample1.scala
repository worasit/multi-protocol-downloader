package textprocessing

object RegExExample1 extends App {
  val phonePattern = """\((\d\d\d)\) (\d\d\d)-(\d\d\d\d)""".r
  val phoneBook = """Name1	(210) 555-3758
    Name2	(512) 555-6948
    Name3	(605) 555-3724"""
    
  for(m <- phonePattern.findAllMatchIn(phoneBook)) {
    val areaCode = m.group(1)
    println(areaCode)
  }
}