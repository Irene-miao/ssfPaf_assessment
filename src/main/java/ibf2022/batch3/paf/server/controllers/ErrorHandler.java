package ibf2022.batch3.paf.server.controllers;

import org.springframework.http.HttpStatusCode;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandler {
    
    @ExceptionHandler({Exception.class})
    public String handleError(Exception e, Model m){
        m.addAttribute("error", e.getMessage());
        return "error";
    }

    @ExceptionHandler({RestaurantException.class})
    public ModelAndView handleRestaurantError(RestaurantException e){
        ModelAndView mv = new ModelAndView();

        mv.setViewName("error");
        mv.addObject("message", e.getMessage());
        mv.setStatus(HttpStatusCode.valueOf(400));
        return mv;
    }
}
