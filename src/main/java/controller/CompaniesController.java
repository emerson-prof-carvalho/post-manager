package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;

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
			CompanyDAO dao = DAOFactory.createDAO(CompanyDAO.class);
			
			Company company = new Company();
			company.setName("Face");
			company.setRole("Programador");
			company.setStart(Date.valueOf(java.time.LocalDate.now()));
			company.setEnd(Date.valueOf(java.time.LocalDate.now()));
			company.setUser(new User(1));
			
			try {
				dao.save(company);
			} catch (ModelException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			PrintWriter writer = resp.getWriter();
			writer.append("Salvou");
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + action);
		}
	}
}
