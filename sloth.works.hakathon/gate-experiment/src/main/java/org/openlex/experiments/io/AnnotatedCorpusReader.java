package org.openlex.experiments.io;

import gate.DataStore;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.corpora.DocumentStaxUtils;
import gate.util.GateException;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by mateva on 21.01.18.
 */
public class AnnotatedCorpusReader {
    private static String PATH_TO_GATE = "/home/mateva/Installs/GATE";
    private static String PATH_TO_GATE_PLUGINS = "/home/mateva/Installs/GATE/plugins";

    private static String PATH_TO_RESOURCES = "/home/mateva/OpenLex/M/OpenLex/sloth.works.hakathon/gate-experiment/src/main/resources/";
    private static String PATH_TO_FILE_RESOURCES = "file://" + PATH_TO_RESOURCES;
    private static String PATH_TO_OUTPUT = PATH_TO_RESOURCES + "results"

    private static String DATA_STORE_CLASS = "gate.persist.SerialDataStore";
    private static String DOC_IMPL_CLASS = "gate.corpora.DocumentImpl";

    public static void main(String[] args) {
        if (Gate.getGateHome() == null) {
            Gate.setGateHome(new File(PATH_TO_GATE));
        }
        if (Gate.getPluginsHome() == null) {
            Gate.setPluginsHome(new File(PATH_TO_GATE_PLUGINS));
        }

        DataStore annotatedLaws = null;
        DataStore annotatedAmendments = null;
        try {
            Gate.init();

            annotatedLaws = Factory.openDataStore(DATA_STORE_CLASS, PATH_TO_FILE_RESOURCES + "ds");
            annotatedAmendments = Factory.openDataStore(DATA_STORE_CLASS, PATH_TO_FILE_RESOURCES + "amendments");

            List docIds = annotatedLaws.getLrIds(DOC_IMPL_CLASS);

            for (Object id : docIds) {
                Document d = (Document) Factory.createResource(DOC_IMPL_CLASS,
                        gate.Utils.featureMap(DataStore.DATASTORE_FEATURE_NAME, annotatedLaws,
                                DataStore.LR_ID_FEATURE_NAME, id));

                System.out.println(d.getContent());
                System.out.print(d.getNamedAnnotationSets());
                try {
                    File outputFile = new File("fun.txt"); // based on doc name, sequential number, etc.
                    DocumentStaxUtils.writeDocument(d, outputFile);
                } catch (XMLStreamException | IOException ioe) {
                    System.out.println(ioe.getStackTrace());
                } finally {
                    Factory.deleteResource(d);
                }
            }

        } catch (GateException e) {
            System.out.println(e);
        }
    }

}
