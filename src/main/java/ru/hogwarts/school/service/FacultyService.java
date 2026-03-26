package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.stream.LongStream;
import java.util.stream.Stream;
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
    public Faculty getFacultyLongestName(){
        logger.info("Was invoked method for getFacultyLongestName");
        return facultyRepository.findAll().stream()
                .max( (faculty1, faculty2) -> faculty1.getName().length() - faculty2.getName().length() )
                .orElse(null);
    }
    public Long example4() {
        logger.info("Was invoked method for example4");
        long startTime1 = System.currentTimeMillis();
        long sum1 = Stream.iterate(1,a -> a +1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b );
        long endTime1 = System.currentTimeMillis();
        long duration1 = endTime1 - startTime1;
        long startTime2 = System.currentTimeMillis();
        long sum2 = LongStream
                .rangeClosed(1, 1_000_000)
                .parallel()
                .sum();
        long endTime2 = System.currentTimeMillis();
        long duration2 = endTime2 - startTime2;
        logger.info("Example4: duration1 = {}, duration2 = {}", duration1, duration2);
        logger.info("Example4: sum1 = {}, sum2 = {}", sum1, sum2);
        return sum2;
    }
}
