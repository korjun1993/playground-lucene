package org.example;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.junit.jupiter.api.Test;

public class PhraseQueryTest {

    @Test
    void test() throws Exception {
        RAMDirectory dir = new RAMDirectory();
        IndexWriter writer = new IndexWriter(dir, new WhitespaceAnalyzer(), MaxFieldLength.UNLIMITED);
        Document doc = new Document();
        doc.add(new Field("field", "the quick brown fox jumped over the lazy dog", Store.YES, Index.ANALYZED));
        writer.addDocument(doc);
        writer.close();
        IndexSearcher searcher = new IndexSearcher(dir);
        PhraseQuery query = new PhraseQuery();
        query.setSlop(3);
        query.add(new Term("field", "fox"));
        query.add(new Term("field", "quick"));
        TopDocs matches = searcher.search(query, 10);
        System.out.println(matches.totalHits);
    }
}
