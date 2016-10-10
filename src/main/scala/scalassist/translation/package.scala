package scalassist

import javassist.{CtClass, CtField, CtMethod}

/**
  * This package houses the building blocks for a class-transform DSL
  */
package object translation {

    import scalassist._
    import TranslationSet._

    /**
      * Generates a [[TranslationSpec]] from a name and string, passed to [[TranslationSet]]
      *
      * @param name             class name
      * @param application     function to which an applicable CtClass is passed
      * @return                 transform action for [[TranslationSet]]
      */
    def ModifyClass[R](name: String)(application: ClassTranslateApplication[R]): TranslationSpec[R] = (name, application)

    /**
      * Decorates CtMethod, for use with `method""`, to allow for `method"name" ... {m => do(m)}` style expressions
      *
      * @param method CtMethod
      */
    implicit class DSLCtMethodDecorator(val method: CtMethod) extends AnyVal {
        def apply[R](application: CtMethod => R) = application(method)
    }

    /**
      * Intermediary used to either further define a method search, or to proceed immediately without specifying signature
      */
    final class DSLCtMethodSearchBuilder(val definingClassAndName: (CtClass, String)) extends AnyVal {

        private def definingClass = definingClassAndName._1
        private def methodName = definingClassAndName._2

        /**
          * Search for the method based on stringified signature (ex. `([Ljava.lang.String)I` for `int name(String[])`)
          *
          * @param sig java signature string
          * @return    CtMethod
          */
        def withSignature(sig: String): CtMethod = definingClass.getMethod(methodName, sig)

        /**
          * Search for the method based on parameter class types
          *
          * @param params parameter classes
          * @return
          */
        def withParameterClass(params: Class[_]*): CtMethod = withParameters(params.map(_.getCanonicalName): _*)

        /**
          * Search for the method based on parameter type names
          *
          * @param paramNames parameter names
          * @return            method
          */
        def withParameters(paramNames: String*): CtMethod = {
            definingClass.methods.find(_.parameterTypes.map(_.name) sameElements paramNames) match {
                case Some(ctMethod) => ctMethod
                case None => throw new IllegalArgumentException("No method exists with these parameters")
            }
        }

        /**
          * If there are one or fewer methods named like the method we are looking for, return that method or raise an exception if
          * it is not found.
          *
          * An exception will be raised when there are multiple named methods.
          *
          * @return method
          */
        def asOnly: CtMethod = {
            val candidates = definingClass.methods.filter(_.name == methodName)
            if (candidates.length > 1) {
                throw new IllegalStateException("asOnly may only be called when there is only one method with a given name")
            } else candidates.headOption match {
                case Some(ctMethod) => ctMethod
                case None => throw new IllegalArgumentException(s"No method named $methodName exists within ${definingClass.name}")
            }
        }
    }

    /**
      * Convert a method search builder to a method with the assumption that there is only one matching method, by name
      * @param sb   search builder
      * @return     CtMethod
      * @see        [[DSLCtMethodSearchBuilder.asOnly]]
      */
    implicit def DSLSearchBuilderForceYield(sb: DSLCtMethodSearchBuilder): CtMethod = sb asOnly

    /**
      * Trick to allow applicative use of, or direct use of, a field had via the `field""` syntax
      * @param that the field
      */
    final class DSLCtFieldTween(val that: CtField) extends AnyVal {
        def apply[R](app: (CtField) => R): R = app(that)
    }

    implicit def DSLCtFieldTweenYield(tween: DSLCtFieldTween): CtField = tween.that

    /**
      * String context decorator that adds class, method, and field prefixes that generate and integrate with
      * class transforms
      *
      * Example:
      * `
      * transforms += class"java.lang.System" { implicit cls =>
      *     method"getSecurityManager" { implicit m =>
      *         m.body = """return evilSecurityManager;"""
      *     }
      * }
      * `
      *
      * @param stringContext StringContext
      */
    implicit class TranslationBuilderStringDecorations(val stringContext: StringContext) extends AnyVal {

        /**
          * Generates a [[TranslationSpec]] by way of `ctClass"name" {c => do(c)}` syntax
          *
          * @param parts            String parts
          * @param txApplication   transform application
          * @return                 [[TranslationSpec]]
          */
        def ctClass[R](parts: AnyRef*)(txApplication: ClassTranslateApplication[R]): TranslationSpec[R] = {
            parts.map(_.toString).foreach(println)
            ModifyClass(stringContext.s(parts: _*))(txApplication)
        }

        /**
          * Looks up a field against a CtClass instance
          *
          * Intended for use as follows:
          *
          * `
          * class"package.class" { implicit cls =>
          *     field"name" { field => something(field) } // ... via implicit decorator
          *     // or
          *     field"name".something
          * }
          * `
          * @param parts    String parts
          * @param cls      CtClass
          * @return         CtField
          */
        def field(parts: AnyRef*)(implicit cls: CtClass): DSLCtFieldTween = {
            new DSLCtFieldTween(cls.getField(stringContext.s(parts: _*)))
        }

        /**
          * Same as [[field]] but scope is reduced to fiedls declared by the class
          *
          * @param parts    String parts
          * @param cls      CtClass
          * @return         CtField
          */
        def declaredField(parts: AnyRef*)(implicit cls: CtClass): DSLCtFieldTween = {
            new DSLCtFieldTween(cls.getDeclaredField(stringContext.s(parts: _*)))
        }

        /**
          * Creates a MethodSearchBuilder
          *
          * Intended for use as follows:
          *
          * `
          * class"package.class" { implicit cls =>
          *     method"onlyOneLikeThis" { m => do(m) }
          *     method"aMethod" withSignature("int", "java.lang.String") { m => do(m) }
          * }
          * `
          * @param parts    String parts
          * @param cls      CtClass
          * @return         Method search builder
          */
        def method(parts: AnyRef*)(implicit cls: CtClass): DSLCtMethodSearchBuilder = {
            new DSLCtMethodSearchBuilder(definingClassAndName = (cls, stringContext.s(parts: _*)))
        }
    }

}
