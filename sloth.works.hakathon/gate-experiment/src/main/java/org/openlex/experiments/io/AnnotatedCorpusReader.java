package org.openlex.experiments.io;

import gate.DataStore;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.util.GateException;
import gate.util.InvalidOffsetException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
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
    private static String PATH_TO_RESULT_OUTPUT = PATH_TO_RESOURCES + "results/";
    private static String PATH_TO_ORIGINAL_OUTPUT = PATH_TO_RESOURCES + "original/";

    private static String DATA_STORE_CLASS = "gate.persist.SerialDataStore";
    private static String DOC_IMPL_CLASS = "gate.corpora.DocumentImpl";



    private void read() {
        setupAndStartGate();

        DataStore annotatedLaws = null;
        DataStore annotatedAmendments = null;
        try {
            annotatedLaws = Factory.openDataStore(DATA_STORE_CLASS, PATH_TO_FILE_RESOURCES + "ds");
            annotatedAmendments = Factory.openDataStore(DATA_STORE_CLASS, PATH_TO_FILE_RESOURCES + "amendments");

            List lawsDocIds = annotatedLaws.getLrIds(DOC_IMPL_CLASS);

            for (Object id : lawsDocIds) {
                Document d = readDocumentFrom(annotatedLaws, id);

                writeOriginalFile(d);
                d.getAnnotations().get("NumberedChapter");
//                try {
//                    d.getAnnotations();
//                } catch (XMLStreamException | IOException ioe) {
//                    handleFuckingException(ioe);
//                } finally {
                    Factory.deleteResource(d);
            //    }
            }

        } catch (GateException e) {
            System.out.println(e);
        }
    }

    private String getPartOfDocument(Document docs, Long startOffSet, Long endOffset) {
        try {
            return docs.getContent().getContent(startOffSet, endOffset).toString();
        } catch (InvalidOffsetException e) {
            handleFuckingException(e);
        }
        return null;
    }

    private void writeOriginalFile(Document document) {
        String originalContent = document.getContent().toString();
        String name = document.getName();
        writeContentTOFileAtPath(originalContent, PATH_TO_ORIGINAL_OUTPUT + name);
    }

    private void writeContentTOFileAtPath(String content, String path) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            handleFuckingException(e);
        }
    }

    private void setupAndStartGate() {
        if (Gate.getGateHome() == null) {
            Gate.setGateHome(new File(PATH_TO_GATE));
        }
        if (Gate.getPluginsHome() == null) {
            Gate.setPluginsHome(new File(PATH_TO_GATE_PLUGINS));
        }

        try {
            Gate.init();
        } catch (GateException ge) {
            handleFuckingException(ge);
        }
    }

    private Document readDocumentFrom(DataStore ds, Object id) {
        try {
            return (Document) Factory.createResource(DOC_IMPL_CLASS,
                    gate.Utils.featureMap(DataStore.DATASTORE_FEATURE_NAME, ds,
                            DataStore.LR_ID_FEATURE_NAME, id));
        } catch (Exception e) {
            handleFuckingException(e);
        }
        return null;
    }

    private void handleFuckingException(Exception e) {
        System.out.println(e);
    }

    public static void main(String[] args) {
        AnnotatedCorpusReader reader = new AnnotatedCorpusReader();
        reader.read();
    }

}
