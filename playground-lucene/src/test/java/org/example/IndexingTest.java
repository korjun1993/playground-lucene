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
    public void test() throws IOException {
        IndexReader reader = IndexReader.open(directory);
        Assertions.assertEquals(reader.numDocs(), ids.length);
        Assertions.assertEquals(reader.maxDoc(), ids.length);
        reader.close();
    }
}
