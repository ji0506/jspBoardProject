package pro17.se00;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardService {
	BoardDAO boardDAO;
	
	public BoardService() {
		boardDAO = new BoardDAO();
	}
	
	public Map<String,Object> listArticleVO(Map<String,Integer> pageMap) throws SQLException{

		Map<String,Object> map = new HashMap<>();
		List<ArticleVO> list = boardDAO.selectAllArticle(pageMap);
		int tot = boardDAO.selectTotArticle();
		map.put("list", list);
		map.put("tot", tot);
		
		return map;
	}

	public int add(ArticleVO vo)
	{
		int i = boardDAO.insert(vo);
		return i;
	}

	public ArticleVO view(int parseInt) {
		// TODO Auto-generated method stub
		return boardDAO.selectArticle(parseInt);
	}

	public void mod(ArticleVO articleVO) {
		boardDAO.update(articleVO);
	}

	public List<Integer> remove() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Integer> remove(int articleNO) {
		List<Integer> list = boardDAO.selectRemove(articleNO);
		boardDAO.delete(list);
		return list;
	}

}


