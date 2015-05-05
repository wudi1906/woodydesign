package com.mytest.utils.dao;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import com.mytest.utils.entity.PageModule;
import com.mytest.utils.entity.VoMapper;


public class JdbcDaoSupport extends SimpleJdbcDaoSupport {

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

	protected String getLastChar(Number id) {
		String ss = id + "";
		if (ss.equals("0")) {
			throw new IllegalArgumentException("Id is 0");
		}
		int len = ss.length();
		if (len == 0) {
			throw new IllegalArgumentException("Id is null");
		}
		if (len > 1) {
			ss = ss.substring(ss.length() - 1, ss.length());
		}
		try {
			Long.parseLong(ss);
		}
		catch (NumberFormatException e) {
			throw new IllegalArgumentException("Id is less than 0");
		}
		return ss;
	}

	protected String getModTableName(String tableName, Number id) {
		StringBuilder sb = new StringBuilder();
		sb.append(tableName);
		sb.append(this.getLastChar(id));
		return sb.toString();
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

	@SuppressWarnings("unchecked")
	public PageModule findPageList(String sql, int pageIndex, int pageSize,
			Class voClass) throws SQLException {
		return findPageList(sql, null, pageIndex, pageSize, voClass);
	}

	@SuppressWarnings("unchecked")
	public PageModule findPageList(String sql, Object[] parameters,
			int pageNum, int rowNums, Class voclass) throws SQLException {
		Connection conn = getConnection();
		String sqld = "";
		PageModule pg = new PageModule();
		int totalRows = 0;
		sqld = sql + " limit " + (pageNum - 1) * rowNums + " ," + rowNums;
		// 查询总记录数
		try {
			totalRows = countData(sql, conn, parameters);
			pg.m_nItems = totalRows;
			List pageLt = find(sqld, conn, parameters, voclass);
			pg.setCollection(pageLt);
			pg.setPageInfo(rowNums, pageNum, totalRows);
		}
		finally {
			DataSourceUtils.releaseConnection(conn, getDataSource());
		}
		return pg;
	}

	/**
	 * 执行一个查询,获得连接，不关闭连接。关闭ResultSet
	 *
	 * @param sql
	 *            需要执行的sql语句
	 * @return ResultSet
	 */
	public <T> List<T> find(String sql, Object[] parameters, int pageIndex,
			int pageSize, Class<T> voclass) throws SQLException {
		Connection conn = getConnection();
		List<T> list = new ArrayList<T>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			pstmt.setFetchSize(50);
			sql = sql + " limit " + (pageIndex - 1) * pageSize + " ,"
					+ pageSize;
			// 动态设置参数
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					pstmt.setObject(i + 1, parameters[i]);
				}
			}
			rs = pstmt.executeQuery();
			T t = null;
			while (rs.next()) {
				t = voclass.newInstance();
				setRSdata(t, rs);
				list.add(t);
			}
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(pstmt);
		}
		return list;
	}

	public <T> List<T> find(String sql, int pageIndex, int pageSize,
			Class<T> voclass) throws SQLException {
		return find(sql, null, pageIndex, pageSize, voclass);
	}

	/**
	 * 执行一个查询,获得连接，不关闭连接。关闭ResultSet
	 *
	 * @param sql
	 *            需要执行的sql语句
	 * @return ResultSet
	 */
	public <T> List<T> find(String sql, Connection conn, Object[] parameters,
			Class<T> voclass) throws SQLException {
		List<T> list = new ArrayList<T>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			pstmt.setFetchSize(50);
			// 动态设置参数
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					pstmt.setObject(i + 1, parameters[i]);
				}
			}
			rs = pstmt.executeQuery();
			T t = null;
			while (rs.next()) {
				t = voclass.newInstance();
				setRSdata(t, rs);
				list.add(t);
			}
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(pstmt);
		}
		return list;
	}

	/**
	 * 执行一个查询,获得连接，不关闭连接。关闭ResultSet
	 *
	 * @param sql
	 *            需要执行的sql语句
	 * @return ResultSet
	 */
	public <T> List<T> find(String sql, Object[] parameters, Class<T> voclass)
			throws SQLException {
		Connection conn = getConnection();
		List<T> list = new ArrayList<T>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(sql,
					ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			pstmt.setFetchSize(50);
			// 动态设置参数
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					pstmt.setObject(i + 1, parameters[i]);
				}
			}
			rs = pstmt.executeQuery();
			T t = null;
			while (rs.next()) {
				t = voclass.newInstance();
				setRSdata(t, rs);
				list.add(t);
			}
		}
		catch (InstantiationException e) {
			e.printStackTrace();
		}
		catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(pstmt);
			DataSourceUtils.releaseConnection(conn, getDataSource());
		}
		return list;
	}

	/**
	 * 查询数据列表，并封装为对象
	 *
	 * @param <T>
	 * @param sql
	 * @param conn
	 * @param voclass
	 *            封装对象的类对象
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> find(String sql, Class<T> voclass) throws SQLException {
		Object paramters[] = null;
		return find(sql, paramters, voclass);
	}

	public <T> T findObject(String sql, Class<T> voclass, Object[] params) {
		List<T> list;
		try {
			list = this.find(sql + " limit 1", params, voclass);
		}
		catch (SQLException e) {
			throw new RuntimeException(e);
		}
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}

	/**
	 * 查询数据列表，并封装为对象
	 *
	 * @param <T>
	 * @param sql
	 * @param conn
	 * @param voclass
	 *            封装对象的类对象
	 * @return
	 * @throws SQLException
	 */
	public <T> List<T> find(String sql, Connection conn, Class<T> voclass)
			throws SQLException {
		return find(sql, conn, null, voclass);
	}

	/**
	 * 查询数据的统计
	 *
	 * @param sql
	 * @param values
	 * @return
	 */
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

	/**
	 * 封装RS数据的方法，即把RS中的数据封装为某个对象
	 *
	 * @param voclass
	 * @param rs
	 */
	@SuppressWarnings("unchecked")
	private void setRSdata(Object vo, ResultSet rs) {
		Class classObj = vo.getClass();
		Field fileds[] = classObj.getDeclaredFields();
		VoMapper annotation = null;
		if (fileds.length > 0) {
			String fieldName = "";
			try {
				// Object obj = voclass.newInstance();
				ResultSetMetaData metad = rs.getMetaData();
				for (Field f : fileds) {
					annotation = f.getAnnotation(VoMapper.class);
					if (annotation != null) {
						fieldName = annotation.name();
					}
					else {
						fieldName = f.getName();
					}
					f.setAccessible(true);
					if (checkFiled(metad, fieldName)) {
						setVal(f, vo, fieldName, rs);
					}
				}
			}
			catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
			catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean checkFiled(ResultSetMetaData md, String fieldname)
			throws SQLException {
		int count = md.getColumnCount();
		for (int i = 0; i < count; i++) {
			if (fieldname.toLowerCase().equals(
					md.getColumnName(i + 1).toLowerCase())) {

				return true;
			}
		}
		return false;
	}

	private void setVal(Field f, Object vo, String fieldName, ResultSet rs)
			throws IllegalArgumentException, IllegalAccessException,
			SQLException {
		String type = f.getType().toString();
		if (type.indexOf("java.") != -1) {
			type = f.getType().toString().substring(type.indexOf("java."));
		}
		if ("java.util.Date".equals(type)) {
			f.set(vo, rs.getTimestamp(fieldName));
		}
		else if ("java.lang.String".equals(type)) {
			f.set(vo, rs.getString(fieldName));
		}
		else if ("java.lang.Long".equals(type) || "long".equals(type)) {
			f.set(vo, rs.getLong(fieldName));
		}
		else if ("java.lang.Integer".equals(type) || "int".equals(type)) {
			f.set(vo, rs.getInt(fieldName));
		}
		else if ("java.lang.Double".equals(type) || "double".equals(type)) {
			f.set(vo, rs.getDouble(fieldName));
		}
		else if ("java.lang.Float".equals(type) || "float".equals(type)) {
			f.set(vo, rs.getFloat(fieldName));
		}
	}

	private int countData(String sql, Connection conn, Object[] parameters) {
		PreparedStatement st = null;
		ResultSet rt = null;
		int rows = 0;
		// if(sql.trim().toLowerCase().startsWith("select")){
		// sql = sql.substring(sql.toLowerCase().indexOf("from"));
		// }
		// String sql1 = "select count(*) c " + sql + "";
		String sql1 = "select count(*) c from ( " + sql + " ) exdb";
		try {
			st = conn.prepareStatement(sql1, ResultSet.TYPE_SCROLL_INSENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			// 动态设置参数
			if (parameters != null && parameters.length > 0) {
				for (int i = 0; i < parameters.length; i++) {
					st.setObject(i + 1, parameters[i]);
				}
			}
			rt = st.executeQuery();
			if (rt.next())
				rows = rt.getInt("c");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (null != rt) {
					rt.close();
					rt = null;
				}
				if (null != st) {
					st.close();
					st = null;
				}
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rows;
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
}