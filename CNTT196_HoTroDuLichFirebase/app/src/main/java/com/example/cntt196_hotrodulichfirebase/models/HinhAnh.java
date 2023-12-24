package com.example.cntt196_hotrodulichfirebase.models;

import java.io.Serializable;

public class HinhAnh implements Serializable {
    private String ID_Document;
    private String fileName;

    public String getID_Document() {
        return ID_Document;
    }

    public String getFileName() {
        return fileName;
    }

    public void setID_Document(String ID_Document) {
        this.ID_Document = ID_Document;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public HinhAnh() {}

    public HinhAnh(String ID_Document, String fileName) {
        this.ID_Document = ID_Document;
        this.fileName = fileName;
    }
}
