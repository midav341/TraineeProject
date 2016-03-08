package db;

import java.io.File;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory; 
import org.hibernate.cfg.AnnotationConfiguration;

import controller.SpringController; 
 
public class HibernateUtil { 
    private static final SessionFactory sessionFactory = buildSessionFactory();
	public static final Logger LOG=Logger.getLogger(SpringController.class);
	
    private static SessionFactory buildSessionFactory() { 
        try { 
            return new AnnotationConfiguration().configure( 
                    new File(SessionFactory.class.getClassLoader().getResource(
                    		"hibernate.cfg.xml"
                    		).getFile())
                    ).buildSessionFactory(); 
 
        } catch (Throwable ex) { 
        	LOG.error("Initial SessionFactory creation failed." + ex); 
            throw new ExceptionInInitializerError(ex); 
        } 
    } 
 
    public static SessionFactory getSessionFactory() { 
        return sessionFactory; 
    } 
 
    public static void shutdown() { 
        getSessionFactory().close(); 
    } 
} 