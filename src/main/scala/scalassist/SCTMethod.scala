package scalassist

import javassist.CtMethod

/**
  * Created by roman on 10/2/16.
  */
class SCTMethod(val impl: CtMethod) extends AnyVal {

    def name_=(name: String) = {
        impl.setName(name)
        name
    }

    def returnType = impl.getReturnType

}
