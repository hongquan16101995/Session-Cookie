package cg.controller;

import cg.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@SessionAttributes("user")
public class HomeController {

    @ModelAttribute("user")
    public User setUpUserForm() {
        return new User();
    }

    @GetMapping("/login")
    public String Index(@CookieValue(value = "setUser", defaultValue = "") String setUser, Model model, HttpSession session) {
        Cookie cookie = new Cookie("setUser", setUser);
        cookie.setValue("");
//        User user = (User)session.getAttribute("userSession");
//        System.out.println(user);
        model.addAttribute("cookieValue", cookie);
        return "/index";
    }

//    @GetMapping("/test")
//    public String test(@SessionAttribute("user") User user, Model model) {
//        model.addAttribute("userTest", user);
//        System.out.println(user);
//        return "/test";
//    }

    @PostMapping("/doLogin")
    public String doLogin(@ModelAttribute("user") User user, Model model,
                          @CookieValue(value = "setUser", defaultValue = "") String setUser,
                          HttpServletResponse response, HttpServletRequest request) {
        //implement business logic
        if (user.getAccount().equals("admin@gmail.com") && user.getPass().equals("123456")) {
            if (user.getAccount() != null)
                setUser = user.getAccount();

            // create cookie and set it in response
            Cookie cookie = new Cookie("setUser", setUser);
            cookie.setMaxAge(1000);
            response.addCookie(cookie);

//            user = new User("C1021H1", "CodeGym");

            //get all cookies
            Cookie[] cookies = request.getCookies();
            //iterate each cookie
            for (Cookie ck : cookies) {
                //display only the cookie with the name 'setUser'
                if (!ck.getName().equals("setUser")) {
                    ck.setValue("");
                }
                model.addAttribute("cookieValue", ck);
                break;
            }
            model.addAttribute("message", "Login success. Welcome ");
        } else {
            user.setAccount("");
            Cookie cookie = new Cookie("setUser", null);
            cookie.setMaxAge(0);
            cookie.setSecure(true);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
//            Cookie cookie = new Cookie("setUser", setUser);
            model.addAttribute("cookieValue", cookie);
            model.addAttribute("message", "Login failed. Try again.");
        }
        return "/index";
    }
}
