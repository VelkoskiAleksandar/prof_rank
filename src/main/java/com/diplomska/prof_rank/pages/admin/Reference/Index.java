package com.diplomska.prof_rank.pages.admin.Reference;

import com.diplomska.prof_rank.annotations.AdministratorPage;
import com.diplomska.prof_rank.entities.Reference;
import com.diplomska.prof_rank.services.ExcelWorkbook;
import com.diplomska.prof_rank.services.ReferenceHibernate;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.PropertyConduitSource;

import java.util.List;

/**
 * Created by Aleksandar on 24-Sep-16.
 */
@AdministratorPage
public class Index {
    @Inject
    private ReferenceHibernate referenceHibernate;

    @Inject
    private ExcelWorkbook excelWorkbook;

    @Property
    private Reference reference;

    @Property
    private BeanModel<Reference> referenceBeanModel;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private Messages messages;

    @Inject
    private PropertyConduitSource pcs;

    public List<Reference> getReferences() {
        return referenceHibernate.getAll();
    }

    void setupRender() {
        referenceBeanModel = beanModelSource.createDisplayModel(Reference.class, messages);
        referenceBeanModel.include("name", "points");
        referenceBeanModel.add("referenceType", pcs.create(Reference.class, "referenceType"));
        referenceBeanModel.add("show", null);
        referenceBeanModel.add("edit", null);
        referenceBeanModel.add("delete", null);
    }

    @CommitAfter
    void onActionFromDelete(Long referenceId) {
        reference = referenceHibernate.getById(referenceId);
        referenceHibernate.delete(reference);
    }

    @CommitAfter
    void onActionFromReadExcel() throws Exception {
        excelWorkbook.readCategorySpreadsheet("poi_test.xlsx", 1);
        excelWorkbook.readCategorySpreadsheet("poi_test.xlsx", 2);
        excelWorkbook.readCategorySpreadsheet("poi_test.xlsx", 3);
    }
}
