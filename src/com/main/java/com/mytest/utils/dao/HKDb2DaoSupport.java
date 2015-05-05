package com.mytest.utils.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

public class HKDb2DaoSupport extends HkDaoSupport {

	protected Connection getCurrentConnection() {
		Connection con = this.getConnection();
		return con;
	}

	public <T> List<T> query(String sql, int begin, int size,
			ParameterizedRowMapper<T> rm, Object... values) {
		return this.query(sql, rm, values);
	}

	@Override
	public <T> T queryForObject(String sql, ParameterizedRowMapper<T> rm,
			Object... values) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = this.getCurrentConnection();
		try {
			ps = con.prepareStatement(sql);
			if (values != null) {
				for (int i = 0; i < values.length; i++) {
					ps.setObject(i + 1, values[i]);
				}
			}
			rs = ps.executeQuery();
			int rowNum = 0;
			T t = null;
			while (rs.next()) {
				t = rm.mapRow(rs, rowNum++);
			}
			if (rowNum == 0) {
				return null;
			}
			if (rowNum > 1) {
				throw new IncorrectResultSizeDataAccessException(1, rowNum);
			}
			return t;
		}
		catch (SQLException e) {
			JdbcUtils.closeStatement(ps);
			ps = null;
			DataSourceUtils.releaseConnection(con, getDataSource());
			con = null;
			e.printStackTrace();
			throw getExceptionTranslator().translate("StatementCallback", sql,
					e);
		}
		finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
			DataSourceUtils.releaseConnection(con, getDataSource());
		}
	}
}