/*
 * Copyright (c) 2015. All work is owned by Sam Collins and is not to be redistributed without his permission.
 * He is also not liable for any damage done and is not required to maintain any work.
 */

package org.cyberworks.builder.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by scollins on 07/12/15.
 */
@XmlRootElement
public class Mods {
    private List<ModFile> modFiles = null;

    public List<ModFile> getModFiles() {
        return modFiles;
    }

    @XmlElement(name="mod")
    public void setModFiles(List<ModFile> modFiles) {
        this.modFiles = modFiles;
    }
}
