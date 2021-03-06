package mk.ukim.finki.isis.edubio.model;

import mk.ukim.finki.isis.edubio.entities.Role;
import mk.ukim.finki.isis.edubio.services.PersonHibernate;
import mk.ukim.finki.isis.model.entities.Person;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aleksandar on 07-Oct-16.
 */
public class UserInfo {
    enum UserRole {
        INSTRUCTOR, ADMINISTRATOR, NONE
    };

    private String userName;
    private Long personId;
    private List<UserRole> userRoles;
    private int roleCount;
    private UserRole selectedRole;
//    private GenericService genericService;
    private PersonHibernate personHibernate;
    private Long institutionId;
    private Long adminId;
    private Long instructorId;
    private Logger logger;
    private Role role;
    private Person person;

    public UserInfo(@Inject PersonHibernate personHibernate,
//                    @Inject GenericService genericService,
                    @Inject RequestGlobals requestGlobals
//                    ,@Inject Logger logger
    )
            throws Exception {
        this.personHibernate = personHibernate;
//        this.genericService = genericService;
        this.logger = logger;
        roleCount = 0;

        if (requestGlobals != null) {
            if (requestGlobals.getHTTPServletRequest().getRemoteUser() != null) {
                this.userName = requestGlobals.getHTTPServletRequest()
                        .getRemoteUser();
                this.setupUser();
            } else {
                this.userName = null;
            }
        } else {
            this.userName = null;
        }
    }

    private void setupUser() throws Exception {
        if (userName != null) {
//            logger.info("Logged in user: " + userName);
            userRoles = new ArrayList<UserRole>();
            person = personHibernate.getPersonByUsername(userName);

            if (person == null) {
                throw new Exception("No such person in system. Username: " + userName);
            }

            this.personId = new Long(person.getPersonId());

            if (personId != null) {
                role = personHibernate.getRoleByPersonId(personId);
                if (role.getName().toLowerCase().equals("admin")) {
                    userRoles.add(UserRole.ADMINISTRATOR);
                } else if (role.getName().toLowerCase().equals("instructor")) {
                    userRoles.add(UserRole.INSTRUCTOR);
                }
                roleCount++;

                selectedRole = userRoles.get(0);
//                logger.info("Logged in user: " + userName
//                        + " has only one role: " + selectedRole
//                        + ", so the role is activated.");

                if (selectedRole == UserRole.INSTRUCTOR) {
                    setUserRoleInstructor(personId);
                } else if (selectedRole == UserRole.ADMINISTRATOR) {
                    setUserRoleAdmin(personId);
                } else {
                    setNullAllRolesId();
                }
            }

        }
    }

    public String getUserName() {
        return userName;
    }

    public Long getPersonId() {
        return personId;
    }

    public int getRoleCount() {
        return roleCount;
    }

    public boolean isInstructor() {
        return selectedRole == UserRole.INSTRUCTOR;
    }

    public boolean isAdministrator() {
        return userRoles.contains(UserRole.ADMINISTRATOR);
    }

    public boolean isAdmin() {
        return selectedRole == UserRole.ADMINISTRATOR;
    }

    public boolean isNone() {
        return selectedRole == UserRole.NONE;
    }

    public void setUserRoleInstructor(long instructorId) {
        setNullAllRolesId();

        this.instructorId = instructorId;
    }

    public void setUserRoleAdmin(long staffId) {
        setNullAllRolesId();

        this.adminId = staffId;
    }

    private void setNullAllRolesId() {
        instructorId = null;
        adminId = null;
    }

    public Long getAdminId() {
        return adminId;
    }

    public Long getInstructorId() {
        return instructorId;
    }

    public void impersonate(String inUsername) throws Exception {
        this.userName = inUsername;
        this.setupUser();
    }

    public Role getRole() {
        return role;
    }

    public Person getPerson() {
        return person;
    }
}
