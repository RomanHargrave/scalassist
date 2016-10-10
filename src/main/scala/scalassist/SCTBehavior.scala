package scalassist

import javassist.{CtBehavior, CtClass}

/**
  * Syntactic wrapper around CtBehavior
  */
class SCTBehavior(val impl: CtBehavior) extends AnyVal {

    def longName = impl.getLongName

    def methodInfo = impl.getMethodInfo

    def parameterAnnotations = impl.getParameterAnnotations
    def availableParameterAnnotations = impl.getAvailableParameterAnnotations
    def parameterTypes = impl.getParameterTypes

    def exceptionTypes = impl.getExceptionTypes
    def exceptionTypes_=(types: Array[CtClass]) = {
        impl.setExceptionTypes(types)
        types
    }

    def body_=(src: String) = impl.setBody(src)
    // TODO maybe a monad for building setBody(src, delegateObj, delegateMethod) calls?

}
