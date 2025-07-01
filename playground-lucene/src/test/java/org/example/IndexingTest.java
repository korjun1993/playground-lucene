package org.example;

import java.io.IOException;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class IndexingTest {

    protected String[] ids = {"1", "2"};
    protected String[] unindexed = {"Netherlands", "Italy"};
    protected String[] unstored = {"Amsterdam has lots of bridges", "Venice has lots of canals"};
    protected String[] text = {"Amsterdam", "Venice"};
    private Directory directory;

    @BeforeEach
    protected void setUp() throws Exception {
        directory = new RAMDirectory();
        IndexWriter writer = getWriter();
        for (int i = 0; i < ids.length; i++) {
            Document doc = new Document();
            doc.add(new Field("id", ids[i], Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field("country", unindexed[i], Store.YES, Index.NO));
            doc.add(new Field("contents", unstored[i], Field.Store.NO, Field.Index.ANALYZED));
            doc.add(new Field("city", text[i], Field.Store.YES, Field.Index.ANALYZED));
            writer.addDocument(doc);
        }
        writer.close();
    }

    private IndexWriter getWriter() throws IOException {
        return new IndexWriter(directory, new WhitespaceAnalyzer(), MaxFieldLength.UNLIMITED);
    }

    @Test
    @DisplayName("Writer 테스트")
    public void test1() throws IOException {
        IndexWriter writer = getWriter();
        Assertions.assertEquals(writer.numDocs(), ids.length);
        writer.close();
    }

    @Test
    @DisplayName("Reader 테스트")
    public void test2() throws IOException {
        IndexReader reader = IndexReader.open(directory);
        Assertions.assertEquals(reader.numDocs(), ids.length);
        Assertions.assertEquals(reader.maxDoc(), ids.length);
        reader.close();
    }

    @Test
    @DisplayName("삭제 테스트")
    public void test3() throws IOException {
        IndexWriter writer = getWriter();
        writer.deleteDocuments(new Term("id", "1"));
        writer.commit();
        Assertions.assertTrue(writer.hasDeletions()); // 색인에 삭제됐다고 표시된 문서가 있는지 확인
        Assertions.assertEquals(2, writer.maxDoc()); // 삭제 여부에 관계없이 maxDoc은 전체 문서 수를 나타냄
        Assertions.assertEquals(1, writer.numDocs()); // 삭제되지 않은 문서 수를 나타냄
        writer.close();
    }

    @Test
    @DisplayName("삭제후 최적화 테스트")
    public void test4() throws IOException {
        IndexWriter writer = getWriter();
        writer.deleteDocuments(new Term("id", "1"));
        writer.optimize();
        writer.commit();
        Assertions.assertFalse(writer.hasDeletions()); // 최적화 후 삭제된 문서가 없음을 확인
        Assertions.assertEquals(1, writer.maxDoc()); // 최적화를 진행했기 때문에 maxDoc 메소드 역시 2 대신 1 값을 리턴
        Assertions.assertEquals(1, writer.numDocs());
        writer.close();
    }
}
