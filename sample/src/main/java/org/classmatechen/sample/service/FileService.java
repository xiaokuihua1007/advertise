package org.classmatechen.sample.service;

import java.io.InputStream;

public interface FileService {

    String save(InputStream stream, String fileName);
}
