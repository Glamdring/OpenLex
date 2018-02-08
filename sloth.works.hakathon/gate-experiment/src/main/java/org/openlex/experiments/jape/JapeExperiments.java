package org.openlex.experiments.jape;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.jape.ActionContext;
import gate.jape.JapeException;

// Import: block code will be embedded here
class JapeExperiments implements java.io.Serializable, gate.jape.RhsAction {
	private static final long serialVersionUID = 7825106315666933111L;
	private ActionContext ctx;

    @Override
	public ActionContext getActionContext() {
        return null;
    }

    public String ruleName() {
        return null;
    }

    public String phaseName() {
        return null;
    }

    @Override
	public void doit(
            gate.Document doc,
            java.util.Map<java.lang.String, gate.AnnotationSet> bindings,
            gate.AnnotationSet inputAS,
            gate.AnnotationSet outputAS,
            gate.creole.ontology.Ontology ontology) throws JapeException {

        AnnotationSet rule = bindings.get("ruleMatch");


        AnnotationSet articleAlineaMention = bindings.get("aam");
        Annotation aaMention = articleAlineaMention.iterator().next();

        AnnotationSet startA = bindings.get("startA");
        Annotation startAPoint = startA.iterator().next();

        AnnotationSet endA = bindings.get("endA");
        Annotation endAPoint = endA.iterator().next();

        AnnotationSet startB = bindings.get("startB");
        Annotation startBPoint = startB.iterator().next();

        AnnotationSet endB = bindings.get("endB");
        Annotation endBPoint = endB.iterator().next();

        try {
            FeatureMap features = Factory.newFeatureMap();
            features.put("what", gate.Utils.cleanStringFor(doc, startAPoint.getEndNode().getOffset(),
                    endAPoint.getStartNode().getOffset()));
            features.put("withWhat", gate.Utils.cleanStringFor(doc, startBPoint.getEndNode().getOffset(),
                    endBPoint.getStartNode().getOffset()));
            features.put("article_number", aaMention.getFeatures().get("article_number"));
            features.put("alinea_number", aaMention.getFeatures().get("alinea_number"));

            features.put("rule", "RuleSubstitute");

            outputAS.add(rule.firstNode().getOffset(), rule.lastNode().getOffset(), "RuleSubstitute", features);
        } catch (Exception e) {
            e.printStackTrace();
        }

//        AnnotationSet subset = inputAS.getCovering("AlineaContent", doc..getStartNode().getOffset(),
//                pointCont.getEndNode().getOffset());
//        if (!subset.isEmpty()) {
//            Annotation alinea = subset.iterator().next();
//            FeatureMap features = pointCont.getFeatures();
//            features.put("article_number", alinea.getFeatures().get("article_number"));
//            features.put("alinea_number", alinea.getFeatures().get("number"));
//        } else {
//            AnnotationSet subset2 = inputAS.getCovering("ArticleContent", pointCont.getStartNode().getOffset(),
//                    pointCont.getEndNode().getOffset());
//            if (!subset2.isEmpty()) {
//                Annotation article = subset.iterator().next();
//                FeatureMap features = pointCont.getFeatures();
//                features.put("article_number", article.getFeatures().get("number"));
//            }
//        }
    }

    @Override
	public void setActionContext(ActionContext actionContext) {

    }
}