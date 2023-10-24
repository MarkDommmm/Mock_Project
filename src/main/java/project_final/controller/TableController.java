package project_final.controller;


import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import project_final.exception.CustomsException;
import project_final.model.dto.request.TableRequest;
import project_final.model.dto.response.TableResponse;
import project_final.service.ITableService;
import project_final.service.ITableTypeService;

import javax.servlet.http.HttpSession;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;


@Controller
@AllArgsConstructor
@RequestMapping("/table")
public class TableController {
    private final ITableService tableService;

    private final ITableTypeService tableTypeService;

    @GetMapping
    public String findAll(@RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "5") int size,
                          @RequestParam(defaultValue = "") String name,
                          Model model) {
        model.addAttribute("tables", tableService.findAll(name, page, size));
        model.addAttribute("name", name);
        return "/dashboard/page/table/table-list";
    }

    @GetMapping("/details")
    public String getDetailsTable(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(name = "date", required = false)
                                  @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
                                  @RequestParam(name = "start",required = false, defaultValue = "") String start,
                                  @RequestParam(name = "end",required = false, defaultValue = "") String end,
                                  Model model) {
        if (date == null) {
            date = new Date();
        }
        model.addAttribute("date", date);
        model.addAttribute("start", start);
        model.addAttribute("end", end);
        model.addAttribute("objects", tableService.getTableStatusForDate(date, start, end, page, size));
        return "/dashboard/page/table/table-details";
    }


    @GetMapping("/add")
    public String add(Model model) {
        model.addAttribute("tables", new TableRequest());
        model.addAttribute("tableTypes", tableTypeService.findAll());
        return "/dashboard/page/table/table-add";

    }

    @PostMapping("/add")
    public String addTable(@ModelAttribute TableRequest tableRequest) throws CustomsException {
        tableService.save(tableRequest);
        return "redirect:/table";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, HttpSession session) {
        TableResponse table = tableService.findById(id);
        model.addAttribute("tables", table);
        session.setAttribute("oldImage", table.getTableImage());
        model.addAttribute("tableTypes", tableTypeService.findAll());
        return "/dashboard/page/table/table-update";

    }

    @PostMapping("/update")


    public String updateTable(@ModelAttribute("tables") TableRequest tableRequest, Model model) {
        tableService.save(tableRequest);
        return "redirect:/table";
    }

    @GetMapping("/delete/{id}")
    public String deleteTable(@PathVariable Long id) {
        tableService.delete(id);
        return "redirect:/table";
    }
    @GetMapping("/delete-home/{id}")
    public String deleteTableh(@PathVariable Long id) {
        tableService.delete(id);
        return "redirect:/home";
    }

    @GetMapping("status/{id}")
    public String changeStatus(@PathVariable Long id) {
        tableService.changeStatus(id);
        return "redirect:/table";
    }

}
