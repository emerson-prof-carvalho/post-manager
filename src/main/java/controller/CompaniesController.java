package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Company;
import model.ModelException;
import model.User;
import model.dao.CompanyDAO;
import model.dao.DAOFactory;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/companies", "/company/form", "/company/delete", "/company/insert", "/company/update"})
public class CompaniesController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/post-manager/company/form": {
			CommonsController.listUsers(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-company.jsp");
			break;
		}
		default:
			listCompanies(req);
			
			ControllerUtil.transferSessionMessagesToRequest(req);
			
			ControllerUtil.forward(req, resp, "/companies.jsp");
		}
	}
	
	private void listCompanies(HttpServletRequest req) {
		CompanyDAO dao = DAOFactory.createDAO(CompanyDAO.class);
		
		List<Company> companies = null;
		
		try {
			companies = dao.listAll();
		} catch (ModelException e) {
			
			e.printStackTrace();
		}
		
		if (companies != null)
			req.setAttribute("companies", companies);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/post-manager/company/insert": {
			insertCompany(req, resp);
			break;
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + action);
		}

		ControllerUtil.redirect(resp, req.getContextPath() + "/companies");
	}

	private void insertCompany(HttpServletRequest req, HttpServletResponse resp) {
		String companyName = req.getParameter("name");
		String role = req.getParameter("role");
		Integer userId = Integer.parseInt(req.getParameter("user"));
		String start = req.getParameter("start");
		String end = req.getParameter("end");
		
		Company company = new Company();
		company.setName(companyName);
		company.setRole(role);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date startDate = null;
		try {
			startDate = dateFormat.parse(start);
		} catch (ParseException pe) {
			startDate = new Date();
		}
		company.setStart(startDate);
		
		Date endDate = null;
		try {
			endDate = dateFormat.parse(end);
		} catch (ParseException pe) {}
		company.setEnd(endDate);

		User user = new User(userId);
		company.setUser(user);

		CompanyDAO dao = DAOFactory.createDAO(CompanyDAO.class);

		try {
			if (dao.save(company)) {
				ControllerUtil.sucessMessage(req, 
						"Empresa '" + company.getName() + "' salva com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, 
						"Empresa '" + company.getName() + "' não pode ser salva.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}
}
