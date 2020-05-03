package com.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
	private static final SessionFactory sessionFactory = setSessionFactory();

	private static SessionFactory setSessionFactory() {
		SessionFactory sessionFactoryy = null;
		try {
			sessionFactoryy = new Configuration().configure().buildSessionFactory();
		} catch (Exception e) {
			System.out.print("Error" + e);
		}
		return sessionFactoryy;
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static Session openSession() {
		return getSessionFactory().openSession();
	}


}
