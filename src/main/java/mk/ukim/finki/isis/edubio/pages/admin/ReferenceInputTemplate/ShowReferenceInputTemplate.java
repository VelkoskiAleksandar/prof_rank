package mk.ukim.finki.isis.edubio.pages.admin.ReferenceInputTemplate;

import mk.ukim.finki.isis.edubio.annotations.AdministratorPage;
import mk.ukim.finki.isis.edubio.entities.Attribute;
import mk.ukim.finki.isis.edubio.entities.ReferenceInputTemplate;
import mk.ukim.finki.isis.edubio.services.AttributeHibernate;
import mk.ukim.finki.isis.edubio.services.ReferenceInputTemplateHibernate;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.PropertyConduitSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksandar on 25-Sep-16.
 */
@AdministratorPage
public class ShowReferenceInputTemplate {
    @Persist
    @Property
    private Long referenceInputTemplateId;

    @Inject
    private ReferenceInputTemplateHibernate referenceInputTemplateHibernate;

    @Inject
    private AttributeHibernate attributeHibernate;

    @Persist
    @Property
    private ReferenceInputTemplate referenceInputTemplate;

    @Property
    private Attribute addAttribute;

    @Property
    private Attribute attribute;

    @Property
    private BeanModel<Attribute> attributeBeanModel;

    @Property
    private BeanModel<Attribute> addAttributeBeanModel;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private Messages messages;

    @Inject
    private PropertyConduitSource pcs;

    public List<Attribute> getAddAttributes() {
        return attributeHibernate.getAll();
    }

    public List<Attribute> getAttributes() {
        List<Attribute> attributeReferenceInputTemplates = new ArrayList<Attribute>();

        attributeReferenceInputTemplates.addAll(referenceInputTemplateHibernate.getAttributes(referenceInputTemplate));

        return attributeReferenceInputTemplates;
    }

    void onActivate(Long referenceInputTemplateId) {
        this.referenceInputTemplateId = referenceInputTemplateId;
    }

    Long passivate() {
        return referenceInputTemplateId;
    }

    void setupRender() throws Exception {
        this.referenceInputTemplate = referenceInputTemplateHibernate.getById(referenceInputTemplateId);

        if (referenceInputTemplate == null) {
            throw new Exception("ReferenceInputTemplate " + referenceInputTemplateId + " does not exist.");
        }

        attributeBeanModel = beanModelSource.createDisplayModel(Attribute.class, messages);
        attributeBeanModel.add("attributeName", pcs.create(Attribute.class, "name"));
        attributeBeanModel.add("attributeInputType", pcs.create(Attribute.class, "inputType"));
        attributeBeanModel.add("delete", null);

        addAttributeBeanModel = beanModelSource.createDisplayModel(Attribute.class, messages);
        addAttributeBeanModel.include("name", "inputType");
        addAttributeBeanModel.add("add", null);
    }

    @CommitAfter
    void onActionFromAdd(Long attributeId) {
        attribute = attributeHibernate.getById(attributeId);

        referenceInputTemplateHibernate.setAttribute(referenceInputTemplate, attribute);
    }

    @CommitAfter
    void onActionFromDelete(Long attributeId) {
        Attribute attr = attributeHibernate.getById(attributeId);

        referenceInputTemplateHibernate.deleteAttribute(referenceInputTemplate, attr);
    }
}
