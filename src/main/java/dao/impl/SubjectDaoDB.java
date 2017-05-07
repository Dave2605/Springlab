package dao.impl;

import dao.SubjectDao;
import entities.Subject;
import entities.Teacher;
import exceptions.DataFetchingException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubjectDaoDB implements SubjectDao {

    @Autowired
    SessionFactory sessionFactory;

    Session session;

    public List<Subject> getAll() throws DataFetchingException {
        List<Subject> subjectList;
        try {
            session = sessionFactory.openSession();
            subjectList = session.createQuery("FROM Subject ").list();
            session.close();
        } catch (Exception e) {
            session.close();
            throw new DataFetchingException();
        }
        return subjectList;
    }

    public Subject getSubject(Integer id) throws DataFetchingException {
        Subject subject;
        try {
            session = sessionFactory.openSession();
            subject = (Subject) session.get(Subject.class, id);
            session.close();
        } catch (Exception e) {
            session.close();
            throw new DataFetchingException();
        }
        return subject;
    }

    public void createSubject(String name, String subjectGroup, Integer passScore, Integer teacherId) throws DataFetchingException {
        try {
            session = sessionFactory.openSession();

            Teacher teacher = session.get(Teacher.class, teacherId);
            Subject subject = new Subject(name, subjectGroup, passScore, teacher);

            Transaction transaction = session.beginTransaction();

            session.save(subject);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            session.close();
            throw new DataFetchingException();
        }
    }

    public void updateSubject(Integer id, String name, String subjectGroup, Integer passScore, Integer teacherId) throws DataFetchingException {
        try {
            session = sessionFactory.openSession();

            Teacher teacher = session.get(Teacher.class, teacherId);
            Subject subject = session.load(Subject.class, id);

            Transaction transaction = session.beginTransaction();

            subject.setName(name);
            subject.setSubjectGroup(subjectGroup);
            subject.setPassScore(passScore);
            subject.setTeacher(teacher);

            transaction.commit();
            session.close();
        } catch (Exception e) {
            session.close();
            throw new DataFetchingException();
        }
    }

    public void deleteSubject(Integer id) throws DataFetchingException {
        try {
            session = sessionFactory.openSession();
            Subject subject = session.load(Subject.class, id);

            Transaction transaction = session.beginTransaction();
            session.delete(subject);
            transaction.commit();

            session.close();
        } catch (Exception e) {
            session.close();
            throw new DataFetchingException();
        }
    }
}
