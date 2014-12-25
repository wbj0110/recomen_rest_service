package com.copal.recomend;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.copal.recomend.entity.Msg;
import com.copal.recomend.util.scala.ModuleDAO;

@RestController
public class RestRecomendController {

	@RequestMapping("/rest")
	public Msg greeting(
			@RequestParam(value = "cityid", defaultValue = "null") Long cityid,
			@RequestParam(value = "userid", defaultValue = "null") String userid) {
		if(cityid==null || userid==null) return new Msg("input cityie, userid", -1, null);
    	List result =null;
    	  try{
    		  result =   new ModuleDAO().get(cityid,userid);
    		  if(result ==null && result.size()<=0) return new Msg("no info", 101, null);
    	  }catch(Exception e){
    		  return new Msg("no resinfo for  this user", 102, null);
    	  }
    	  return new Msg("sucesess", 0, result);
	}
}
