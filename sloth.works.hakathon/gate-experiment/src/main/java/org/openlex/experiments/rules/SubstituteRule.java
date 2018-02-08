package org.openlex.experiments.rules;

public class SubstituteRule implements Rule {

    public String apply(String original, String what, String withWhat) {
        return original.replaceAll(what, withWhat);
    }

}
