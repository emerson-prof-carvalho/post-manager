package model.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Company;
import model.ModelException;
import model.User;

public class MySQLCompanyDAO implements CompanyDAO {

	@Override
	public boolean save(Company company) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO companies VALUES (DEFAULT, ?, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		
		db.setString(1, company.getName());
		db.setString(2, company.getRole());
		db.setDate(3, company.getStart() == null ? new Date() : company.getStart());
			
		if (company.getEnd() == null)
			db.setNullDate(4);
		else db.setDate(4, company.getEnd());

		db.setInt(5, company.getUser().getId());
		
		return db.executeUpdate() > 0;	
	}

	@Override
	public boolean update(Company company) throws ModelException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Company company) throws ModelException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Company> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Company> companies = new ArrayList<Company>();
			
		// Declara uma instrução SQL
		String sqlQuery = " SELECT c.id as 'company_id', c.*, u.* \n"
				+ " FROM companies c \n"
				+ " INNER JOIN users u \n"
				+ " ON c.user_id = u.id;";
		
		db.createStatement();
	
		db.executeQuery(sqlQuery);

		while (db.next()) {
			User user = new User(db.getInt("user_id"));
			user.setName(db.getString("nome"));
			user.setGender(db.getString("sexo"));
			user.setEmail(db.getString("email"));
			
			Company company = new Company(db.getInt("company_id"));
			company.setName(db.getString("name"));
			company.setRole(db.getString("role"));
			company.setStart(db.getDate("start"));
			company.setEnd(db.getDate("end"));
			company.setUser(user);
			
			companies.add(company);
		}
		
		return companies;
	}

	@Override
	public Company findById(int id) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}
}
