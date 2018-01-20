package org.openlex.experiments.jape;

import gate.*;
import gate.jape.*;

// Import: block code will be embedded here
class JapeExperiments implements java.io.Serializable, gate.jape.RhsAction {
    private ActionContext ctx;

    public ActionContext getActionContext() { return null; }

    public String ruleName() { return null; }

    public String phaseName() { return null; }

    public void doit(
            gate.Document doc,
            java.util.Map<java.lang.String, gate.AnnotationSet> bindings,
            gate.AnnotationSet inputAS,
            gate.AnnotationSet outputAS,
            gate.creole.ontology.Ontology ontology) throws JapeException {
        AnnotationSet tokens = (AnnotationSet) bindings.get("tok");
        AnnotationSet dottedNumber = tokens.get("DottedNumber", tokens.firstNode().getOffset(), tokens.lastNode().getOffset());
        inputAS.removeAll(dottedNumber);

    }

    public void setActionContext(ActionContext actionContext) {

    }
}