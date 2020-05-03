package com.model.app;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;

import com.model.map.Student;
import com.model.map.StudentInfo;
import com.util.HibernateUtil;

public class App {

	public static void main(String[] args) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;

		try {
			transaction = session.beginTransaction();

			Student student = new Student();
			student.setName("Ali");
			student.setSureName("Kaya");

			StudentInfo studentInfo = new StudentInfo();
			studentInfo.setAddress("London");
			studentInfo.setBirthDate(new Date());
			studentInfo.setEmail("aa.gmail.com");
			studentInfo.setStudent(student);
			studentInfo.setTelNumber("1941");
			student.setStudentInfo(studentInfo);

			session.save(student);

			// session.getTransaction().commit();
			// did not save studentInfo but cascade type save it
			transaction.commit();

			System.out.println("Success" + student.getId());

			List<Student> students = getAllPersonel();
			System.out.println(" Student List Size = " + students.size());
			for (Student student2 : students) {
				System.out.println(" Student = " + student2.getName() + " --- " + student2.getSureName());
			}

			Student st = getAllPersonelByID(1);
			System.out.println("Student criteris = " + st.getName());

            List<Student> studentLists = getAllPersonelByName("Ali");
            System.out.println(" studentLists List Size = " + students.size());
			for (Student student2 : studentLists) {
				System.out.println(" studentLists = " + student2.getName() + " --- " + student2.getSureName());
			}

		} catch (HibernateException e) {
			transaction.rollback();
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
	}

	private static List<Student> getAllPersonel() {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		List<Student> students = new ArrayList<Student>();

		try {
			transaction = session.beginTransaction();
			String hql = "SELECT st FROM Student as st where 1=1 and st.id > :idParam order by st.id desc";
			/// Also you can use StringBuilder
			Query<?> query = session.createQuery(hql);
			query.setParameter("idParam", 0);
			List<?> studentList = query.list();

			// List<?> studentList = session.createQuery("SELECT st FROM Student as st where
			// 1=1 AND st.id is not null").list();

			for (Iterator<?> iterator = studentList.iterator(); iterator.hasNext();) {
				Student student = (Student) iterator.next();
				students.add(student);
			}
			transaction.commit();
		} catch (Exception e) {
			throw (e);
		} finally {
			session.close();
		}
		return students;
	}

	private static Student getAllPersonelByID(Integer id) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		Student student = new Student();

		try {
			transaction = session.beginTransaction();
			Criteria criteria = session.createCriteria(Student.class)
					.add(Restrictions.eq("id", id))
					.add(Restrictions.like("name", "%li%"))
					.setFirstResult(0)
					.setMaxResults(2)
					//.setProjection(Projections.max("id"))  //return max id
					.addOrder(Order.desc("id"));
			List <Student> studentList = (List<Student>) criteria.list();
			if(studentList.size() > 0) {
				student = studentList.get(0);
			}
			transaction.commit();
		} catch (Exception e) {
			throw (e);
		} finally {
			session.close();
		}
		return student;

	}
	
	private static List<Student>  getAllPersonelByName(String name) {
		Session session = HibernateUtil.openSession();
		Transaction transaction = null;
		List<Student> studentLists = new ArrayList<Student>();

		try {
			transaction = session.beginTransaction();
			Query<?> query = session.createSQLQuery("SELECT * from Student WHERE name = ?")
					.addEntity(Student.class);
			query.setParameter(1, name);
			
			List<?> studentList = query.list();
			
			for (Iterator<?> iterator = studentList.iterator(); iterator.hasNext();) {
				Student student = (Student) iterator.next();
				studentLists.add(student);
			}
			transaction.commit();
		} catch (Exception e) {
			throw (e);
		} finally {
			session.close();
		}
		return studentLists;
	}
}
