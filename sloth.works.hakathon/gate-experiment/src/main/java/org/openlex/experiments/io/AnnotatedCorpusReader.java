package org.openlex.experiments.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import gate.Annotation;
import gate.AnnotationSet;
import gate.DataStore;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.util.GateException;
import gate.util.InvalidOffsetException;

/**
 * Created by mateva on 21.01.18.
 */
public class AnnotatedCorpusReader {

	private static String DATA_STORE_CLASS = "gate.persist.SerialDataStore";
	private static String DOC_IMPL_CLASS = "gate.corpora.DocumentImpl";
	private GATEPathBundle paths;

	public AnnotatedCorpusReader(GATEPathBundle paths) {
		this.paths = paths;
	}

	private void read() {
		setupAndStartGate();

		DataStore annotatedLaws = null;
		DataStore annotatedAmendments = null;
		try {
			annotatedLaws = Factory.openDataStore(DATA_STORE_CLASS, paths.getPathToFileResources() + "laws");
			annotatedAmendments = Factory.openDataStore(DATA_STORE_CLASS, paths.getPathToFileResources() + "amends");

			List<?> annotatedAmendmentsLrIds = annotatedAmendments.getLrIds(DOC_IMPL_CLASS);

			Set<Diff> diffs = new HashSet<>();

			for (Object id : annotatedAmendmentsLrIds) {
				Document d = readDocumentFrom(annotatedAmendments, id);

				for (Annotation a : d.getAnnotations().get("RuleSubstitute")) {
					FeatureMap map = a.getFeatures();
					String alNum = (String) map.get("alinea_number");
					String articleNum = (String) map.get("article_number");
					String what = (String) map.get("what");
					String withWhat = (String) map.get("withWhat");

					Diff diff = new Diff(alNum, articleNum, what, withWhat, d);
					diffs.add(diff);

					System.out.println(alNum);
					System.out.println(articleNum);
					System.out.println(what);
					System.out.println(withWhat);
				}

				Factory.deleteResource(d);
			}

			List<?> lawsDocIds = annotatedLaws.getLrIds(DOC_IMPL_CLASS);

			for (Object id : lawsDocIds) {
				Document d = readDocumentFrom(annotatedLaws, id);

				String originalContent = d.getContent().toString();
				String name = d.getName();
				writeContentTOFileAtPath(originalContent, paths.getPathToOriginalOutput() + name);

				AnnotationSet alineaContents = d.getAnnotations().get("AlineaContent");

				Map<String, String> changed = new HashMap<>();
				for (Diff diff : diffs) {

					for (Annotation alineaContent : alineaContents) {
						FeatureMap features = alineaContent.getFeatures();
						if (diff.getAlNum().equals(features.get("number"))
								&& diff.getArticleNum().equals(features.get("article_number"))) {
							System.out.println("Match!");
						}
						String tosub = getPartOfDocument(d, alineaContent.getStartNode().getOffset(),
								alineaContent.getEndNode().getOffset());
						String newVer = tosub.replaceAll(diff.getWhat(), diff.getWithWhat());
						changed.put(tosub, newVer);
					}

				}
				for (Map.Entry<String, String> entry : changed.entrySet()) {
					originalContent = originalContent.replace(entry.getKey(), entry.getValue());
				}

				writeContentTOFileAtPath(originalContent, paths.getPathToResultOutput() + name);
				Factory.deleteResource(d);

			}

		} catch (GateException e) {
			System.out.println(e);
		}
	}

	private String getPartOfDocument(Document docs, Long startOffSet, Long endOffset) {
		try {
			return docs.getContent().getContent(startOffSet, endOffset).toString();
		} catch (InvalidOffsetException e) {
			handleAnyException(e);
		}
		return null;
	}

	private void writeOriginalFile(Document document) {
		String originalContent = document.getContent().toString();
		String name = document.getName();
		writeContentTOFileAtPath(originalContent, paths.getPathToOriginalOutput() + name);
	}

	private void writeContentTOFileAtPath(String content, String path) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(path));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			handleAnyException(e);
		}
	}

	private void setupAndStartGate() {
		if (Gate.getGateHome() == null) {
			Gate.setGateHome(new File(paths.getPathToGATE()));
		}
		if (Gate.getPluginsHome() == null) {
			Gate.setPluginsHome(new File(paths.getPathToGATEPlugins()));
		}

		try {
			Gate.init();
		} catch (GateException ge) {
			handleAnyException(ge);
		}
	}

	private Document readDocumentFrom(DataStore ds, Object id) {
		try {
			return (Document) Factory.createResource(DOC_IMPL_CLASS,
					gate.Utils.featureMap(DataStore.DATASTORE_FEATURE_NAME, ds, DataStore.LR_ID_FEATURE_NAME, id));
		} catch (Exception e) {
			handleAnyException(e);
		}
		return null;
	}

	private void handleAnyException(Exception e) {
		System.out.println(e);
	}

	public static void main(String[] args) {

		GATEPathBundle paths = initializePaths(args);

		if (paths != null) {
			AnnotatedCorpusReader reader = new AnnotatedCorpusReader(paths);
			reader.read();
		}

	}

	private static GATEPathBundle initializePaths(String[] args) {
		GATEPathBundle paths = null;
		boolean argsInvalid = false;
		if (args.length >= 4) {
			ArgsParser aPaths = new ArgsParser(args);
			if (aPaths.handleArgs()) {
				paths = aPaths;
			} else {
				argsInvalid = true;
			}
		} else {
			argsInvalid = true;
		}
		if (argsInvalid) {
			ConfigParser cfgParser = new ConfigParser();
			if (cfgParser.parseConfig()) {
				paths = cfgParser;
			}

		}
		return paths;
	}

}