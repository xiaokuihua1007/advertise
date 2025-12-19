package org.classmatechen.sample.sample;

import java.util.Iterator;

import org.classmatechen.basic.Request;
import org.classmatechen.basic.req.RequestBuilder;
import org.classmatechen.basic.req.page.Page;

public class SamplerUtil {

    public static <P, R> Request<P, R> create(Class<? extends Request<P, R>> cls) {

        Request<P, R> request;
        try {
            request = cls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new RequestBuilder<>(request).limit().retry().build();
    }

    public static <P extends Page, R> Request<P, Iterator<R>> page(Class<? extends Request<P, R>> cls) {

        Request<P, R> request;
        try {
            request = cls.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new RequestBuilder<>(request).limit().retry().page().build();
    }
}
