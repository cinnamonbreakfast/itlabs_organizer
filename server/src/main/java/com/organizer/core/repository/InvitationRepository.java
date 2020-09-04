package com.organizer.core.repository;

import com.organizer.core.model.Invitation;
import com.organizer.core.model.Service;
import com.organizer.core.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface InvitationRepository extends  Repository<Long, Invitation>{

    List<Invitation> findByUserAndAccepted(User user,Boolean accepted);

    Invitation findByUserAndServiceAndAccepted(User user , Service service, Boolean accepted);

    Invitation findByUserAndService(User user, Service service);

}
