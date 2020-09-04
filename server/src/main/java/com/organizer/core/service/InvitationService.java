package com.organizer.core.service;

import com.organizer.core.model.Invitation;
import com.organizer.core.model.User;
import com.organizer.core.repository.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitationService {

    InvitationRepository invitationRepository;
    @Autowired
    public InvitationService(InvitationRepository invitationRepository){
        this.invitationRepository= invitationRepository;
    }

    public void save(Invitation invitation){
        invitationRepository.save(invitation);
    }

    public List<Invitation> findByUserAndAccepted(User user ){
        return invitationRepository.findByUserAndAccepted(user,false);
    }

    public Invitation findById(Long id ){
        return invitationRepository.findById(id).get();
    }

    public Invitation findByUserAndServiceAndAccepted(User user , com.organizer.core.model.Service service , Boolean accepted){
        return invitationRepository.findByUserAndServiceAndAccepted(user,service,accepted);
    }

    public Invitation findByUserAndService(User user , com.organizer.core.model.Service service){
        return invitationRepository.findByUserAndService(user , service);
    }


}
