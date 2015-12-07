/*
 * Copyright (c) 2015. All work is owned by Sam Collins and is not to be redistributed without his permission.
 * He is also not liable for any damage done and is not required to maintain any work.
 */

package org.cyberworks.builder.xml;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * Created by scollins on 07/12/15.
 */
public class ModFile {
    private String folder;
    private String name;
    private String hash;

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getFolder() {
        return folder;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }
}
