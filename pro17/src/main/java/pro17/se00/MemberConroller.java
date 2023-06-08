package pro17.se00;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



@WebServlet("/member/*")
public class MemberConroller extends HttpServlet {
	private static final long serialVersionUID = 1L;
    MemberDAO dao;   
	
    public MemberConroller() {
        super();
    }
    
	@Override
	public void init() throws ServletException {
		
		dao = new MemberDAO();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String nextPage = null;
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=UTF-8");

		String action = request.getPathInfo();		
		
		if(action == null || "list.do".equals(action))
		{
			List<MemberVO> memList = dao.listMembers();
			request.setAttribute("memList", memList);
			nextPage = "/view/member.jsp";
			
		} else if (action.equals("/add.do")){
			MemberVO vo = new MemberVO();
			vo.setId(request.getParameter("id"));
			vo.setPwd(request.getParameter("pwd"));
			vo.setEmail(request.getParameter("email"));
			vo.setName(request.getParameter("name"));
			dao.addMember(vo);
			request.setAttribute("msg","add");
			nextPage = "/member/list.do";
			
		} else if(action.equals("/memberForm.do")) {
			nextPage = "/view/memberForm.jsp";
			
		}else if(action.equals("/modForm.do")) {
			String id = request.getParameter("id");
			MemberVO vo = dao.findMembers(id);
			request.setAttribute("info", vo);
			nextPage = "/view/modMemberForm.jsp";
		} else if(action.equals("/mod.do")) {
			MemberVO vo = new MemberVO();
			vo.setId(request.getParameter("id"));
			vo.setPwd(request.getParameter("pwd"));
			vo.setEmail(request.getParameter("email"));
			vo.setName(request.getParameter("name"));
			dao.modMember(vo);
			request.setAttribute("msg","mod");
			nextPage = "/member/list.do";			
		} else if(action.equals("/del.do")) {
			String id = request.getParameter("id");
			dao.delMember(id);
			request.setAttribute("msg", "del");
			nextPage = "/member/list.do";			
		}
		else {
			List<MemberVO> memList = dao.listMembers();
			request.setAttribute("memList", memList);
			nextPage = "/view/member.jsp";
			
		}

		RequestDispatcher dis = request.getRequestDispatcher(nextPage);
		dis.forward(request, response);

	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}


}
