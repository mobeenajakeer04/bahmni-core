package org.bahmni.module.drugorderrelationship.dao.impl;

import org.bahmni.module.drugorderrelationship.dao.DrugOrderRelationshipDao;
import org.bahmni.module.drugorderrelationship.model.DrugOrderRelationship;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.openmrs.Concept;
import org.openmrs.DrugOrder;
import org.openmrs.Obs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DrugOrderRelationshipDaoImpl implements DrugOrderRelationshipDao {
    /**
     * Hibernate session factory
     */

    private SessionFactory sessionFactory;

    /**
     * Set session factory
     *
     * @param sessionFactory
     */
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    @Transactional
    public DrugOrderRelationship saveOrUpdate(DrugOrderRelationship obsRelationship) {
        sessionFactory.getCurrentSession().saveOrUpdate(obsRelationship);
        return obsRelationship;
    }



    @Override
    @Transactional
    public DrugOrderRelationship getRelationById(int id) {
        Query query = sessionFactory.getCurrentSession().createQuery("from ObsRelationship where uuid=:uuid");
        query.setInteger("id",id);
        List<DrugOrderRelationship> list = query.list();
        if(list.size() != 0){
            return list.get(0);
        }
        return null;
    }

    @Override
    @Transactional
    public List<DrugOrderRelationship> getDrugOrdersByCategory( Concept category) {
        Query query = createGetRelationsQueryForCategory(category);
        List<DrugOrderRelationship> drugOrderRelationshipList = query.list();
        return drugOrderRelationshipList;
    }

    @Override
    @Transactional
    public List<DrugOrderRelationship> getDrugOrdersByTreatmentLine( Concept treatmentLine) {
        Query query = createGetRelationsQueryForCategory(treatmentLine);
        List<DrugOrderRelationship> drugOrderRelationshipList = query.list();
        return drugOrderRelationshipList;
    }


    @Override
    @Transactional
    public List<DrugOrderRelationship> getRelationshipByOrder( DrugOrder order) {
        Query query = createGetRelationsQueryForOrder(order);
        List<DrugOrderRelationship> drugOrderRelationshipList = query.list();
        return drugOrderRelationshipList;
    }



    private Query createGetRelationsQueryForTreatmentLine(Concept treatmentLine) {
        Query query = null;
        if(treatmentLine == null){
            throw new IllegalArgumentException("Category cannot be null");
        }
        else{
            query = sessionFactory.getCurrentSession().createQuery("from DrugOrderRelationship where treatmentLine=:treatmentLine ");
            query.setInteger("treatmentLine", treatmentLine.getId());

        }
        return query;
    }


    private Query createGetRelationsQueryForCategory(Concept category) {
        Query query = null;
        if(category == null){
            throw new IllegalArgumentException("Category cannot be null");
        }
        else{
            query = sessionFactory.getCurrentSession().createQuery("from DrugOrderRelationship where category=:category ");
            query.setInteger("category", category.getId());

        }
        return query;
    }


    private Query createGetRelationsQueryForOrder(DrugOrder drugOrder) {
        Query query = null;
        if(drugOrder == null){
            throw new IllegalArgumentException("DrugOrder  cannot be null");
        }
        else{
            query = sessionFactory.getCurrentSession().createQuery("from DrugOrderRelationship where drugOrder=:drugOrder ");
            query.setInteger("drugOrder", drugOrder.getId());

        }
        return query;
    }

}
