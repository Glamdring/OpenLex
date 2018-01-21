package org.openlex.experiments.io;

import gate.Document;

/**
 * Created by mateva on 21.01.18.
 */
public class Diff {
    String alNum ;
    String articleNum ;
    String what;
    String withWhat ;
    Document doc;

    public Diff(String alNum, String articleNum, String what, String withWhat, Document doc) {
        this.alNum = alNum;
        this.articleNum = articleNum;
        this.what = what;
        this.withWhat = withWhat;
        this.doc = doc;
    }

    public Document getDoc() {
        return doc;
    }

    public String getAlNum() {
        return alNum;
    }

    public void setAlNum(String alNum) {
        this.alNum = alNum;
    }

    public String getArticleNum() {
        return articleNum;
    }

    public void setArticleNum(String articleNum) {
        this.articleNum = articleNum;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getWithWhat() {
        return withWhat;
    }

    public void setWithWhat(String withWhat) {
        this.withWhat = withWhat;
    }

}
