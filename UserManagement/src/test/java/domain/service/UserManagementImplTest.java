package domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import domain.model.Preference;
import eu.drus.jpa.unit.api.JpaUnit;

@ExtendWith(JpaUnit.class)
@ExtendWith(MockitoExtension.class)
class UserManagementImplTest {

    @Spy
    @PersistenceContext(unitName = "UserManagementPUTest")
    EntityManager em;

    @InjectMocks
    private UserManagementImplTest userManagementImplTest;

//    @Test
//    void testGetAll() {
//        int size = initDataStore();
//        assertEquals(size, userManagementImplTest.getAll().size());
//    }
//
//    private int initDataStore() {
//        int size = userManagementImplTest.getAll().size();
//        List<Preference> prefs = getRooms();
//        for (Preference r : prefs) {
//            em.persist(r);
//        }
//        return size + prefs.size();
//    }
}
