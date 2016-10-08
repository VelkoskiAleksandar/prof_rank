package com.diplomska.prof_rank.pages.admin.SubjectDomain;

import com.diplomska.prof_rank.annotations.AdministratorPage;
import com.diplomska.prof_rank.entities.SubjectDomain;
import com.diplomska.prof_rank.services.SubjectDomainHibernate;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by Aleksandar on 25-Sep-16.
 */
@AdministratorPage
public class ShowSubjectDomain {
    @Persist
    @Property
    private Long subjectDomainId;

    @Inject
    private SubjectDomainHibernate subjectDomainHibernate;

    @Persist
    @Property
    private SubjectDomain subjectDomain;

    void onActivate(Long subjectDomainId) {
        this.subjectDomainId = subjectDomainId;
    }

    Long passivate() {
        return subjectDomainId;
    }

    void setupRender() throws Exception {
        this.subjectDomain = subjectDomainHibernate.getById(subjectDomainId);
    }
}
