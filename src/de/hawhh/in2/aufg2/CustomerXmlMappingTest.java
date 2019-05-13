package de.hawhh.in2.aufg2;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Test;

import de.hawhh.in2.aufg2.Customer;

class CustomerXmlMappingTest
{


	@Test
	void testInsert()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("haw_demo2");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		em.createQuery("Delete from Customer").executeUpdate();
		final Customer test1 = new Customer("122", "Mr.", "Heckenschere", "12.04.2001");
		em.persist(test1);
		em.getTransaction().commit();
		
		assertEquals(em.find(Customer.class, "122").getID(), "122");
		assertEquals(em.find(Customer.class, "122").getFirstName(), "Mr.");
		assertEquals(em.find(Customer.class, "122").getFamilyName(), "Heckenschere");
		assertEquals(em.find(Customer.class, "122").getEntryDate(), "12.04.2001");

	}
	
	@Test
	void testUpdate()
	{
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("haw_demo2");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		em.createQuery("Delete from Customer").executeUpdate();
		final Customer test1 = new Customer("122", "Mr.", "Heckenschere", "12.04.2001");
		em.persist(test1);
		em.getTransaction().commit();
		
		em.getTransaction().begin();
		//em.find(Customer.class, "122").setID("222");
		em.find(Customer.class, "122").setFirstName("Neuer");
		em.find(Customer.class, "122").setFamilyName("Custo");
		em.find(Customer.class, "122").setEntryDate("13.04.2001");
		em.getTransaction().commit();
		
		//assertEquals(em.find(Customer.class, "122").getID(), "222");
		assertEquals(em.find(Customer.class, "122").getFirstName(), "Neuer");
		assertEquals(em.find(Customer.class, "122").getFamilyName(), "Custo");
		assertEquals(em.find(Customer.class, "122").getEntryDate(), "13.04.2001");
		
//		em.getTransaction().begin();
//		em.find(Customer.class, "122").setID("222");
//		em.getTransaction().commit();
//		assertEquals(em.find(Customer.class, "122").getID(), "222");

	}

}
