package mk.ukim.finki.isis.edubio.pages.admin.Role;

import mk.ukim.finki.isis.edubio.annotations.AdministratorPage;
import mk.ukim.finki.isis.edubio.entities.Role;
import mk.ukim.finki.isis.edubio.services.RoleHibernate;
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
    private RoleHibernate roleHibernate;

    @Property
    private Role role;

    @Property
    private BeanModel<Role> roleBeanModel;

    @Inject
    private BeanModelSource beanModelSource;

    @Inject
    private Messages messages;

    @Inject
    private PropertyConduitSource pcs;

    public List<Role> getRoles() {
        return roleHibernate.getAll();
    }

    void setupRender() {
        roleBeanModel = beanModelSource.createDisplayModel(Role.class, messages);
        roleBeanModel.include("name");
        roleBeanModel.add("show", null);
        roleBeanModel.add("edit", null);
        roleBeanModel.add("delete", null);
    }

    @CommitAfter
    void onActionFromDelete(Long roleId) {
        role = roleHibernate.getById(roleId);
        roleHibernate.delete(role);
    }
}
