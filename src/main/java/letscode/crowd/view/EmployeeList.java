package letscode.crowd.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import letscode.crowd.component.EmployeeEditor;
import letscode.crowd.domain.Employee;
import letscode.crowd.repo.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class EmployeeList extends VerticalLayout {
    private final EmployeeRepo employeeRepo;

    private final EmployeeEditor employeeEditor;

    private Grid<Employee> employeeGrid= new Grid<>(Employee.class);
    private final TextField filter = new TextField();
    private final Button addNewButton = new Button("New employee", VaadinIcon.PLUS.create());
    private final HorizontalLayout toolbar = new HorizontalLayout(filter, addNewButton);

    @Autowired
    public EmployeeList(EmployeeRepo employeeRepo, EmployeeEditor employeeEditor) {
        this.employeeRepo = employeeRepo;
        this.employeeEditor = employeeEditor;

        filter.setPlaceholder("Type to filter");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(field -> fillList(field.getValue()));

        add(toolbar, employeeGrid, employeeEditor);

        employeeGrid
                .asSingleSelect()
                .addValueChangeListener(e -> employeeEditor.editEmployee(e.getValue()));

        addNewButton.addClickListener(e -> employeeEditor.editEmployee(new Employee()));

        employeeEditor.setChangeHandler(() -> {
            employeeEditor.setVisible(false);
            fillList(filter.getValue());
        });

        fillList("");
    }

    private void fillList(String name) {
        if (name.isEmpty()) {
            employeeGrid.setItems(this.employeeRepo.findAll());
        } else {
            employeeGrid.setItems(this.employeeRepo.findByName(name));
        }
    }
}
