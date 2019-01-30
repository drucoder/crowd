package letscode.crowd.repo;

import letscode.crowd.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    @Query("from Employee e " +
            "where " +
            "   concat(e.lastName, ' ', e.patronymic, ' ', e.firstName) like concat('%', :name, '%')")
    List<Employee> findByName(@Param("name") String name);
}
