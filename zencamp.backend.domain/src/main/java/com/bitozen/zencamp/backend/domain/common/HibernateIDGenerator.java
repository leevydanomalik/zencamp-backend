package com.bitozen.zencamp.backend.domain.common;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HibernateIDGenerator implements IdentifierGenerator, Configurable {

    private static final Logger LOG = Logger.getLogger(HibernateIDGenerator.class.getName());
    
    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {

    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        String query = String.format("select max(%s) from zencamp_%s", session.getEntityPersister(object.getClass().getName(), object).getIdentifierPropertyName(), object.getClass().getSimpleName());
        Connection connection = session.connection();
        long generatedId=0;
        session.connection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next()) {
                generatedId = rs.getInt(1) + 1;
                LOG.log(Level.INFO, "GENERATED IDS ----> {0} <---- for {1}", new Object[]{generatedId, object.getClass().getSimpleName()});
            }
        } catch (SQLException ex) {
            LOG.log(Level.INFO, ex.getMessage());
        }
        return generatedId;
    }
}
