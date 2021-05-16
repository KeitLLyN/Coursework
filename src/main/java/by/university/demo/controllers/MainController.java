package by.university.demo.controllers;

import by.university.demo.entity.Message;
import by.university.demo.entity.Role;
import by.university.demo.entity.User;
import by.university.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    private final MessageService messageService;

    @Autowired
    public MainController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/main")
    public String main(@AuthenticationPrincipal User user,
                       @RequestParam(required = false, defaultValue = "") String filter,
                       Model model) {
        Iterable<Message> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = messageService.findByTag(filter);
        } else {
            messages = messageService.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        model.addAttribute("isAdmin", user.isAdmin());
        return "main";
    }

    @PostMapping("/main")
    public String add(@AuthenticationPrincipal User user,
                      @ModelAttribute Message message) {
        message.setAuthor(user);
        messageService.save(message);
        return "redirect:/main";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String delete(@PathVariable("id") Long id,
                         @RequestParam Long user_id,
                         @AuthenticationPrincipal User user) {
        if (user_id.equals(user.getId()) || user.getRoles().contains(Role.ADMIN)) {
            messageService.deleteById(id);
        }
        return "redirect:/main";
    }

    @ModelAttribute("message")
    public Message message() {
        return new Message();
    }
}
