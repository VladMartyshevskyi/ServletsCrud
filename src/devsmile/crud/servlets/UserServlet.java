package devsmile.crud.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import devsmile.crud.dao.UserDao;
import devsmile.crud.models.User;

public class UserServlet extends HttpServlet {

	private UserDao userDao;
	private Gson gson = new Gson();

	@Override
	public void init() throws ServletException {
		String jdbcURL = getServletContext().getInitParameter("jdbcURL");
		String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
		String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");
		userDao = new UserDao(jdbcURL, jdbcUsername, jdbcPassword);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		String action = req.getPathInfo();
		
		try {
			switch (action) {
				case "/all":
					sendAsJson(resp, userDao.getAll());
					break;
				case "/get": {
					int id = Integer.parseInt(req.getParameter("id"));
					sendAsJson(resp, userDao.getById(id));
					break;
				}
				case "/delete": {
					int id = Integer.parseInt(req.getParameter("id"));
					userDao.delete(id);
					break;
				}
				default:
					sendAsJson(resp, userDao.getAll());
			}

		} catch (Exception e) {
			throw new ServletException();
		}
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String body = getRequestBody(request);
		String action = request.getPathInfo();
		try {
			switch (action) {
				case "/add":
					userDao.add(gson.fromJson(body, User.class));
					break;
				case "/update":
					userDao.update(gson.fromJson(body, User.class));
					break;
				default:
					sendAsJson(response, userDao.getAll());
			}

		} catch (Exception e) {
			throw new ServletException();
		}
	}

	private void sendAsJson(HttpServletResponse response, Object obj) throws IOException {

		response.setContentType("application/json");
		String res = gson.toJson(obj);

		PrintWriter out = response.getWriter();
		out.print(res);
		out.flush();
	}

	private String getRequestBody(HttpServletRequest request) throws IOException {
		String lines = "";
		while (request.getReader().ready()) {
			lines += request.getReader().readLine();
		}
		return lines;
	}
}
