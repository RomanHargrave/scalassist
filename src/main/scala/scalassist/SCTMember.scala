package scalassist

import javassist.CtMember

/**
  * Syntactic wrapper around CtMember
  */
class SCTMember(val impl: CtMember) extends AnyVal {

    def name = impl.getName
    def signature = impl.getSignature
    def annotations = impl.getAnnotations
    def availableAnnotations = impl.getAvailableAnnotations
    def declaringClass = impl.getDeclaringClass

    def modifiers = impl.getModifiers
    def modifires_=(pMod: Int) = {
        impl.setModifiers(pMod)
        pMod
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


}
