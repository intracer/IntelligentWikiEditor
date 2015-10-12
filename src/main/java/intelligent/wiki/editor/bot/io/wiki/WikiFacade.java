/*
 * Copyright (C) 2015 Myroslav Rudnytskyi
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
package intelligent.wiki.editor.bot.io.wiki;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

/**
 * Class created to access Wikipedia data using it's API and it is simple
 * implementation of {@link WikiOperations}. Note, that all it's methods can
 * throw {@link IOException}.
 *
 * @author Myroslav Rudnytskyi
 * @version 01.10.2015
 */
public class WikiFacade implements WikiOperations {

	private final Logger log = Logger.getLogger(WikiFacade.class.getName());

	private final String apiUrl;

	private final String formatStr = "&format=xml";

	private final String queryStr = "?action=query";

	private final WikiQuery query = new WikiQuery();

	/**
	 * Constructor for creating object, using specified language.
	 *
	 * @param locale locale object, containing specified language
	 */
	public WikiFacade(Locale locale) {
		String langCode = locale == null ? Locale.getDefault().getLanguage() : locale.getLanguage();
		// https is necessary!
		apiUrl = "https://" + langCode + ".wikipedia.org/w/api.php";
	}

	private boolean isNullString(String arg, String methodName) {
		boolean result = arg == null;
		if (result) log.warning("Null string in " + methodName);
		return result;
	}

	private boolean isInvalidString(String arg, String methodName) {
		boolean result = isNullString(arg, methodName) || arg.isEmpty();
		if (result) log.warning("Invalid string in " + methodName);
		return result;
	}

	private boolean isInvalidNumber(long arg, String methodName) {
		boolean result = arg < 0;
		if (result) log.warning("Invalid number in " + methodName);
		return result;
	}

	private String normalize(String arg) {
		return arg.replace(' ', '_');
	}

	@Override
	public boolean existsArticle(String name) throws IOException {
		if (isInvalidString(name, "existsArticle")) return false;

		return !getArticleContent(name).isEmpty();
	}

	@Override
	public List<String> getArticlesStartingWith(String prefix, int limit) throws IOException {
		if (isNullString(prefix, "getArticlesStartingWith") ||
				isInvalidNumber(limit, "getArticlesStartingWith")) return Collections.emptyList();

		return getPagesStartingWith(prefix, limit, WikiNamespace.MAIN);
	}

	@Override
	public String getArticleContent(String name) throws IOException {
		if (isInvalidString(name, "getArticleContent")) return "";

		URL request = new URL(apiUrl + queryStr + formatStr + "&prop=revisions&rvprop=content&titles=" + normalize(name));
		return query.getArticleContent(request);
	}

	@Override
	public boolean existsCategory(String name) throws IOException {
		//TODO
		return false;
	}

	@Override
	public List<String> getCategoriesStartingWith(String prefix, int limit) throws IOException {
		if (isNullString(prefix, "getCategoriesStartingWith") ||
				isInvalidNumber(limit, "getCategoriesStartingWith")) return Collections.emptyList();

		return getPagesStartingWith(prefix, limit, WikiNamespace.CATEGORY);
	}

	@Override
	public boolean existsTemplate(String name) throws IOException {
		//TODO
		return false;
	}

	@Override
	public List<String> getTemplatesStartingWith(String prefix, int limit) throws IOException {
		if (isNullString(prefix, "getTemplatesStartingWith") ||
				isInvalidNumber(limit, "getTemplatesStartingWith")) return Collections.emptyList();

		return getPagesStartingWith(prefix, limit, WikiNamespace.TEMPLATE);
	}

	private List<String> getPagesStartingWith(String prefix, int limit, WikiNamespace namespace) throws IOException {
		URL request = new URL(apiUrl + queryStr + formatStr + "&list=allpages&apprefix=" + normalize(prefix) +
				"&apnamespace=" + namespace + "&aplimit=" + limit);
		return query.getPages(request);
	}
}