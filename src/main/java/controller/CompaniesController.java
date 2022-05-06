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
import model.dao.UserDAO;

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
		case "/post-manager/company/update":{
			CommonsController.listUsers(req);
			req.setAttribute("action", "update");
			Company c = loadCompany(req);
			req.setAttribute("company", c);
			ControllerUtil.forward(req, resp, "/form-company.jsp");
			break;
		}
		default:
			listCompanies(req);
			
			ControllerUtil.transferSessionMessagesToRequest(req);
			
			ControllerUtil.forward(req, resp, "/companies.jsp");
		}
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
		case "/post-manager/company/update":{
			updateCompany(req, resp);
			break;
		}
		case "/post-manager/company/delete":{
			deleteCompany(req, resp);
			break;
		}
		default:
			System.out.println("URL inválida " + action);
		}

		ControllerUtil.redirect(resp, req.getContextPath() + "/companies");
	}

	private void deleteCompany(HttpServletRequest req, HttpServletResponse resp) {
		String companyIdParameter = req.getParameter("id");
		
		int companyId = Integer.parseInt(companyIdParameter);
		
		CompanyDAO dao = DAOFactory.createDAO(CompanyDAO.class);
		
		try {
			Company company = dao.findById(companyId);
			
			if (company == null)
				throw new ModelException("Empresa não encontrada para deleção.");
			
			if (dao.delete(company)) {
				ControllerUtil.sucessMessage(req, "Empresa '" + company.getName() + "' deletada com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Empresa '" + company.getName() + "' não pode ser deletada.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void updateCompany(HttpServletRequest req, HttpServletResponse resp) {
		String companyName = req.getParameter("name");
		String companyRole = req.getParameter("role");
		String start = req.getParameter("start");
		String end = req.getParameter("end");
		Integer userId = Integer.parseInt(req.getParameter("user"));
		
		Company company = loadCompany(req);
		company.setName(companyName);
		company.setRole(companyRole);
		
		Date startDate = formatDate(start);
		company.setStart(startDate);

		Date endDate = formatDate(end);
		company.setEnd(endDate);
		
		User user = new User(userId);
		company.setUser(user);
		
		CompanyDAO dao = DAOFactory.createDAO(CompanyDAO.class);
		
		try {
			if (dao.update(company)) {
				ControllerUtil.sucessMessage(req, "Empresa '" + company.getName() + "' atualizada com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Empresa '" + company.getName() + "' não pode ser atualizada.");
			}				
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}		
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
		
		Date startDate = formatDate(start);
		company.setStart(startDate);
		
		Date endDate = formatDate(end);
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

	private Date formatDate(String stringDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.parse(stringDate);
		} catch (ParseException pe) {
			return null;
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
	
	private Company loadCompany(HttpServletRequest req) {
		String companyIdParameter = req.getParameter("companyId");
		
		int companyId = Integer.parseInt(companyIdParameter);
		
		CompanyDAO dao = DAOFactory.createDAO(CompanyDAO.class);
		
		try {
			Company c = dao.findById(companyId);
			
			if (c == null)
				throw new ModelException("Empresa não encontrada para alteração");
			
			return c;
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
		
		return null;
	}
}
