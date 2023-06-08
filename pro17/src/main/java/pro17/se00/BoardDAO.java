package pro17.se00;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.PStmtKey;


public class BoardDAO {
	private DataSource dataFac;
	Connection conn;
	PreparedStatement stmt;

	
	public BoardDAO() {
		try {
			Context ctx = new InitialContext();
			Context envctx = (Context) ctx.lookup("java:comp/env");
			dataFac = (DataSource) envctx.lookup("jdbc/mariadb");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}	
	
	public List<ArticleVO> selectAllArticle(Map<String,Integer> pageMap) throws SQLException {
		// TODO Auto-generated method stub
		
		List<ArticleVO> list = new ArrayList<ArticleVO>();
		int section = (Integer) pageMap.get("section");
		int pageNum = (Integer) pageMap.get("pageNum");
		
		try {
			conn = dataFac.getConnection();
			String sql = "SELECT c.* FROM ( "
					+ "SELECT * FROM ( "
					+ "	WITH RECURSIVE board AS ( "
					+ "	   SELECT 1 AS LEVEL, b.*, b.articleNo AS topId "
					+ "		FROM t_board b WHERE b.parentNO = 0"
					+ "	   UNION ALL"
					+ "	   SELECT LEVEL + 1 AS LEVEL, b.*, l.topId "
					+ "	   FROM board l INNER JOIN t_board b ON  l.articleNo = b.parentNO"
					+ "	)"
					+ "	SELECT "
					+ "	   LEVEL,"
					+ "	   articleNO,"
					+ "	   parentNO,"
					+ "	   CONCAT(SPACE(4*(LEVEL-1)),title) AS title,"
					+ "	   title AS oldtitle,"
					+ "	   content,"
					+ "	   writedate,"
					+ "	   id,"
					+ "	   ROW_NUMBER() over(ORDER BY topId DESC, parentNO) AS rownum "
					+ "	FROM board "
					+ "	ORDER BY topId DESC, parentNO"
					+ "	) b "
					+ "WHERE rownum BETWEEN (?-1)*100+(?-1)*10+1 AND (?-1)*100+?*10 "
					+ ") c ";

			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, section);
			stmt.setInt(2, pageNum);
			stmt.setInt(3, section);
			stmt.setInt(4, pageNum);

			ResultSet re = stmt.executeQuery();
			
			while(re.next())
			{
				ArticleVO vo= new ArticleVO();
				vo.setLevel(re.getInt("LEVEL"));
				vo.setArticleNO(re.getInt("articleNO"));
				vo.setParentNO(re.getInt("parentNO"));
				vo.setTitle(re.getString("title"));
				vo.setContent(re.getString("content"));
				vo.setWriteDate(re.getDate("writedate"));
				vo.setId(re.getString("id"));
				list.add(vo);
			}
			re.close();
			stmt.close();
			conn.close();

			
		}catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return list;
	}

	
	public int insert(ArticleVO vo) {
		int articleNO = getNewNo();

		try {
			conn = dataFac.getConnection();

			String sql = "insert into t_board(articleNO,parentNO,title,content,imageFileName,id) values(?,?,?,?,?,?)";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, articleNO);
			stmt.setInt(2, vo.getParentNO());
			stmt.setString(3, vo.getTitle());
			stmt.setString(4, vo.getContent());
			stmt.setString(5, vo.getImageFileName());
			stmt.setString(6, vo.getId());

			stmt.executeUpdate();
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return articleNO;
	}
	
	
	public int getNewNo() {

		int i = -1;
		try {
			conn = dataFac.getConnection();

			String sql = "select max(articleNO) from t_board";
			stmt = conn.prepareStatement(sql);

			ResultSet re = stmt.executeQuery();
			
			if(re.next())
			{
				i = re.getInt(1)+1;
			}
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	public ArticleVO selectArticle(int parseInt) {
		// TODO Auto-generated method stub
		
		ArticleVO vo = null;
		try {
			conn = dataFac.getConnection();

			String sql = "select * from t_board where articleNO=?";
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, parseInt);

			ResultSet re = stmt.executeQuery();
			
			if(re.next())
			{
				vo= new ArticleVO();
				vo.setArticleNO(re.getInt("articleNO"));
				vo.setParentNO(re.getInt("parentNO"));
				vo.setTitle(re.getString("title"));
				vo.setContent(re.getString("content"));
				vo.setWriteDate(re.getDate("writedate"));
				vo.setId(re.getString("id"));
				vo.setImageFileName(re.getString("imageFileName"));
			}
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return vo;
	}

	public Object update(ArticleVO vo) {
		
		try {
			
			conn = dataFac.getConnection();
			String imageFileName = vo.getImageFileName();
			String sql = "update t_board  set title=?,content=?";
			if (imageFileName != null && imageFileName.length() != 0) {
				sql += ",imageFileName=?";
			}
			sql += " where articleNO=?";

			
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			stmt.setString(1, vo.getTitle());
			stmt.setString(2, vo.getContent());
			stmt.setString(3, vo.getImageFileName());
			stmt.setInt(4, vo.getArticleNO());

			stmt.executeUpdate();
			stmt.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<Integer> selectRemove(int articleNO) {
		
		
		List<Integer> list  = new ArrayList<>();
		try {
			conn = dataFac.getConnection();
			String sql = "WITH RECURSIVE board AS ("
					+ "   SELECT b.*"
					+ "   FROM t_board b WHERE b.articleNo = ?"
					+ "   UNION ALL"
					+ "   SELECT b.*"
					+ "   FROM board l INNER JOIN t_board b ON  l.articleNo = b.parentNO"
					+ "	) "
					+ "SELECT articleNo FROM board	";
			
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, articleNO);


			ResultSet re = stmt.executeQuery();
			
			if(re.next())
			{
				int i = re.getInt(1);
				list.add(i);
			}
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return list;
	}

	public void delete(List<Integer> list) {
	
		try {
			conn = dataFac.getConnection();
			String sql = "delete from t_board where articleNo in(";
			
			for(int i =0; i < list.size();i++)
				if( i == 0)
					sql += list.get(i);
				else
					sql += "," + list.get(i);
			
			sql += ")";
			stmt = conn.prepareStatement(sql);

			stmt.executeUpdate();
			stmt.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	public int selectTotArticle() {
		int i = -1;
		try {
			conn = dataFac.getConnection();

			String sql = "select count(articleNO) from t_board";
			stmt = conn.prepareStatement(sql);

			ResultSet re = stmt.executeQuery();
			
			if(re.next())
			{
				i = re.getInt(1);
			}
			stmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return i;
	}

	

}
