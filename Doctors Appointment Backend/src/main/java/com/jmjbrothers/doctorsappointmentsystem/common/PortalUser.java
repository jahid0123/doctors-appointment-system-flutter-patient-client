package com.jmjbrothers.doctorsappointmentsystem.common;

import com.jmjbrothers.doctorsappointmentsystem.constants.Role;

public interface PortalUser {

    Long getId();
    String getEmail();
    String getPassword();
    Role getRole();
    String getName();
}
