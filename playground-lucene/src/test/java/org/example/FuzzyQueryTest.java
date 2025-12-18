package org.example;

import java.awt.TextField;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.junit.jupiter.api.Test;

public class FuzzyQueryTest {

    @Test
    void test() throws Exception {
        RAMDirectory dir = new RAMDirectory();
        IndexWriter writer = new IndexWriter(dir, new WhitespaceAnalyzer(), MaxFieldLength.UNLIMITED);
        Document doc1 = new Document();
        doc1.add(new Field("contents", "fuzzy", Field.Store.YES, Index.ANALYZED));
        Document doc2 = new Document();
        doc2.add(new Field("contents", "wuzzy", Field.Store.YES, Index.ANALYZED));
        writer.addDocument(doc1);
        writer.addDocument(doc2);
        writer.close();
        IndexSearcher searcher = new IndexSearcher(dir);
        FuzzyQuery query = new FuzzyQuery(new Term("contents", "wuzza"));
        TopDocs matches = searcher.search(query, 10);
        System.out.println(matches.totalHits);
        // 기본 유사도 임계값: 0.5
        // 유사도 = 1 - 편집거리/min(검색어길이,색인된단어길이)
        // doc1의 유사도 = 1 - 2/5 = 0.6
        // doc2의 유사도 = 1 - 1/5 = 0.8
        // 둘 다 임계값을 만족하여 검색 대상으로 선정
    }
}
