package letscode.crowd.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import letscode.crowd.domain.Employee;
import letscode.crowd.repo.EmployeeRepo;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;


@SpringComponent
@UIScope
public class EmployeeEditor extends VerticalLayout implements KeyNotifier {
    private final EmployeeRepo employeeRepo;

    private Employee employee;

    private TextField firstName = new TextField("", "First name");
    private TextField lastName = new TextField("", "Last name");
    private TextField patronymic = new TextField("", "Patronymic");

    private Button save = new Button("Save");
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");
    private HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);

    private Binder<Employee> binder = new Binder<>(Employee.class);
    @Setter
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    @Autowired
    public EmployeeEditor(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;

        add(lastName, firstName, patronymic, buttons);

        binder.bindInstanceFields(this);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editEmployee(employee));
        setVisible(false);
    }

    private void save() {
        employeeRepo.save(employee);
        changeHandler.onChange();
    }

    private void delete() {
        employeeRepo.delete(employee);
        changeHandler.onChange();
    }

    public void editEmployee(Employee emp) {
        if (emp == null) {
            setVisible(false);
            return;
        }

        if (emp.getId() != null) {
            this.employee = employeeRepo.findById(emp.getId()).orElse(emp);
        } else {
            this.employee = emp;
        }

        binder.setBean(this.employee);

        setVisible(true);

        lastName.focus();
    }
}
