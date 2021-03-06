package com.Safetynet.Repository.Impl;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.Safetynet.Exceptions.CustomExceptions.FirestationAlreadyExistsException;
import com.Safetynet.Exceptions.CustomExceptions.FirestationNotFoundByAddressException;
import com.Safetynet.Exceptions.CustomExceptions.FirestationNotFoundExceptions;
import com.Safetynet.Model.Firestations;
import com.Safetynet.Repository.IFirestationDAO;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Repository
public class FirestationDAO implements IFirestationDAO{
    private List<Firestations> firestationsList;
    private static final Logger LOGGER = LogManager.getLogger(FirestationDAO.class);

    public List<Firestations> getFirestationsList() {
        return firestationsList;
    }

    public void setFirestationsList(List<Firestations> firestationsList) {
        this.firestationsList = firestationsList;
       
    }

    @Override
    public Firestations findByNumber(Integer firestationNumber){
        return  firestationsList.stream()
            .filter(f -> f.getStation() == firestationNumber)
            .findAny().orElseThrow(()-> new FirestationNotFoundExceptions(firestationNumber));
    }

    @Override
    public Firestations addFirestations(Firestations firestations) {
        if(firestationsList.stream().anyMatch(f->f.getAddress().equals(firestations.getAddress()))){
            throw new FirestationAlreadyExistsException(firestations.getAddress());
        }else {
            firestationsList.add(firestations);
            LOGGER.info("Firestation  ajoutée");
            return firestations;
        }
    }

    @Override
    public Firestations editFirestations(Firestations firestations) {
        Firestations firestationToUpdate = firestationsList.stream()
            .filter(f -> f.getAddress().equalsIgnoreCase(firestations.getAddress()))
            .findAny().orElseThrow(()-> new FirestationNotFoundByAddressException(firestations.getAddress()));
        firestationsList.set(firestationsList.indexOf(firestationToUpdate),firestations);
        LOGGER.info("Firestation modifiée");
        return firestations;
    }

    @Override
    public void deleteFirestations(Firestations firestations) {
        Firestations firestationToDelete = firestationsList.stream()
                .filter(f -> f.getAddress().equals(firestations.getAddress()))
                .findAny().orElseThrow(() -> new FirestationNotFoundByAddressException(firestations.getAddress()));
        firestationsList.remove(firestationToDelete);
        LOGGER.info("Firestation  supprimée");
    }
}
