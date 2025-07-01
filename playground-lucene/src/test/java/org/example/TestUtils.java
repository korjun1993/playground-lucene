package org.example;

import java.io.IOException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

public class TestUtils {

    public static int hitCount(IndexSearcher searcher, Query query) throws IOException {
        return searcher.search(query, 1).totalHits;
    }
}
