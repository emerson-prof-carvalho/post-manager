package model.dao;

import java.util.List;

import model.Company;
import model.ModelException;

public class MySQLCompanyDAO implements CompanyDAO {

	@Override
	public boolean save(Company company) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO companies VALUES (DEFAULT, ?, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		
		db.setString(1, company.getName());
		db.setString(2, company.getRole());
		db.setDate(3, company.getStart());
		db.setDate(4, company.getEnd());
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Company findById(int id) throws ModelException {
		// TODO Auto-generated method stub
		return null;
	}
}
