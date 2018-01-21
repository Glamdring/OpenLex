package org.openlex.experiments.rules;

/**
 * Created by mateva on 20.01.18.
 */
public interface Rule {
    String perform(String original, String paramA, String paramB);
}

