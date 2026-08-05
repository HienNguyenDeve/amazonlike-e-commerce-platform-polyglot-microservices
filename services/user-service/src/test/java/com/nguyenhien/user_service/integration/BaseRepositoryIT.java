package com.nguyenhien.user_service.integration;

public abstract class BaseRepositoryIT extends BaseIntegrationTest {
    protected void persist(Object entity) {

        entityManager.persist(entity);
        flush();

    }

    protected void persistAll(Object... entities) {

        for (Object entity : entities) {
            entityManager.persist(entity);
        }

        flush();

    }

    protected void deleteAll(String table) {

        jdbcTemplate.execute(
                "TRUNCATE TABLE "
                        + table
                        + " CASCADE");

    }
}
