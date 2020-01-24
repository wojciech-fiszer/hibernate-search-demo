package com.fiserv.hibernatesearchdemo.model;

import lombok.*;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.WhitespaceTokenizerFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.ngram.EdgeNGramFilterFactory;
import org.hibernate.search.annotations.*;
import org.hibernate.search.annotations.Parameter;

import javax.persistence.*;

@Entity
@Indexed
@Data
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
@AnalyzerDef(
        name = "postContentIndexingAnalyzer",
        tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
                @TokenFilterDef(factory = LowerCaseFilterFactory.class),
                @TokenFilterDef(
                        factory = EdgeNGramFilterFactory.class,
                        params = {
                                @Parameter(name = "minGramSize", value = "1"),
                                @Parameter(name = "maxGramSize", value = "10")
                        }
                )
        })
@AnalyzerDef(
        name = "postContentQueryingAnalyzer",
        tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class),
        filters = {
                @TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
                @TokenFilterDef(factory = LowerCaseFilterFactory.class)
        }
)
public class Post {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private Long id;

    @Field(name = Fields.CONTENT, analyzer = @Analyzer(definition = "postContentIndexingAnalyzer"))
    private String content;

    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    private User user;

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Fields {
        public static final String CONTENT = "content";
    }
}
