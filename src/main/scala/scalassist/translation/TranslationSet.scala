package scalassist.translation

import javassist.{ClassPool, CtClass, Translator}

import scalassist._

/**
  * Transform package that calls class transforms on a CtClass matching the classname specified at construction
  * Part of the transform DSL
  */
final class TranslationSet extends AnyRef with Translator {

    import TranslationSet._

    private var translations = Set[TranslationSpec[_]]()

    override def start(pool: ClassPool): Unit = ()

    override def onLoad(pool: ClassPool, classname: String): Unit = {
        ScalassistLog.trace(s"Running translations for $classname")
        val ctClass = pool.getCtClass(classname)
        translations withFilter(_._1 == classname) foreach(_._2(ctClass))
    }

    def +=(activity: TranslationSpec[_]) = translations += activity

}

object TranslationSet {

    type ClassTranslateApplication[R] = CtClass => R
    type TranslationSpec[R] = (String, ClassTranslateApplication[R])

}