package com.stratio.meta.server;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.RegularStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.stratio.meta.common.result.ConnectResult;
import com.stratio.meta.common.result.MetaResult;
import com.stratio.meta.common.result.QueryResult;
import com.stratio.meta.core.engine.Engine;
import com.stratio.meta.core.engine.EngineConfig;
import com.stratio.meta.core.planner.MetaPlan;
import com.stratio.meta.core.utils.MetaQuery;
import com.stratio.meta.core.validator.MetaValidation;
import org.apache.log4j.Logger;

public class MetaServer {
    
    private final Logger logger = Logger.getLogger(MetaServer.class);
    
    private Cluster cluster;
    private Session session;  

    public MetaServer() {
        connect();
    }

    public MetaResult connect(String host){
        try {            
            if(cluster == null){
                cluster = Cluster.builder().addContactPoint(host).build();
            }                

            if(session == null){
                session = cluster.connect();
            }
        } catch(Exception ex){
            ConnectResult connResult = new ConnectResult();
            connResult.setHasError();
            connResult.setErrorMessage(ex.getMessage());
            return connResult;
        }
        
        return new ConnectResult("Success");
    }
    
    public MetaResult connect(){
        return connect("127.0.0.1");
    }
            
    public boolean close(){
        if(session != null){
            session.close();
        }
        
        if(cluster != null){
            cluster.close();
        }       
        
        return true;
    }    
    
    public MetaResult executeQuery(String targetKs, Statement query) {
        return executeQuery(query.toString());
    }
    
    public QueryResult executeQuery(String query){
        EngineConfig config =new EngineConfig();
        config.setCassandraHosts(new String[]{"127.0.0.1"});
        config.setCassandraPort(9042);

        Engine engine = new Engine(config);
        // PARSER ACTOR    
        MetaQuery metaQuery = engine.getParser().parseStatement(query);
        if(metaQuery.hasError()){ // parser error
            return metaQuery.getResult();
        }
        // VALIDATOR ACTOR
        metaQuery = engine.getValidator().validateQuery(metaQuery);

        // PLANNER ACTOR
        MetaPlan metaPlan = engine.getPlanner().planQuery(metaQuery);
        metaQuery.setPlan(metaPlan);
        if(metaQuery.hasError()){ // Cannot plan
            return metaQuery.getResult();
        }                
        // EXECUTOR ACTOR
        QueryResult result = engine.getExecutor().executeQuery(metaQuery);
        return result;
    }

    public Metadata getMetadata() {
        return cluster.getMetadata();
    }

    public PreparedStatement prepare(String query) {
        return session.prepare(query);
    }

    public PreparedStatement prepare(RegularStatement rs) {
        return session.prepare(rs);
    }    
    
}
