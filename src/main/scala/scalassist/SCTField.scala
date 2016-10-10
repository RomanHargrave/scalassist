package scalassist

import javassist.{CtClass, CtField}

/**
  * Syntactic wrapper around CtField
  */
class SCTField(val impl: CtField) extends AnyVal {

    def fieldInfo = impl.getFieldInfo

    def name_=(name: String) = {
        impl.setName(name)
        name
    }

    def `type` = impl.getType
    def type_=(nType: CtClass) = {
        impl.setType(nType)
        `type`
    }
    def fieldType = `type`
    def fieldType_=(nType: CtClass) = type_=(nType)


    def constantValue = Option(impl.getConstantValue)

}
