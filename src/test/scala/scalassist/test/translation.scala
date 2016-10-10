package scalassist.test

import javassist._

import scalassist._
import org.scalatest._

class translation extends FunSuite with Matchers {

    import scalassist.translation._

    val testCp = new ClassPool
    val testLoader = new Loader
    val translator = new TranslationSet
    testLoader.addTranslator(testCp, translator)
    testCp.insertClassPath(new LoaderClassPath(this.getClass.getClassLoader))
    testCp.insertClassPath("./test-data.jar")


    test("Class string decorator yields a transform spec") {
        ctClass"test.Test" { ct => } should matchPattern { case(_: String, _: Function1[CtClass, _]) => }
    }

    test("Field modification via translation DSL") {
        translator += ctClass"test.Test" { implicit ct =>
            field"aField" apply { implicit field =>
                field.`type`.name should equal("java.lang.String")
                field.constantValue should equal(None)
            }
        }
    }

    test("Method modification via translation DSL") {
        translator += ctClass"test.Test" { implicit ct =>
            method"testMethod" withParameters "int" apply {m =>
                m body_=
                    """
                      |{
                      | System.out.println("body modified");
                      | return $1 + 10;
                      |}
                    """.stripMargin
            }
        }

        val testClass = testLoader.loadClass("test.Test")
        val testInstance = testClass.newInstance()
        val testMethod = testClass.getMethod("testMethod", java.lang.Integer.TYPE)
        testMethod.invoke(testInstance, 20.asInstanceOf[Integer]).asInstanceOf[Integer] shouldEqual 30
    }


}
