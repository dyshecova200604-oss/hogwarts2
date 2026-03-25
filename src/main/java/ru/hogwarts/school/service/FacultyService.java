package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

@Service
public class FacultyService{

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService( FacultyRepository facultyRepository ){
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty( Faculty faculty){
        logger.info("Was invoked method for createFaculty {}", faculty.getName());
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id){
        logger.info("Was invoked method for deleteFaculty {}", id);
        facultyRepository.deleteById(id);
    }

    public Faculty findFaculty(long id){
        logger.info("Was invoked method for findFaculty {}", id);
        return facultyRepository.findById( id ).get();
    }

    public Faculty editFaculty(Faculty faculty){
        logger.info("Was invoked method for editFaculty {}", faculty.getName());
        return facultyRepository.save(faculty);
    }

    public Collection<Faculty> getAllFaculty(){
        logger.info("Was invoked method for getAllFaculty");
        return facultyRepository.findAll();
    }

    public Faculty filteredFacultyByColor(String color){
        logger.info("Was invoked method for filteredFacultyByColor {}", color);
        return facultyRepository.findByColor( color );
    }

    public Collection<Faculty> findFacultyByName(String name){
        logger.info("Was invoked method for filteredFacultyByName {}", name);
        return facultyRepository.findFacultyByNameIgnoreCase(name);
    }

    public Collection<Faculty> findFacultyByColor(String color){
        logger.info("Was invoked method for filteredFacultyByColor {}", color);
        return facultyRepository.findFacultyByColorIgnoreCase(color);
    }

    public Faculty getFacultiesByStudentsName(String name){
        logger.info("Was invoked method for getFacultiesByStudentsName {}", name);
        return facultyRepository.getFacultiesByStudentsName(name);
    }
}