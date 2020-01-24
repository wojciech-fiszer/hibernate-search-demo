package com.fiserv.hibernatesearchdemo;

import com.fiserv.hibernatesearchdemo.model.Post;
import com.fiserv.hibernatesearchdemo.repository.PostRepository;
import com.fiserv.hibernatesearchdemo.repository.UserRepository;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
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
    private UserRepository userRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private EntityManager entityManager;

    @Test
    void contextLoads() {
    }

    @Test
    void indexPosts() {
        Post post = new Post();
        post.setContent("content");

        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionTemplate.execute(transactionStatus -> postRepository.save(post));

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
                .forEntity(Post.class)
                .get();

        Query query = queryBuilder.keyword().onField("content").matching("content").createQuery();
        List resultList = fullTextEntityManager.createFullTextQuery(query, Post.class).getResultList();
        assertEquals(1, resultList.size());

        post.setContent("updated");
        transactionTemplate.execute(transactionStatus -> postRepository.save(post));
        List queryForOldResultList = fullTextEntityManager.createFullTextQuery(query, Post.class).getResultList();
        Query queryForUpdated = queryBuilder.keyword().onField("content").matching("updated").createQuery();
        List queryForUpdatedResultList = fullTextEntityManager.createFullTextQuery(queryForUpdated, Post.class).getResultList();
        assertTrue(queryForOldResultList.isEmpty());
        assertEquals(1, queryForUpdatedResultList.size());
    }
}
