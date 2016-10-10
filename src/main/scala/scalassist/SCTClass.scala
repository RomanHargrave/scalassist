package scalassist

import javassist.CtClass

/**
  * Wrapper for semantic getter/setters
  */
class SCTClass(val impl: CtClass) extends AnyVal {

    def superclass = Option(impl.getSuperclass)
    def superclass_=(cls: Option[CtClass]) = cls match {
        case Some(that) =>
            impl.setSuperclass(that)
            cls
        case None =>
            impl.setSuperclass(null)
            cls
    }

    def simpleName = impl.getSimpleName
    def packageName = impl.getPackageName
    def name = impl.getName
    def name_=(pName: String) = {
        impl.setName(pName)
        pName
    }

    def modifiers = impl.getModifiers
    def modifiers_=(pModifiers: Int) = {
        impl.setModifiers(pModifiers)
        pModifiers
    }

    def interfaces = impl.getInterfaces
    def interfaces_=(ifaces: Array[CtClass]) = {
        impl.setInterfaces(ifaces)
        ifaces
    }

    def genericSignature = Option(impl.getGenericSignature)
    def genericSignature_=(sig: Option[String]) = sig match {
        case Some(that) =>
            impl.setGenericSignature(that)
            sig
        case None =>
            impl.setGenericSignature(null)
            sig
    }


    def fields = impl.getFields
    def declaredFields = impl.getDeclaredFields
    def declaredBehaviors = impl.getDeclaredBehaviors
    def declaredClasses = impl.getDeclaredClasses
    def methods = impl.getMethods
    def constructors = impl.getConstructors
    def declaredConstructors = impl.getDeclaredConstructors
    def classInitializer = Option(impl.getClassInitializer)
    def nestedClasses = impl.getNestedClasses
    def referencedClasses = impl.getRefClasses

    def annotations = impl.getAnnotations
    def availableAnnotations = impl.getAvailableAnnotations

    def enclosingBehavior = Option(impl.getEnclosingBehavior)
    def componentType = Option(impl.getComponentType)

    def modified = impl.isModified
    def frozen = impl.isFrozen
    def primitive = impl.isPrimitive

    def classPool = impl.getClassPool
    def url = impl.getURL

}