/*
 * Copyright (C) 2016 Myroslav Rudnytskyi
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 */

package intelligent.wiki.editor.web.spring_mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Main controller of web application is used as entry point.
 *
 * @author Myroslav Rudnytskyi
 * @version 26.04.2016
 */
@Controller
public class IntelligentWikiEditorController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String showHome(ModelMap model) {

		model.addAttribute("message", "Spring 3 MVC Hello World");
		return "home";

	}

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
	public ModelAndView showDashboard() {

		ModelAndView model = new ModelAndView();
		model.setViewName("dashboard");
		return model;

	}
}
