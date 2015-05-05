package com.mytest.utils.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

public class HkDaoSupport extends JdbcDaoSupport {

	public int[] batchUpdate(String sql, BatchPreparedStatementSetter bpss) {
		try {
			return this.getJdbcTemplate().batchUpdate(sql, bpss);
		}
		catch (DataAccessException e) {
			e.printStackTrace();
			throw e;
		}
	}

	protected Connection getCurrentConnection() {
		Connection con = this.getConnection();
		return con;
	}

	public Number insertObject(String sql, Object... values) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = this.getCurrentConnection();
		try {
			ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			if (values != null) {
				int i = 1;
				for (Object value : values) {
					ps.setObject(i++, value);
				}
			}
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			if (rs.next()) {
				return (Number) rs.getObject(1);
			}
			return 0;
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

	public <T> List<T> query(String sql, int begin, int size,
			ParameterizedRowMapper<T> rm, Object... values) {
		return this.query(sql + " limit " + begin + "," + size, rm, values);
	}

	public <T, E> List<T> queryInId(String tableName, String fields,
			String idField, List<E> idList, ParameterizedRowMapper<T> mapper,
			Object... values) {
		if (idList.isEmpty()) {
			return new ArrayList<T>();
		}
		StringBuilder sql = new StringBuilder("select ");
		sql.append(fields).append(" from ").append(tableName).append(" where ");
		sql.append(idField).append(" in (");
		for (E o : idList) {
			sql.append(o.toString()).append(",");
		}
		sql.deleteCharAt(sql.length() - 1).append(")");
		return this.query(sql.toString(), mapper, values);
	}

	public <T> List<T> query(String sql, ParameterizedRowMapper<T> rm,
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
			List<T> list = new ArrayList<T>();
			while (rs.next()) {
				list.add(rm.mapRow(rs, rowNum++));
			}
			return list;
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

	public Number queryForNumber(String sql, Object... values) {
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
			int size = 0;
			Number res = 0;
			while (rs.next()) {
				res = (Number) rs.getObject(1);
				size++;
			}
			if (size == 0) {
				return res;
			}
			if (size > 1) {
				throw new IncorrectResultSizeDataAccessException(1, size);
			}
			return res;
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

	public <T> T queryForObject(String sql, ParameterizedRowMapper<T> rm,
			Object... values) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection con = this.getCurrentConnection();
		try {
			ps = con.prepareStatement(sql + " limit 1");
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

	public int update(String sql, Object... values) {
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
			int rows = ps.executeUpdate();
			return rows;
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