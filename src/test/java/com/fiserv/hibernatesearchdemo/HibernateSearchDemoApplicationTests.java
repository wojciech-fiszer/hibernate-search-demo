package com.fiserv.hibernatesearchdemo;

import com.fiserv.hibernatesearchdemo.model.Post;
import com.fiserv.hibernatesearchdemo.repository.PostRepository;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class HibernateSearchDemoApplicationTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private EntityManager entityManager;

    @Test
    @SuppressWarnings("unchecked")
    void indexPosts() {
        Post post = new Post();
        String oldContent = "content";
        post.setContent(oldContent);

        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionTemplate.execute(transactionStatus -> postRepository.save(post));

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(Post.class)
                .overridesForField(Post.Fields.CONTENT, "postContentQueryingAnalyzer")
                .get();

        Query query = queryBuilder.keyword().onField(Post.Fields.CONTENT).matching(oldContent).createQuery();
        List<Post> resultList = fullTextEntityManager.createFullTextQuery(query, Post.class).getResultList();
        assertEquals(1, resultList.size());

        String updatedContent = "updated";
        post.setContent(updatedContent);
        transactionTemplate.execute(transactionStatus -> postRepository.save(post));
        List<Post> queryForOldResultList = fullTextEntityManager.createFullTextQuery(query, Post.class).getResultList();
        Query queryForUpdated = queryBuilder.keyword().onField(Post.Fields.CONTENT).matching(updatedContent)
                .createQuery();
        List<Post> queryForUpdatedResultList = fullTextEntityManager.createFullTextQuery(queryForUpdated, Post.class)
                .getResultList();
        assertTrue(queryForOldResultList.isEmpty());
        assertEquals(1, queryForUpdatedResultList.size());
    }
}
