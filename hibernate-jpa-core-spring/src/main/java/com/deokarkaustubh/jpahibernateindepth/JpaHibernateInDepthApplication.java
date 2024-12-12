package com.deokarkaustubh.jpahibernateindepth;

import com.deokarkaustubh.jpahibernateindepth.entity.Course;
import com.deokarkaustubh.jpahibernateindepth.entity.Passport;
import com.deokarkaustubh.jpahibernateindepth.entity.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class JpaHibernateInDepthApplication {

    private SessionFactory sessionFactory;

    private void instantiateHibernate() {
        // configures settings from hibernate.cfg.xml
        initSessionFactory();
        performSessionOperations();
    }

    private void performSessionOperations() {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Passport passport1 = new Passport();
        passport1.setNumber("p101");
        Passport passport2 = new Passport();
        passport2.setNumber("p101");

        Student student = new Student();
        student.setPassport(passport1);
        student.setName("stu1");
        Student student2 = new Student();
        student2.setPassport(passport2);
        student2.setName("stu2");


        Course course = new Course();
        course.setName("c1");
        Course course2 = new Course();
        course2.setName("c2");

        student.getCourseList().add(course);
        student2.getCourseList().add(course2);

        session.getTransaction().commit();
        session.close();
    }


    private void initSessionFactory() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        } finally {
            sessionFactory.close();
        }
    }

    public static void main(String[] args) {
        JpaHibernateInDepthApplication main = new JpaHibernateInDepthApplication();
        main.instantiateHibernate();
    }
}