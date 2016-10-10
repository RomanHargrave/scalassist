import javassist._

/**
  * Created by roman on 10/2/16.
  */
package object scalassist {

    private[scalassist] val ScalassistLog = grizzled.slf4j.Logger("scalassist")

    implicit class JClassDecorators(val cls: java.lang.Class[_]) extends AnyVal {
    }

    implicit def wrapCtBehavior(ctBehavior: CtBehavior): SCTBehavior = new SCTBehavior(ctBehavior)
    implicit def wrapCtClass(ctClass: CtClass): SCTClass = new SCTClass(ctClass)
    implicit def wrapCtConstructor(ctConstructor: CtConstructor): SCTConstructor = new SCTConstructor(ctConstructor)
    implicit def wrapCtField(ctField: CtField): SCTField = new SCTField(ctField)
    implicit def wrapCtMember(ctMember: CtMember): SCTMember = new SCTMember(ctMember)
    implicit def wrapCtMethod(ctMethod: CtMethod): SCTMethod = new SCTMethod(ctMethod)
}
