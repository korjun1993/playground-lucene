package org.example;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.RAMDirectory;
import org.junit.jupiter.api.Test;

public class WildcardQueryTest {

    @Test
    void test() throws Exception {
        RAMDirectory dir = new RAMDirectory();
        IndexWriter writer = new IndexWriter(dir, new WhitespaceAnalyzer(), MaxFieldLength.UNLIMITED);
        Document doc = new Document();
        doc.add(new Field("contents", "wild child mild mildew", Field.Store.YES, Index.ANALYZED));
        writer.addDocument(doc);
        writer.close();
        IndexSearcher searcher = new IndexSearcher(dir);
        Query query = new WildcardQuery(new Term("contents", "?ild*"));
        searcher.search(query, 10);
        TopDocs matches = searcher.search(query, 10);
        System.out.println(matches.totalHits); // 1
    }
}
