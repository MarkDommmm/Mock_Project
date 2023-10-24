package project_final.controller;

import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import project_final.exception.CustomsException;
import project_final.model.dto.request.ReservationMenuRequest;
import project_final.model.dto.response.ReservationResponse;
import project_final.security.UserPrinciple;
import project_final.service.IReservationMenuService;
import project_final.service.IReservationService;

@Controller
@AllArgsConstructor
@RequestMapping("/reservationMenu")
public class ReservationMenuController {
    private final IReservationMenuService reservationMenuService;
    private final IReservationService reservationService;
    @GetMapping
    public String getReservationMenu(Model model,@RequestParam(defaultValue = "") String name,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "5") int size) {
        model.addAttribute("tableMenus", reservationMenuService.findAll(name,page,size));
        model.addAttribute("name", name);
        return "/dashboard/page/reservationMenus";
    }


    @GetMapping("/add")
    public String add(@RequestParam("idReservation") Long idR,
                      @RequestParam("idUser") Long idU,
                      Model model, @RequestParam(defaultValue = "") String name,
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "12") int size) throws CustomsException{
        model.addAttribute("carts",reservationMenuService.getDetails(idR));
        model.addAttribute("idR",idR);
        model.addAttribute("reservationMenu",new ReservationMenuRequest());
        return "dashboard/menu";
    }

    @PostMapping("/add")
    public String addTableMenu(@ModelAttribute("tableMenu") ReservationMenuRequest tableMenuRequest ) throws CustomsException {
        reservationMenuService.save(tableMenuRequest);
        return "redirect:/tableMenu";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model,@PathVariable Long id){
        model.addAttribute("tableMenu",reservationMenuService.findById(id));
        return "/dashboard/page/reservationMenus/add";
    }

    @PostMapping("/update")
    public String updateTableMenu(@ModelAttribute("tableMenu") ReservationMenuRequest tableMenuRequest ) throws CustomsException {
        reservationMenuService.save(tableMenuRequest);
        return "redirect:/tableMenu";
    }

    @GetMapping("/delete/{id}")
    public String deleteTableMenu(@PathVariable Long id) {
        reservationMenuService.delete(id);
        return "redirect:/tableMenu";
    }
}
