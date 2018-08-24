package org.sql2o;

/**
 * Represents a method with a {@link Connection} and an optional argument. Implementations of this interface be used as
 * a parameter to one of the {@link Sql2o#runInTransaction(StatementRunnableWithResult<V>)} Sql2o.runInTransaction} overloads,
 * to run core safely in a transaction.
 */
public interface StatementRunnableWithResult<V> {

    V run(Connection connection, Object argument) throws Throwable;
}
