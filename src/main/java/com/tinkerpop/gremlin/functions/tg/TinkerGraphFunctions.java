package com.tinkerpop.gremlin.functions.tg;

import com.tinkerpop.gremlin.functions.FunctionLibrary;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class TinkerGraphFunctions extends FunctionLibrary {

    public static final String NAMESPACE_PREFIX = "tg";

    public TinkerGraphFunctions() {
        this.addFunction(NAMESPACE_PREFIX, new OpenFunction());
    }
}
