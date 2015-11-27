package com.soledede.recomend;

import java.util.List;

import com.soledede.recomend.util.scala.ModuleDAO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soledede.recomend.entity.Msg;

@RestController
public class RestRecomendController {

    @RequestMapping("/recommend")
    public Msg recommend(@RequestParam(value = "userid", defaultValue = "null") String userid) {
        if (userid == null) return new Msg("input, userid", -1, null);
        List result = null;
        try {
            result = new ModuleDAO().get(userid);
            if (result == null && result.size() <= 0) return new Msg("no info", 101, null);
        } catch (Exception e) {
            return new Msg("no iteminfo for  this user", 102, null);
        }
        return new Msg("sucesess", 0, result);
    }
}
