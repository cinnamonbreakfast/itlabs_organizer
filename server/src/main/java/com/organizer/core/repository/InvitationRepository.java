package com.organizer.core.repository;

import com.organizer.core.model.Invitation;
import com.organizer.core.model.User;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvitationRepository extends  Repository<Long, Invitation>{

    List<Invitation> findByUserAndAccepted(User user,Boolean accepted);
}
